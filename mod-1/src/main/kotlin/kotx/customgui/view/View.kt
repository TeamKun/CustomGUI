package kotx.customgui.view

import kotlin.math.*

interface View {
    var x1: Int
    var y1: Int
    var x2: Int
    var y2: Int

    val renderer: ViewRenderer<out View>

    fun onClick() {

    }

    val width: Int
        get() = max(x1, x2) - min(x1, x2)

    val height: Int
        get() = max(y1, y2) - min(y1, y2)
}