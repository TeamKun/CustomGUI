package kotx.customgui.util

import kotlinx.coroutines.*
import kotlin.coroutines.*
import kotlin.system.*

fun CoroutineScope.timer(
    interval: Long,
    fixedRate: Boolean = true,
    context: CoroutineContext = EmptyCoroutineContext,
    action: suspend TimerScope.() -> Unit
) = launch(context) {
    val scope = TimerScope()

    while (isActive) {
        val time = measureTimeMillis { action(scope) }

        if (scope.isCanceled) break

        if (fixedRate) delay(maxOf(0, interval - time)) else delay(interval)
    }
}


class TimerScope {
    var isCanceled: Boolean = false
        private set

    fun cancel() {
        isCanceled = true
    }
}