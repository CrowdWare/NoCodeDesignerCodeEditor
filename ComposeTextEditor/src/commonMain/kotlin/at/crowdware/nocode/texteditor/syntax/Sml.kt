package at.crowdware.nocode.texteditor.syntax

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle

class SmlSyntaxHighlighter(val colors: ExtendedColors) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // Erstelle einen völlig neuen AnnotatedString mit den Hervorhebungen
        val highlighted = buildAnnotatedString {
            var currentIndex = 0
            
            // Durchlaufe den Text Zeichen für Zeichen
            while (currentIndex < text.length) {
                val remainingText = text.text.substring(currentIndex)
                
                // Prüfe auf geschweifte Klammern
                if (remainingText.startsWith("{") || remainingText.startsWith("}")) {
                    withStyle(SpanStyle(color = colors.bracketColor)) {
                        append(remainingText[0])
                    }
                    currentIndex++
                    continue
                }
                
                // Prüfe auf Doppelpunkte
                if (remainingText.startsWith(":")) {
                    withStyle(SpanStyle(color = colors.bracketColor)) {
                        append(remainingText[0])
                    }
                    currentIndex++
                    continue
                }
                
                // Prüfe auf Elementnamen (gefolgt von Leerzeichen und geschweifter Klammer)
                val elementMatch = Regex("^(\\w+)\\s*\\{").find(remainingText)
                if (elementMatch != null) {
                    val elementName = elementMatch.groups[1]?.value ?: ""
                    withStyle(SpanStyle(color = colors.elementColor)) {
                        append(elementName)
                    }
                    currentIndex += elementName.length
                    continue
                }
                
                // Prüfe auf Strings
                val stringMatch = Regex("^\"[^\"]*\"").find(remainingText)
                if (stringMatch != null) {
                    withStyle(SpanStyle(color = colors.stringColor)) {
                        append(stringMatch.value)
                    }
                    currentIndex += stringMatch.value.length
                    continue
                }
                
                // Prüfe auf Zahlen
                val numberMatch = Regex("^\\b\\d+\\b").find(remainingText)
                if (numberMatch != null) {
                    withStyle(SpanStyle(color = colors.numberColor)) {
                        append(numberMatch.value)
                    }
                    currentIndex += numberMatch.value.length
                    continue
                }
                
                // Prüfe auf Eigenschaften
                val propertyMatch = Regex("^(\\w+)(?=\\s*:)").find(remainingText)
                if (propertyMatch != null) {
                    withStyle(SpanStyle(color = colors.attributeNameColor)) {
                        append(propertyMatch.value)
                    }
                    currentIndex += propertyMatch.value.length
                    continue
                }
                
                // Wenn kein Muster gefunden wurde, füge das aktuelle Zeichen unverändert hinzu
                append(remainingText[0])
                currentIndex++
            }
        }
        
        return TransformedText(highlighted, OffsetMapping.Identity)
    }
}
