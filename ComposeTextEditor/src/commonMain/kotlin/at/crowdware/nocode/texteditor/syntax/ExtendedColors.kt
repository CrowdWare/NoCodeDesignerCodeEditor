package at.crowdware.nocode.texteditor.syntax

import androidx.compose.ui.graphics.Color

/**
 * Erweiterte Farbpalette für Syntax-Highlighting
 */
data class ExtendedColors(
    val syntaxColor: Color = Color(0xFF61BEA6),
    val bracketColor: Color = Color(0xFFF5D52E),  // Gelb für geschweifte Klammern
    val attributeValueColor: Color = Color(0xFFBE896F),
    val attributeNameColor: Color = Color(0xFFA0D4FC),
    val mdHeader: Color = Color(0xFFB774B1),
    val defaultTextColor: Color = Color(0xFFB0B0B0),
    val linkColor: Color = Color(0xFF5C7AF1),
    val elementColor: Color = Color(0xFF66CCFF),    // Blau für SML-Elemente wie Rectangle, Text, Button
    val stringColor: Color = Color(0xFFBB8844),  // Braun für Strings
    val numberColor: Color = Color(0xFF66CCFF)   // Hellblau für Zahlen
)
