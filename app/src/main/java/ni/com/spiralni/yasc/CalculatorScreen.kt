package ni.com.spiralni.yasc

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CalculatorScreen(display: Display, onKeyClick: (key: String) -> Unit) {

    Surface(color = MaterialTheme.colors.background) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight(0.40f)
                    .fillMaxWidth()
                    .background(Color.Black)
                    .border(1.dp, Color.Yellow)
            ) {
                DisplayComponent(display.main, display.secondary)
            }
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black)
                    .border(1.dp, Color.Yellow)
            ) {
                MainPad(onKeyClick)
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RightPad(onKeyClick)
                }

            }
        }
    }
}

fun getFontSize(display: String): TextUnit {
    return when {
        display.length < 7 -> 50.sp
        display.length > 6 -> 45.sp
        display.length > 7 -> 35.sp
        display.length > 8 -> 30.sp
        display.length > 9 -> 25.sp
        else -> 20.sp
    }
}

@Composable
fun DisplayComponent(display: String, secondaryDisplay: String) {
    val (fontSize, setFontSize) = remember { mutableStateOf(50.sp) }

    setFontSize(getFontSize(display))

    val colScroll = rememberScrollState(0)

    Column(
        modifier = Modifier
            .padding(32.dp)
            .verticalScroll(colScroll)
    ) {
        Text(
            text = display,
            fontSize = fontSize,
            style = MaterialTheme.typography.button,
            color = Color.Green,
            textAlign = TextAlign.Right,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = secondaryDisplay,
            fontSize = 20.sp,
            textAlign = TextAlign.Right,
            style = MaterialTheme.typography.button,
            color = Color.Magenta,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        )
    }
}

@Composable
fun MainPad(onKeyTap: (key: String) -> Unit) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth(0.75f)
            .fillMaxHeight()
            .background(Color.Black)
            .border(1.dp, Color.Yellow)
    ) {
        TileRow(onKeyTap, modifier = Modifier.weight(1.0f), "7", "8", "9")
        TileRow(onKeyTap, modifier = Modifier.weight(1.0f), "4", "5", "6")
        TileRow(onKeyTap, modifier = Modifier.weight(1.0f), "1", "2", "3")
        TileRow(onKeyTap, modifier = Modifier.weight(1.0f), "0", ".", "=")
    }
}

@Composable
fun RightPad(onKeyClick: (key: String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxHeight(0.95f),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        listOf("C", "/", "x", "-", "+").forEach {
            TileRow(onTap = onKeyClick, modifier = Modifier.weight(1.0f), it)
        }
    }
}

@Composable
fun TileRow(
    onTap: (key: String) -> Unit,
    modifier: Modifier,
    vararg values: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {
        values.forEach {
            Tile(it, onTap, modifier = Modifier.weight(1.0f))
        }
    }
}

@Composable
fun Tile(label: String,
         onTap: (key: String) -> Unit,
         modifier: Modifier) {
    val composableScope = rememberCoroutineScope()

    var bgColor by remember {
        mutableStateOf(Color.Transparent)
    }

    Box(
        modifier = modifier
            .background(bgColor)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        composableScope.launch {
                            bgColor = Color.Green.copy(
                                alpha = 0.3f
                            )
                            onTap(label)
                            delay(150)
                            bgColor = Color.Transparent
                        }
                    },
                    onLongPress = {
                        if ("C" == label) {
                            composableScope.launch {
                                bgColor = Color.Yellow.copy(
                                    alpha = 0.3f
                                )
                                onTap("CC")
                                delay(150)
                                bgColor = Color.Transparent
                            }
                        }
                    }
                )
            }
            .fillMaxSize()
    ) {
        Text(
            text = label,
            fontSize = 35.sp,
            style = MaterialTheme.typography.button,
            color = Color.Green,
            modifier = Modifier
                .align(Alignment.Center)
                .matchParentSize()
        )
    }
}
