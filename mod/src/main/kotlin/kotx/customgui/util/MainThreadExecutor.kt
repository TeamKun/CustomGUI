package kotx.customgui.util

import java.util.concurrent.ConcurrentLinkedQueue

object MainThreadExecutor {
    private val tasks = ConcurrentLinkedQueue<Runnable>()

    fun offer(runnable: Runnable) {
        tasks.offer(runnable)
    }

    fun consume() {
        tasks.poll()?.run()
    }
}