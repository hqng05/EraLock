package tech.qhuyy.eraLock.detector

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace

object NetherPortalDetector {

    const val MIN_WIDTH = 2
    const val MAX_WIDTH = 21
    const val MIN_HEIGHT = 3
    const val MAX_HEIGHT = 21

    private fun isFrameBlock(block: Block): Boolean = block.type == Material.OBSIDIAN

    private fun isEmpty(block: Block): Boolean = when (block.type) {
        Material.AIR, Material.CAVE_AIR, Material.VOID_AIR,
        Material.FIRE, Material.SOUL_FIRE, Material.NETHER_PORTAL -> true
        else -> false
    }

    enum class Axis { X, Z }

    data class PortalShapeResult(
        val axis: Axis,
        val rightDir: BlockFace,
        val bottomLeft: Block,
        val width: Int,
        val height: Int,
        val numPortalBlocks: Int
    ) {
        fun isValid(): Boolean =
            width in MIN_WIDTH..MAX_WIDTH && height in MIN_HEIGHT..MAX_HEIGHT

        fun isEmptyValidPortal(): Boolean = isValid() && numPortalBlocks == 0

        fun isComplete(): Boolean = isValid() && numPortalBlocks == width * height
    }

    fun wouldIgniteCreatePortal(ignitedBlock: Block, preferredAxis: Axis = Axis.X): Boolean {
        val first = findAnyShape(ignitedBlock, preferredAxis)
        if (first.isEmptyValidPortal()) return true

        val other = if (preferredAxis == Axis.X) Axis.Z else Axis.X
        val second = findAnyShape(ignitedBlock, other)
        return second.isEmptyValidPortal()
    }

    fun findAnyShape(startPos: Block, axis: Axis): PortalShapeResult {
        val rightDir = if (axis == Axis.X) BlockFace.WEST else BlockFace.SOUTH

        val bottomLeft = calculateBottomLeft(startPos, rightDir)
            ?: return PortalShapeResult(axis, rightDir, startPos, 0, 0, 0)

        val width = calculateWidth(bottomLeft, rightDir)
        if (width == 0) {
            return PortalShapeResult(axis, rightDir, bottomLeft, 0, 0, 0)
        }

        val portalBlockCount = intArrayOf(0)
        val height = calculateHeight(bottomLeft, rightDir, width, portalBlockCount)
        return PortalShapeResult(axis, rightDir, bottomLeft, width, height, portalBlockCount[0])
    }

    private fun calculateBottomLeft(startPos: Block, rightDir: BlockFace): Block? {
        var pos = startPos
        val minY = pos.world.minHeight
        val floorLimit = maxOf(minY, pos.y - MAX_WIDTH)

        while (pos.y > floorLimit && isEmpty(pos.getRelative(BlockFace.DOWN))) {
            pos = pos.getRelative(BlockFace.DOWN)
        }

        val leftDir = rightDir.oppositeFace
        val stepsToFrame = traceRowToFrame(pos, leftDir)
        if (stepsToFrame <= 0) return null
        return pos.getRelative(leftDir, stepsToFrame - 1)
    }

    private fun calculateWidth(bottomLeft: Block, rightDir: BlockFace): Int {
        val width = traceRowToFrame(bottomLeft, rightDir)
        return if (width in MIN_WIDTH..MAX_WIDTH) width else 0
    }

    private fun traceRowToFrame(pos: Block, direction: BlockFace): Int {
        for (step in 0..MAX_WIDTH) {
            val cur = pos.getRelative(direction, step)
            if (!isEmpty(cur)) {
                return if (isFrameBlock(cur)) step else -1
            }
            val below = cur.getRelative(BlockFace.DOWN)
            if (!isFrameBlock(below)) return -1
        }
        return -1
    }

    private fun calculateHeight(
        bottomLeft: Block,
        rightDir: BlockFace,
        width: Int,
        portalBlockCountOut: IntArray
    ): Int {
        val height = getDistanceUntilTop(bottomLeft, rightDir, width, portalBlockCountOut)
        return if (height in MIN_HEIGHT..MAX_HEIGHT && hasTopFrame(bottomLeft, rightDir, width, height)) {
            height
        } else {
            0
        }
    }

    private fun hasTopFrame(bottomLeft: Block, rightDir: BlockFace, width: Int, height: Int): Boolean {
        for (i in 0 until width) {
            val framePos = bottomLeft.getRelative(BlockFace.UP, height).getRelative(rightDir, i)
            if (!isFrameBlock(framePos)) return false
        }
        return true
    }

    private fun getDistanceUntilTop(
        bottomLeft: Block,
        rightDir: BlockFace,
        width: Int,
        portalBlockCountOut: IntArray
    ): Int {
        val leftCol = rightDir.oppositeFace
        for (h in 0 until MAX_HEIGHT) {
            val left = bottomLeft.getRelative(BlockFace.UP, h).getRelative(leftCol, 1)
            if (!isFrameBlock(left)) return h

            val right = bottomLeft.getRelative(BlockFace.UP, h).getRelative(rightDir, width)
            if (!isFrameBlock(right)) return h

            for (i in 0 until width) {
                val mid = bottomLeft.getRelative(BlockFace.UP, h).getRelative(rightDir, i)
                if (!isEmpty(mid)) return h
                if (mid.type == Material.NETHER_PORTAL) portalBlockCountOut[0]++
            }
        }
        return MAX_HEIGHT
    }
}
