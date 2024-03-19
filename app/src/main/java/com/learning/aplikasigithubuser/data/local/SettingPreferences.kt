package com.learning.aplikasigithubuser.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.prefDatastore by preferencesDataStore("settings")
class SettingPreferences constructor(context: Context){

    private val settingDatastore = context.prefDatastore
    private val themeKEY = booleanPreferencesKey("theme_setting")

    fun getThemeSetting(): Flow<Boolean> =
        settingDatastore.data.map { preferences ->
            preferences[themeKEY]?:false
        }
    suspend fun saveThemeSetting(isDarkModeActive: Boolean){
        settingDatastore.edit {
            preferences -> preferences[themeKEY]=isDarkModeActive
        }
    }
}