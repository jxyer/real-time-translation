package com.jxy.realTimeTranslation.service

import android.content.Context
import android.hardware.display.DisplayManager
import android.os.Build
import android.view.Display
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner

@RequiresApi(Build.VERSION_CODES.R)
abstract class ViewReadyService : LifecycleService(), SavedStateRegistryOwner, ViewModelStoreOwner {

    private val savedStateRegistryController: SavedStateRegistryController by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        SavedStateRegistryController.create(this)
    }

    /**
     * Build our view model store
     */
    private val internalViewModelStore: ViewModelStore by lazy {
        ViewModelStore()
    }

    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    override val viewModelStore: ViewModelStore
        get() = internalViewModelStore

    /**
     * Context dedicated to the view
     */
    internal val overlayContext: Context by lazy {
        // Get the default display
        val defaultDisplay: Display = getSystemService(DisplayManager::class.java).getDisplay(Display.DEFAULT_DISPLAY)
        // Create a display context, and then the window context
        createDisplayContext(defaultDisplay)
            .createWindowContext(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, null)
    }

    override fun onCreate() {
        super.onCreate()
        savedStateRegistryController.performRestore(null)
        println("创建")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("销毁")
    }


}