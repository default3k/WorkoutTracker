package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                adaptiveUI()
            }
        }
    }
}

/* ------------------------ Общий адаптивный UI ------------------------ */

@Composable
fun adaptiveUI() {

    // ★★★★★ ДОБАВЛЕНО: переменная-счётчик ★★★★★
    var counter by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.height(40.dp))

        Spacer(Modifier.height(40.dp))

        val paddingValues = 5.dp
        val uniqueShape = AbsoluteRoundedCornerShape(5)
        var text by remember { mutableStateOf("") }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                modifier = Modifier
                    .padding(paddingValues)
                    .weight(1f),
                shape = uniqueShape,
                onClick = { }
            ) {
                Text("Кнопка")
            }

            TextField(
                modifier = Modifier
                    .padding(paddingValues)
                    .weight(1f),
                shape = uniqueShape,
                value = text,
                onValueChange = { text = it },
                label = { Text("Окно ввода") }
            )

            // ----------- КНОПКА "-" (ИСПРАВЛЕНО) -----------
            Button(
                modifier = Modifier
                    .width(75.dp)
                    .height(50.dp),
                onClick = { if (counter > 0) counter-- },
                shape = CutCornerShape(0, 45, 0, 45)
            ) {
                Text("-")
            }

            // ----------- ЧИСЛО (ИСПРАВЛЕНО) -----------
            Button(
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp),
                onClick = { },    // оставить пустым, как просил
                border = BorderStroke(5.dp, Color.Black)
            ) {
                Text(counter.toString())   // ★ ТЕПЕРЬ ПОКАЗЫВАЕТ counter ★
            }

            // ----------- КНОПКА "+" (ИСПРАВЛЕНО) -----------
            Button(
                modifier = Modifier
                    .width(75.dp)
                    .height(50.dp),
                onClick = { counter++ },
                shape = CutCornerShape(45, 0, 45, 0)
            ) {
                Text("+")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    MyApplicationTheme {
        adaptiveUI()
    }
}
