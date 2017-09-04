package blue.aodev.animeultimetv.utils.extensions

fun <K, V> mapOf(pairs: List<Pair<K, V>>): Map<K, V> {
    return if (pairs.isNotEmpty()) {
        linkedMapOf(*pairs.toTypedArray())
    } else {
        emptyMap()
    }
}

fun <K, V> Map<K, V>.sliceIfPresent(keys: List<K>): List<V> {
    val result = ArrayList<V>(keys.size)
    keys.forEach { key ->
        val value = this[key]
        if (value != null) result.add(value)
    }
    return result
}

fun <K, V> Map<K, V>.sliceOrNull(keys: List<K>): List<V?> {
    val result = ArrayList<V>(keys.size)
    keys.forEach { key ->
        val value = this[key]
        if (value != null) result.add(value)
    }
    return result
}

fun <K, V> Map<K, V>.sliceOrDefault(keys: List<K>, default: V): List<V> {
    val result = ArrayList<V>(keys.size)
    keys.forEach { key ->
        val value = this.getOrDefault(key, default)
        result.add(value)
    }
    return result
}
