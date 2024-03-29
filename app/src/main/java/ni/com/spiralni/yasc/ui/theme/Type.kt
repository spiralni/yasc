package ni.com.spiralni.yasc.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ni.com.spiralni.yasc.R

val PixelMix = FontFamily(
    Font(R.font.pixelmix)
)

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    button = TextStyle(
        fontFamily = PixelMix,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    )
)