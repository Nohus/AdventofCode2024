package day12.visualisation

import androidx.compose.ui.graphics.Color

object Theme {
    val regionColors = listOf(
        Color(0xFFD32F2F),
        Color(0xFFC2185B),
        Color(0xFF7B1FA2),
        Color(0xFF512DA8),
        Color(0xFF303F9F),
        Color(0xFF1976D2),
        Color(0xFF0097A7),
        Color(0xFF00796B),
        Color(0xFF388E3C),
        Color(0xFFE64A19),
        Color(0xFF5D4037),
    )

    fun getRegionColor(regionId: Int): Color {
        return regionColors[regionId % regionColors.size]
    }
}
