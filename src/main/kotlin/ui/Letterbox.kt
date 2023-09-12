package ui

import Globals.WORLD_WIDTH
import com.gregory.base.BaseActor

class Letterbox : BaseActor() {

    init {
        width = WORLD_WIDTH
        height = 138f // 2.39:1 aspect ratio
        drawIndex = -1000
    }
}