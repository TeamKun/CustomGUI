package kotx.ktools

val Long.millis: TimeValue get() = TimeValue(this)
val Long.seconds: TimeValue get() = times(1000).millis
val Long.minutes: TimeValue get() = times(60).seconds
val Long.hours: TimeValue get() = times(60).minutes
val Long.days: TimeValue get() = times(24).hours

val Int.millis: TimeValue get() = toLong().millis
val Int.seconds: TimeValue get() = toLong().seconds
val Int.minutes: TimeValue get() = toLong().minutes
val Int.hours: TimeValue get() = toLong().hours
val Int.days: TimeValue get() = toLong().days

val Long.milli: TimeValue get() = millis
val Long.second: TimeValue get() = seconds
val Long.minute: TimeValue get() = minutes
val Long.hour: TimeValue get() = hours
val Long.day: TimeValue get() = days

val Int.milli: TimeValue get() = millis
val Int.second: TimeValue get() = seconds
val Int.minute: TimeValue get() = minutes
val Int.hour: TimeValue get() = hours
val Int.day: TimeValue get() = days

val Long.ms: TimeValue get() = millis
val Long.s: TimeValue get() = seconds
val Long.m: TimeValue get() = minutes
val Long.h: TimeValue get() = hours
val Long.d: TimeValue get() = days

val Int.ms: TimeValue get() = millis
val Int.s: TimeValue get() = seconds
val Int.m: TimeValue get() = minutes
val Int.h: TimeValue get() = hours
val Int.d: TimeValue get() = days

data class TimeValue internal constructor(val toMillis: Long) {
    val toSeconds = toMillis / 1000
    val toMinutes = toSeconds / 60
    val toHours = toMinutes / 60
    val toDays = toHours / 24

    val millis = toMillis
    val seconds = toSeconds
    val minutes = toMinutes
    val hours = toHours
    val days = toDays

    val milli = toMillis
    val second = toSeconds
    val minute = toMinutes
    val hour = toHours
    val day = toDays

    val ms = toMillis
    val s = toSeconds
    val m = toMinutes
    val h = toHours
    val d = toDays

    operator fun plus(other: TimeValue) = TimeValue(toMillis + other.toMillis)
    operator fun minus(other: TimeValue) = TimeValue(toMillis - other.toMillis)
    operator fun times(multiply: Long) = TimeValue(toMillis * multiply)
    operator fun div(div: Long) = TimeValue(toMillis / div)
    operator fun div(div: TimeValue) = TimeValue(toMillis / div.toMillis)
}