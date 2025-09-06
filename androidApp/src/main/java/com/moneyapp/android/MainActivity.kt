package com.moneyapp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.moneyapp.shared.App
import com.moneyapp.shared.cache.Database
import com.moneyapp.shared.cache.DatabaseDriverFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = Database(DatabaseDriverFactory(this))
        setContent {
            App(database)
        }
    }
}
