package kotx.ktools

infix fun Boolean.alsoIfTrue(block: Unit) = alsoIf(true) { block }
infix fun Boolean.alsoIfFalse(block: Unit) = alsoIf(false) { block }
infix fun Boolean.alsoIfTrue(block: () -> Unit) = alsoIf(true, block)
infix fun Boolean.alsoIfFalse(block: () -> Unit) = alsoIf(false, block)
infix fun Boolean.alsoIf(block: () -> Unit) = alsoIf(true, block)
infix fun Boolean.alsoIf(block: Unit) = alsoIf(true) { block }
fun Boolean.alsoIf(condition: Boolean, block: () -> Unit) {
    if (this == condition) block()
}

infix fun <R> Boolean.letIfTrue(block: () -> R) = letIf(true, block)
infix fun <R> Boolean.letIfFalse(block: () -> R) = letIf(false, block)
infix fun <R> Boolean.letIfTrue(block: R) = letIf(true) { block }
infix fun <R> Boolean.letIfFalse(block: R) = letIf(false) { block }
infix fun <R> Boolean.letIf(block: () -> R) = letIf(true, block)
infix fun <R> Boolean.letIf(block: R) = letIf(true) { block }
fun <R> Boolean.letIf(condition: Boolean, block: () -> R) = if (this == condition) block() else null

fun <T> T.println() = println(this)
fun <T> T.alsoPrintln() = also { println(this) }
fun <T> T.printlnRun(block: (T) -> String) = println(block(this))
fun <T> T.println(block: T.() -> String) = println(block())
fun <T> T.print() = print(this)
fun <T> T.alsoPrint() = also { print(this) }
fun <T> T.printRun(block: (T) -> String) = print(block(this))
fun <T> T.print(block: T.() -> String) = print(block())