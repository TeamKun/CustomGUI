package kotx.customgui.view

import kotlin.math.*

interface View {
    val x1: Int
    val y1: Int
    val x2: Int
    val y2: Int

    val renderer: ViewRenderer<out View>

    val width: Int
        get() = max(x1, x2) - min(x1, x2)

    val height: Int
        get() = max(y1, y2) - min(y1, y2)
}