package kotx.customgui.util

import java.util.concurrent.*

object MainThreadExecutor {
    private val tasks = ConcurrentLinkedQueue<Runnable>()

    fun offer(runnable: Runnable) {
        tasks.offer(runnable)
    }

    fun consume() {
        tasks.poll()?.run()
    }
}