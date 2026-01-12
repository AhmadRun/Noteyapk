package com.notey.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.notey.app.data.AppDatabase
import com.notey.app.data.Category
import com.notey.app.data.Note
import com.notey.app.security.AppLockManager
import com.notey.app.security.BiometricHelper
import com.notey.app.settings.SettingsManager
import com.notey.app.ui.screens.HomeScreen
import com.notey.app.ui.screens.LockScreen
import com.notey.app.ui.screens.SettingsScreen
import com.notey.app.ui.theme.NoteyTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : FragmentActivity() {
    
    private lateinit var appLockManager: AppLockManager
    private lateinit var biometricHelper: BiometricHelper
    private lateinit var settingsManager: SettingsManager
    private lateinit var database: AppDatabase
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        appLockManager = AppLockManager(this)
        biometricHelper = BiometricHelper(this)
        settingsManager = SettingsManager(this)
        database = AppDatabase.getDatabase(this)
        
        setContent {
            var currentTheme by remember { mutableStateOf(settingsManager.themeMode) }
            
            NoteyTheme(dynamicColor = false) {
                var isUnlocked by remember { mutableStateOf(false) }
                var showSettings by remember { mutableStateOf(false) }
                var notes by remember { mutableStateOf<List<Note>>(emptyList()) }
                var categories by remember { mutableStateOf<List<Category>>(emptyList()) }
                
                // Collect notes and categories from database
                LaunchedEffect(isUnlocked) {
                    if (isUnlocked) {
                        launch {
                            database.noteDao().getAllNotes().collectLatest {
                                notes = it
                            }
                        }
                        launch {
                            database.categoryDao().getAllCategories().collectLatest {
                                categories = it
                            }
                        }
                    }
                }
                
                when {
                    !isUnlocked -> {
                        LockScreen(
                            isSetup = !appLockManager.isPinSet(),
                            isBiometricAvailable = biometricHelper.isBiometricAvailable(),
                            onUnlock = { pin ->
                                val success = appLockManager.verifyPin(pin)
                                if (success) isUnlocked = true
                                success
                            },
                            onSetPin = { pin ->
                                appLockManager.setPin(pin)
                                isUnlocked = true
                            },
                            onBiometricClick = {
                                biometricHelper.showBiometricPrompt(
                                    activity = this@MainActivity,
                                    onSuccess = { isUnlocked = true },
                                    onError = { },
                                    onFallback = { }
                                )
                            }
                        )
                    }
                    showSettings -> {
                        SettingsScreen(
                            currentTheme = currentTheme,
                            onThemeChange = { newTheme ->
                                settingsManager.setTheme(newTheme)
                                currentTheme = newTheme
                            },
                            onBack = { showSettings = false }
                        )
                    }
                    else -> {
                        HomeScreen(
                            notes = notes,
                            categories = categories,
                            currentTheme = currentTheme,
                            onAddNote = { title, content, colorIndex, catId ->
                                lifecycleScope.launch {
                                    database.noteDao().insert(
                                        Note(title = title, content = content, color = colorIndex, categoryId = catId)
                                    )
                                }
                            },
                            onDeleteNote = { note ->
                                lifecycleScope.launch {
                                    database.noteDao().delete(note)
                                }
                            },
                            onAddCategory = { name ->
                                lifecycleScope.launch {
                                    database.categoryDao().insert(Category(name = name))
                                }
                            },
                            onDeleteCategory = { category ->
                                lifecycleScope.launch {
                                    database.categoryDao().delete(category)
                                }
                            },
                            onSettingsClick = { showSettings = true }
                        )
                    }
                }
            }
        }
    }
}