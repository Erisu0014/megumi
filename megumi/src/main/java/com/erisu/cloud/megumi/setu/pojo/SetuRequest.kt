package com.erisu.cloud.megumi.setu.pojo

data class SetuRequest(
    var r18: Int = 0,
    var num: Int = 1,
    var uid: IntArray?,
    var keyword: String?,
    var tag: Array<Any>?,
    var size: Array<String> = arrayOf("original"),
    var proxy: String = "i.pixiv.cat",
    var dateAfter: Long?,
    var dateBefore: Long?,
    var dsc: Boolean = false,

    ) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SetuRequest

        if (r18 != other.r18) return false
        if (num != other.num) return false
        if (uid != null) {
            if (other.uid == null) return false
            if (!uid.contentEquals(other.uid)) return false
        } else if (other.uid != null) return false
        if (keyword != other.keyword) return false
        if (tag != null) {
            if (other.tag == null) return false
            if (!tag.contentEquals(other.tag)) return false
        } else if (other.tag != null) return false
        if (!size.contentEquals(other.size)) return false
        if (proxy != other.proxy) return false
        if (dateAfter != other.dateAfter) return false
        if (dateBefore != other.dateBefore) return false
        if (dsc != other.dsc) return false

        return true
    }

    override fun hashCode(): Int {
        var result = r18
        result = 31 * result + num
        result = 31 * result + (uid?.contentHashCode() ?: 0)
        result = 31 * result + keyword.hashCode()
        result = 31 * result + (tag?.contentHashCode() ?: 0)
        result = 31 * result + size.contentHashCode()
        result = 31 * result + proxy.hashCode()
        result = 31 * result + (dateAfter?.hashCode() ?: 0)
        result = 31 * result + (dateBefore?.hashCode() ?: 0)
        result = 31 * result + dsc.hashCode()
        return result
    }
}
