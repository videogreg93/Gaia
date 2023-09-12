package com.gregory.managers.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.gregory.managers.InputActionManager
import com.gregory.managers.MegaManagers
import ktx.freetype.registerFreeTypeFontLoaders
import org.reflections.Reflections

class AssetManager : MegaManagers.Manager {
    private val assetManager = AssetManager()
    var currentlyLoadingType = ""

    override fun init() {
        assetManager.registerFreeTypeFontLoaders()
        initialAssetTypesToLoad.forEach {
            loadAssets(it)
        }
        loadInputIconAssets()
    }

    fun update(): Boolean {
        return assetManager.update()
    }

    fun progress(): Float {
        return assetManager.progress
    }

    fun loadAssets(type: Asset.TYPE) {
        val reflection = MegaManagers.currentContext.inject<Reflections>()
        val assets = reflection.getFieldsAnnotatedWith(Asset::class.java)
            .filter {
                it.getAnnotation(Asset::class.java).type == type
            }
            .mapNotNull {
                it.isAccessible = true
                it.get(null) as? AssetDescriptor<*>
            }
        assets.forEach {
            currentlyLoadingType = it.type.simpleName
            assetManager.load(it)
        }
    }

    private fun loadInputIconAssets() {
        listOf(
            InputActionManager.assets.values,
            InputActionManager.assetsPressed.values,
            InputActionManager.controllerAssets.values
        ).flatten().forEach {
            currentlyLoadingType = it.type.simpleName
            assetManager.load(it)
        }
    }

    companion object {
        private val unknownTexture by lazy { Texture("unknown.png") }
        fun <T> AssetDescriptor<T>.get(): T {
            return try {
                MegaManagers.assetManager.assetManager.get(this)
            } catch (e: Exception) {
                ktx.log.error { "Could not load asset ${this.fileName}" }
                e.printStackTrace()
                throw e
            }
        }

        val initialAssetTypesToLoad = listOf(
            Asset.TYPE.MAIN_MENU,
            Asset.TYPE.OPTIONS_MENU,
            Asset.TYPE.MAP,
        )
    }

}

@Target(AnnotationTarget.FIELD)
annotation class Asset(val type: TYPE = TYPE.GENERIC) {
    enum class TYPE {
        MAIN_MENU,
        OPTIONS_MENU,
        GENERIC,
        MAP,
        BATTLE,
        BATTLE_COMMON,
        SHOP
    }
}