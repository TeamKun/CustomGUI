package kotx.customgui

import java.util.ArrayList

import java.util.concurrent.ConcurrentLinkedDeque

import java.util.Deque

import com.google.common.util.concurrent.AbstractListeningExecutorService
import java.util.concurrent.TimeUnit


object ServerThreadExecutor : AbstractListeningExecutorService() {
    private val queue: Deque<Runnable> = ConcurrentLinkedDeque()

    var serverThread: Thread? = null
    override fun execute(runnable: Runnable) {
        queue.offer(runnable)
    }

    fun executeQueuedTaskImmediately() {
        if (serverThread == null) serverThread = Thread.currentThread()

        var run: Runnable? = null

        while (queue.poll()?.also { run = it } != null) {
            run?.run()
        }
    }

    override fun shutdown() {}
    override fun shutdownNow(): List<Runnable> {
        return ArrayList()
    }

    override fun isShutdown(): Boolean {
        return false
    }

    override fun isTerminated(): Boolean {
        return false
    }

    @Throws(InterruptedException::class)
    override fun awaitTermination(timeout: Long, unit: TimeUnit?): Boolean {
        return false
    }
}