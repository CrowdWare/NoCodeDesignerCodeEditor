package at.crowdware.nocode.texteditor.syntax

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.graphics.Color


class SmlSyntaxHighlighter(val colors: ExtendedColors) : VisualTransformation {
    // because the code is comming line by line we need to remember if we are in a string
    private var inString = false

    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text
        val builder = AnnotatedString.Builder(raw)
        val length = raw.length
        
        // Reine Kommentarzeilen immer als Kommentar behandeln, unabhängig vom inString-Status
        if (raw.trimStart().startsWith("//")) {
            builder.addStyle(
                SpanStyle(color = colors.commentColor),
                0, length
            )
            // inString-Status bleibt unverändert
            return TransformedText(builder.toAnnotatedString(), OffsetMapping.Identity)
        }
        
        // Wir sind innerhalb eines Strings - volle URL Strings wie http:// besonders behandeln
        if (inString) {
            // Suche nach einem schließenden Anführungszeichen
            val quoteIndex = raw.indexOf('"')
            
            if (quoteIndex >= 0) {
                // Der String geht bis zum schließenden Anführungszeichen
                builder.addStyle(
                    SpanStyle(color = colors.attributeValueColor),
                    0, quoteIndex + 1
                )
                
                // String ist geschlossen
                inString = false
                
                // Falls noch Text nach dem String folgt, verarbeite ihn normal
                if (quoteIndex + 1 < length) {
                    processText(raw.substring(quoteIndex + 1), builder, quoteIndex + 1)
                }
            } else {
                // Kein schließendes Anführungszeichen gefunden, alles ist String
                builder.addStyle(
                    SpanStyle(color = colors.attributeValueColor),
                    0, length
                )
            }
            
            return TransformedText(builder.toAnnotatedString(), OffsetMapping.Identity)
        }
        
        // Normale Verarbeitung für Text außerhalb eines Strings
        processText(raw, builder, 0)
        return TransformedText(builder.toAnnotatedString(), OffsetMapping.Identity)
    }
    
    // Verarbeitet Text außerhalb oder nach einem String
    private fun processText(text: String, builder: AnnotatedString.Builder, offset: Int) {
        val length = text.length
        var i = 0
        
        while (i < length) {
            // Prüfe auf einen Kommentar außerhalb eines Strings
            if (i < length - 1 && text[i] == '/' && text[i + 1] == '/') {
                // Kommentar gefunden
                builder.addStyle(
                    SpanStyle(color = colors.commentColor),
                    i + offset, length + offset
                )
                break
            }
            
            // Prüfe auf einen String
            if (text[i] == '"') {
                val startQuote = i
                i++
                
                // Suche das Ende des Strings
                var foundEndQuote = false
                
                while (i < length) {
                    if (text[i] == '"') {
                        // Schließendes Anführungszeichen gefunden
                        foundEndQuote = true
                        break
                    }
                    i++
                }
                
                if (foundEndQuote) {
                    // Vollständiger String mit schließendem Anführungszeichen
                    builder.addStyle(
                        SpanStyle(color = colors.attributeValueColor),
                        startQuote + offset, i + 1 + offset
                    )
                    i++
                } else {
                    // Unvollständiger String bis zum Ende der Zeile
                    builder.addStyle(
                        SpanStyle(color = colors.attributeValueColor),
                        startQuote + offset, length + offset
                    )
                    inString = true
                    break
                }
            } else if (text[i].isDigit()) {
                // Zahlen hervorheben
                val start = i
                while (i < length && (text[i].isDigit() || text[i] == '.')) i++
                
                builder.addStyle(
                    SpanStyle(color = colors.numberColor),
                    start + offset, i + offset
                )
            } else if (text[i].isLetter()) {
                // Wörter (Elemente oder Properties) hervorheben
                val start = i
                while (i < length && (text[i].isLetterOrDigit() || text[i] == '_')) i++
                
                // Prüfe, ob es ein Property oder Element ist
                val restOfLine = text.substring(i).takeWhile { it != '\n' }
                val colonIndex = restOfLine.indexOf(':')
                val openBraceIndex = restOfLine.indexOf('{')
                
                val isProperty = colonIndex >= 0 && (openBraceIndex < 0 || colonIndex < openBraceIndex)
                val isElement = openBraceIndex >= 0 || isWordAloneOnLine(text, start, i)
                
                if (isProperty) {
                    // Es ist ein Property
                    builder.addStyle(
                        SpanStyle(color = colors.attributeNameColor),
                        start + offset, i + offset
                    )
                } else if (isElement) {
                    // Es ist ein Element
                    builder.addStyle(
                        SpanStyle(color = colors.elementColor),
                        start + offset, i + offset
                    )
                }
            } else if (text[i] == ':' || text[i] == '{' || text[i] == '}') {
                // Klammern und Doppelpunkte hervorheben
                builder.addStyle(
                    SpanStyle(color = colors.bracketColor),
                    i + offset, i + 1 + offset
                )
                i++
            } else {
                i++
            }
        }
    }

    private fun isWordAloneOnLine(text: String, start: Int, end: Int): Boolean {
        val before = text.substring(0, start).takeLastWhile { it != '\n' }.trim()
        val after = text.substring(end).takeWhile { it != '\n' }.trim()
        return before.isEmpty() && after.isEmpty()
    }
}
