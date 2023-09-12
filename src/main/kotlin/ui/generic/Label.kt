package ui.generic

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.gregory.base.BaseActor
import com.odencave.i18n.Nls
import ktx.actors.alpha
import ui.utils.centerHorizontallyOn
import ui.utils.centerVerticallyOn
import utils.getHeight
import utils.getWidth
import utils.text
import utils.wrapped

open class Label(var text: String, val font: BitmapFont, x: Float = 0f, y: Float = 0f) : BaseActor(null, x, y) {

    constructor(nls: Nls, font: BitmapFont, x: Float = 0f, y: Float = 0f) : this(nls.text, font, x, y)

    var centerHorizontalCallback: (() -> Float)? = null
    var centerVerticallyCallback: (() -> Float)? = null

    init {
        drawIndex = 10
    }

    override fun act(delta: Float) {
        super.act(delta)
        centerHorizontalCallback?.let {
            centerHorizontallyOn(it.invoke())
        }
        centerVerticallyCallback?.let {
            centerVerticallyOn(it.invoke())
        }
        name = text
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        if (alpha == 1f && shouldDraw) {
            font.draw(batch, text, x, y)
        }
    }

    override fun getWidth(): Float {
        return font.getWidth(text)
    }

    override fun getHeight(): Float {
        return font.getHeight(text)
    }

    override fun center() {
        val x = -width / 2
        val y = height / 2
        setPosition(x, y)
    }

    fun wrap(width: Int): Label {
        text = text.wrapped(font, width)
        return this
    }

    fun centerHorizontallyOn(other: BaseActor) {
        centerHorizontalCallback = { other.x + other.width / 2 }
    }

    fun centerVerticallyOn(other: BaseActor) {
        centerVerticallyCallback = { other.y + other.height / 2 }
    }

    fun keepCenteredOn(other: BaseActor) {
        centerHorizontallyOn(other)
        centerVerticallyCallback = { other.y + other.height / 2 + 20 }
    }

    // Placement functions

    override fun centerOn(other: BaseActor) {
        val newX = other.x + (other.width - width) / 2
        val newY = other.y + other.height / 2 + height / 2
        setPosition(newX, newY)
    }
}