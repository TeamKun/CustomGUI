package kotx.ktools

fun <T> Collection<T?>.anyNull() = any { it == null }
fun <T> Collection<T?>.allNull() = all { it == null }
fun <T> Collection<T?>.joinToStringIndexed(
    separator: CharSequence = ", ",
    prefix: CharSequence = "",
    postfix: CharSequence = "",
    limit: Int = -1,
    truncated: CharSequence = "...",
    transform: (Int, T?) -> CharSequence = { i, v -> "$i: $v" }
) = mapIndexed { i, v ->
    transform(i, v)
}.joinToString(separator, prefix, postfix, limit, truncated)

fun <K, V> List<Pair<K, V>>.forEach(action: (K, V) -> Unit) = forEach {
    action(it.first, it.second)
}

fun <K, V> Map<K, V>.joinToString(
    separator: CharSequence = ", ",
    prefix: CharSequence = "{ ",
    postfix: CharSequence = " }",
    limit: Int = -1,
    truncated: CharSequence = "...",
    transform: (K, V) -> CharSequence = { k, v -> "$k: $v" }
) = toList().joinToString(separator, prefix, postfix, limit, truncated) {
    transform(it.first, it.second)
}

fun <T> List<T>.limit(fromIndex: Int, toIndex: Int) =
    subList(maxOf(0, minOf(size, fromIndex)), minOf(size, maxOf(fromIndex, toIndex)))

fun <T> List<T>.limitTo(toIndex: Int) = limit(0, toIndex)
fun <T> List<T>.limitFrom(fromIndex: Int) = limit(fromIndex, 0)