package at.crowdware.nocode.texteditor.syntax

import androidx.compose.ui.graphics.Color

/**
 * Erweiterte Farbpalette für Syntax-Highlighting
 */
data class ExtendedColors(
    val syntaxColor: Color = Color.White,
    val bracketColor: Color = Color.Yellow,  // Gelb für geschweifte Klammern
    val attributeValueColor: Color = Color.Green,
    val attributeNameColor: Color = Color.Yellow,
    val mdHeader: Color = Color.Magenta,
    val defaultTextColor: Color = Color.White,
    val linkColor: Color = Color.Blue,
    val elementColor: Color = Color.Blue,    // Blau für SML-Elemente wie Rectangle, Text, Button
    val stringColor: Color = Color(0xFFBB8844),  // Braun für Strings
    val numberColor: Color = Color(0xFF66CCFF)   // Hellblau für Zahlen
)
