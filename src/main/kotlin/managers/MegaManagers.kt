package com.gregory.managers

import com.gregory.managers.assets.AssetManager
import com.gregory.managers.context.MainContext
import com.gregory.managers.fonts.FontManager
import com.gregory.managers.prefs.Prefs
import ktx.inject.Context

object MegaManagers {
    val randomManager: RandomManager = RandomManager()
    val assetManager = AssetManager()
    val inputActionManager = InputActionManager()
    val screenManager = ScreenManager()
    val modalManager = ModalManager()
    val fontManager = FontManager()
    val soundManager = SoundManager()
    val prefs by lazy { Prefs }
    val eventManager = EventManager()
    val textBoy = TextBoy()
    lateinit var currentContext: Context

    fun init(args: Array<String>) {
            MainContext.register()
            currentContext = MainContext.context
            prefs.init()
            assetManager.init()
            inputActionManager.init()
            fontManager.init()
            soundManager.init()
            textBoy.init()

    }

    fun dispose() {
        soundManager.dispose()
    }

    interface Manager {
        fun init()
    }
}