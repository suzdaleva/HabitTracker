package com.manicpixie.habittracker

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.manicpixie.habittracker.ui.theme.HabitTrackerTheme
import com.manicpixie.habittracker.ui.theme.PrimaryBlack
import com.manicpixie.habittracker.ui.theme.White
import com.manicpixie.habittracker.util.dpToSp
import kotlinx.coroutines.delay


class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity = this
        setContent {
            HabitTrackerTheme {
                LaunchedEffect(true) {
                    delay(1000)
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                Surface(color = White) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.app_title),
                            style = MaterialTheme.typography.caption.copy(
                                letterSpacing = 0.04.em,
                                color = PrimaryBlack,
                                fontSize = dpToSp(dp = 40.dp)
                            )
                        )
                    }
                }
            }
        }
    }
}