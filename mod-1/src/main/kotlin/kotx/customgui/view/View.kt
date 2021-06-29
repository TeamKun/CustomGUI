package kotx.customgui.view

import com.mojang.blaze3d.matrix.*
import kotlin.math.*

abstract class View(
    var x1: Int,
    var y1: Int,
    var x2: Int,
    var y2: Int
) {
    val width: Int
        get() = max(x1, x2) - min(x1, x2)

    val height: Int
        get() = max(y1, y2) - min(y1, y2)

    abstract fun drawPreview(stack: MatrixStack)
    abstract fun drawFull(stack: MatrixStack)

    open fun handleClick() {}
}