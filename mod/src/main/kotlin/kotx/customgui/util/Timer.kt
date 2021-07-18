package kotx.customgui.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.system.measureTimeMillis

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