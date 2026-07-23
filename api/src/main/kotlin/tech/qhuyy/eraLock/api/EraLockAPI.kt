package tech.qhuyy.eraLock.api

interface EraLockAPI {

    fun isLocked(worldType: WorldType): Boolean

    /**
     * @param announce null = use config toggle, true = force announce, false = suppress
     */
    fun lock(worldType: WorldType, announce: Boolean? = null)

    /**
     * @param announce null = use config toggle, true = force announce, false = suppress
     */
    fun unlock(worldType: WorldType, announce: Boolean? = null)
}
