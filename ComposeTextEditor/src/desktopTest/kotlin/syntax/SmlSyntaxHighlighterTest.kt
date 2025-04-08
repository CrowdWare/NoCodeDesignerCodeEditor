package syntax

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import at.crowdware.nocode.texteditor.syntax.ExtendedColors
import at.crowdware.nocode.texteditor.syntax.SmlSyntaxHighlighter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SmlSyntaxHighlighterTest {
    
    // Farbschema für Tests definieren
    private val testColors = ExtendedColors(
        bracketColor = Color.Yellow,
        attributeValueColor = Color.Red,
        attributeNameColor = Color.Blue,
        elementColor = Color.Green,
        numberColor = Color.Cyan,
        commentColor = Color.Gray
    )
    
    private val highlighter = SmlSyntaxHighlighter(testColors)
    
    @Test
    fun `Basic syntax highlighting test`() {
        val smlText = """
            Rectangle {
                width: 100
                height: 50
                color: "blue"
                Button {
                    text: "Click me"
                }
            }
        """.trimIndent()
        
        val result = highlighter.filter(AnnotatedString(smlText))
        val annotatedString = result.text
        
        // Überprüfen des Texts
        assertEquals(smlText, annotatedString.text)
        
        // Anzahl der Style-Spans prüfen
        assertTrue(annotatedString.spanStyles.isNotEmpty())
        
        // Prüfen, ob "Rectangle" und "Button" als Elemente hervorgehoben sind (grün)
        val rectangleIndex = smlText.indexOf("Rectangle")
        val buttonIndex = smlText.indexOf("Button")
        
        val hasRectangleElementStyle = annotatedString.spanStyles.any { spanStyle ->
            spanStyle.start == rectangleIndex && 
            spanStyle.end == rectangleIndex + "Rectangle".length && 
            spanStyle.item.color == testColors.elementColor
        }
        
        val hasButtonElementStyle = annotatedString.spanStyles.any { spanStyle ->
            spanStyle.start == buttonIndex && 
            spanStyle.end == buttonIndex + "Button".length && 
            spanStyle.item.color == testColors.elementColor
        }
        
        assertTrue(hasRectangleElementStyle, "Rectangle sollte als Element hervorgehoben sein")
        assertTrue(hasButtonElementStyle, "Button sollte als Element hervorgehoben sein")
        
        // Prüfen, ob Properties korrekt hervorgehoben sind (blau)
        val propertyNames = listOf("width", "height", "color", "text")
        
        propertyNames.forEach { property ->
            val propertyIndex = smlText.indexOf(property)
            val hasPropertyStyle = annotatedString.spanStyles.any { spanStyle ->
                spanStyle.start == propertyIndex && 
                spanStyle.end == propertyIndex + property.length && 
                spanStyle.item.color == testColors.attributeNameColor
            }
            
            assertTrue(hasPropertyStyle, "$property sollte als Property hervorgehoben sein")
        }
        
        // Prüfen, ob Zahlen korrekt hervorgehoben sind (cyan)
        val numberStrings = listOf("100", "50")
        
        numberStrings.forEach { number ->
            val numberIndex = smlText.indexOf(number)
            val hasNumberStyle = annotatedString.spanStyles.any { spanStyle ->
                spanStyle.start == numberIndex && 
                spanStyle.end == numberIndex + number.length && 
                spanStyle.item.color == testColors.numberColor
            }
            
            assertTrue(hasNumberStyle, "$number sollte als Zahl hervorgehoben sein")
        }
        
        // Prüfen, ob Strings korrekt hervorgehoben sind (rot)
        val stringValues = listOf("\"blue\"", "\"Click me\"")
        
        stringValues.forEach { str ->
            val strIndex = smlText.indexOf(str)
            val hasStringStyle = annotatedString.spanStyles.any { spanStyle ->
                spanStyle.start == strIndex && 
                spanStyle.end == strIndex + str.length && 
                spanStyle.item.color == testColors.attributeValueColor
            }
            
            assertTrue(hasStringStyle, "$str sollte als String hervorgehoben sein")
        }
        
        // Prüfen, ob Klammern korrekt hervorgehoben sind (gelb)
        val bracketChars = listOf("{", "}", ":")
        
        bracketChars.forEach { bracket ->
            // Prüfe die erste Instanz des Brackets
            val bracketIndex = smlText.indexOf(bracket)
            val hasBracketStyle = annotatedString.spanStyles.any { spanStyle ->
                spanStyle.start == bracketIndex && 
                spanStyle.end == bracketIndex + 1 && 
                spanStyle.item.color == testColors.bracketColor
            }
            
            assertTrue(hasBracketStyle, "$bracket sollte als Klammer hervorgehoben sein")
        }
    }
    
    @Test
    fun `Test for incomplete string highlighting`() {
        // Test für unvollständigen String (nur öffnendes Anführungszeichen)
        val smlText = "property: \"unfinished string"
        
        val result = highlighter.filter(AnnotatedString(smlText))
        val annotatedString = result.text
        
        // Überprüfen, ob der gesamte unvollständige String hervorgehoben ist
        val stringStart = smlText.indexOf("\"")
        val hasStringStyle = annotatedString.spanStyles.any { spanStyle ->
            spanStyle.start == stringStart && 
            spanStyle.end == smlText.length && 
            spanStyle.item.color == testColors.attributeValueColor
        }
        
        assertTrue(hasStringStyle, "Unvollständiger String sollte hervorgehoben sein")
        
        // Zustandsprüfung: Der Highlighter sollte im inString-Zustand sein
        // Wir können dies testen, indem wir einen weiteren Text durchlaufen lassen
        val secondText = "continuation of text"
        val secondResult = highlighter.filter(AnnotatedString(secondText))
        
        // Der gesamte zweite Text sollte als String hervorgehoben sein
        val hasSecondStringStyle = secondResult.text.spanStyles.any { spanStyle ->
            spanStyle.start == 0 && 
            spanStyle.end == secondText.length && 
            spanStyle.item.color == testColors.attributeValueColor
        }
        
        assertTrue(hasSecondStringStyle, "Fortsetzung des unvollständigen Strings sollte hervorgehoben sein")
    }
    
    @Test
    fun `Test for decimal numbers`() {
        val smlText = "property: 123.456"
        
        val result = highlighter.filter(AnnotatedString(smlText))
        val annotatedString = result.text
        
        // Überprüfen, ob die Dezimalzahl hervorgehoben ist
        val numberIndex = smlText.indexOf("123.456")
        val hasNumberStyle = annotatedString.spanStyles.any { spanStyle ->
            spanStyle.start == numberIndex && 
            spanStyle.end == numberIndex + "123.456".length && 
            spanStyle.item.color == testColors.numberColor
        }
        
        assertTrue(hasNumberStyle, "Dezimalzahl sollte hervorgehoben sein")
    }
    
    @Test
    fun `Test for property with spaces before colon`() {
        val smlText = "property   : value"
        
        val result = highlighter.filter(AnnotatedString(smlText))
        val annotatedString = result.text
        
        // Überprüfen, ob die Property hervorgehoben ist
        val propertyIndex = smlText.indexOf("property")
        val hasPropertyStyle = annotatedString.spanStyles.any { spanStyle ->
            spanStyle.start == propertyIndex && 
            spanStyle.end == propertyIndex + "property".length && 
            spanStyle.item.color == testColors.attributeNameColor
        }
        
        assertTrue(hasPropertyStyle, "Property mit Leerzeichen vor dem Doppelpunkt sollte hervorgehoben sein")
    }
    
    @Test
    fun `Test for word alone on line`() {
        // Ein Element allein auf einer Zeile sollte als Element hervorgehoben werden
        val smlText = """
            Rectangle
        """.trimIndent()
        
        val result = highlighter.filter(AnnotatedString(smlText))
        val annotatedString = result.text
        
        // Überprüfen, ob "Rectangle" als Element hervorgehoben ist
        val elementIndex = smlText.indexOf("Rectangle")
        val hasElementStyle = annotatedString.spanStyles.any { spanStyle ->
            spanStyle.start == elementIndex && 
            spanStyle.end == elementIndex + "Rectangle".length && 
            spanStyle.item.color == testColors.elementColor
        }
        
        assertTrue(hasElementStyle, "Element allein auf einer Zeile sollte hervorgehoben sein")
    }
    
    @Test
    fun `Test for isWordAloneOnLine method`() {
        // Wort nicht allein auf einer Zeile
        val smlText = "Some Rectangle on a line"
        
        val result = highlighter.filter(AnnotatedString(smlText))
        val annotatedString = result.text
        
        // "Rectangle" sollte nicht als Element hervorgehoben sein, da es nicht allein auf der Zeile steht
        val rectangleIndex = smlText.indexOf("Rectangle")
        val hasNoElementStyle = annotatedString.spanStyles.none { spanStyle ->
            spanStyle.start == rectangleIndex && 
            spanStyle.end == rectangleIndex + "Rectangle".length && 
            spanStyle.item.color == testColors.elementColor
        }
        
        assertTrue(hasNoElementStyle, "Wort nicht allein auf einer Zeile sollte nicht als Element hervorgehoben sein")
    }
    
    @Test
    fun `Test for state preservation between lines`() {
        // Teste, dass der inString-Zustand korrekt zwischen Aufrufen erhalten bleibt
        
        // Erster Text mit unvollständigem String
        val firstLine = "property: \"this string continues"
        highlighter.filter(AnnotatedString(firstLine))
        
        // Zweiter Text, der den String fortsetzt und abschließt
        val secondLine = "on the next line\""
        val result = highlighter.filter(AnnotatedString(secondLine))
        val annotatedString = result.text
        
        // Der gesamte zweite Text sollte als Teil des Strings hervorgehoben sein
        val hasStringStyle = annotatedString.spanStyles.any { spanStyle ->
            spanStyle.start == 0 && 
            spanStyle.end == "on the next line\"".length && 
            spanStyle.item.color == testColors.attributeValueColor
        }
        
        assertTrue(hasStringStyle, "Fortsetzung eines Strings über mehrere Zeilen sollte korrekt hervorgehoben sein")
        
        // Teste, dass der inString-Zustand nach Schließen des Anführungszeichens zurückgesetzt wird
        val thirdLine = "another: property"
        val thirdResult = highlighter.filter(AnnotatedString(thirdLine))
        
        // "another" sollte als Property hervorgehoben sein, nicht als String
        val propertyIndex = thirdLine.indexOf("another")
        val hasPropertyStyle = thirdResult.text.spanStyles.any { spanStyle ->
            spanStyle.start == propertyIndex && 
            spanStyle.end == propertyIndex + "another".length && 
            spanStyle.item.color == testColors.attributeNameColor
        }
        
        assertTrue(hasPropertyStyle, "Nach Abschluss eines Strings sollte der inString-Zustand zurückgesetzt sein")
    }
    
    // Neue Tests für Kommentare
    
    @Test
    fun `Test basic comment highlighting`() {
        // Einfacher Kommentar
        val smlText = "property: value // Dies ist ein Kommentar"
        
        val result = highlighter.filter(AnnotatedString(smlText))
        val annotatedString = result.text
        
        // Überprüfe, ob der Kommentar grau ist
        val commentStart = smlText.indexOf("//")
        val hasCommentStyle = annotatedString.spanStyles.any { spanStyle ->
            spanStyle.start == commentStart && 
            spanStyle.end == smlText.length && 
            spanStyle.item.color == testColors.commentColor
        }
        
        assertTrue(hasCommentStyle, "Kommentar sollte grau hervorgehoben sein")
        
        // Überprüfe, ob "property" trotzdem als Property erkannt wird
        val propertyIndex = smlText.indexOf("property")
        val hasPropertyStyle = annotatedString.spanStyles.any { spanStyle ->
            spanStyle.start == propertyIndex && 
            spanStyle.end == propertyIndex + "property".length && 
            spanStyle.item.color == testColors.attributeNameColor
        }
        
        assertTrue(hasPropertyStyle, "Property vor dem Kommentar sollte korrekt hervorgehoben sein")
    }
    
    @Test
    fun `Test comment with quote inside`() {
        // Text mit einem Kommentar, der ein Anführungszeichen enthält
        val smlText = "text: \"Hello\" // Kommentar mit \" drin"
        
        val result = highlighter.filter(AnnotatedString(smlText))
        val annotatedString = result.text
        
        // Der String sollte korrekt erkannt werden
        val stringStart = smlText.indexOf("\"")
        val stringEnd = smlText.indexOf("\"", stringStart + 1) + 1
        val hasStringStyle = annotatedString.spanStyles.any { spanStyle ->
            spanStyle.start == stringStart && 
            spanStyle.end == stringEnd && 
            spanStyle.item.color == testColors.attributeValueColor
        }
        
        assertTrue(hasStringStyle, "String vor dem Kommentar sollte korrekt hervorgehoben sein")
        
        // Der Kommentar inklusive des Anführungszeichens sollte grau sein
        val commentStart = smlText.indexOf("//")
        val hasCommentStyle = annotatedString.spanStyles.any { spanStyle ->
            spanStyle.start == commentStart && 
            spanStyle.end == smlText.length && 
            spanStyle.item.color == testColors.commentColor
        }
        
        assertTrue(hasCommentStyle, "Kommentar mit Anführungszeichen sollte grau hervorgehoben sein")
        
        // Wichtig: inString sollte false sein, da der String vor dem Kommentar abgeschlossen wurde
        val nextLine = "normal text"
        val nextResult = highlighter.filter(AnnotatedString(nextLine))
        
        // Der Text sollte nicht als Teil eines Strings markiert sein
        val hasNoStringStyle = nextResult.text.spanStyles.none { spanStyle ->
            spanStyle.item.color == testColors.attributeValueColor
        }
        
        assertTrue(hasNoStringStyle, "Nächste Zeile sollte nicht als String markiert sein")
    }
    
    @Test
    fun `Test unfinished string followed by comment line`() {
        // Unvollständiger String
        val firstLine = "text: \"unfinished string"
        highlighter.filter(AnnotatedString(firstLine))
        
        // Zweite Zeile ist ein Kommentar
        val secondLine = "// Dies ist ein Kommentar"
        val result = highlighter.filter(AnnotatedString(secondLine))
        
        // Der Kommentar sollte als Kommentar markiert sein
        val hasCommentStyle = result.text.spanStyles.any { spanStyle ->
            spanStyle.start == 0 && 
            spanStyle.end == secondLine.length && 
            spanStyle.item.color == testColors.commentColor
        }
        
        assertTrue(hasCommentStyle, "Kommentarzeile sollte als Kommentar markiert sein")
        
        // Dritte Zeile sollte als Teil des unvollständigen Strings markiert sein
        val thirdLine = "continuation of string\""
        val thirdResult = highlighter.filter(AnnotatedString(thirdLine))
        
        val hasStringStyle = thirdResult.text.spanStyles.any { spanStyle ->
            spanStyle.start == 0 && 
            spanStyle.item.color == testColors.attributeValueColor
        }
        
        assertTrue(hasStringStyle, "Text nach Kommentarzeile sollte als Teil des unvollständigen Strings markiert sein")
    }
    
    @Test
    fun `Test comment-only line`() {
        // Eine Zeile, die nur aus einem Kommentar besteht
        val smlText = "// Nur ein Kommentar"
        
        val result = highlighter.filter(AnnotatedString(smlText))
        val annotatedString = result.text
        
        // Die gesamte Zeile sollte als Kommentar markiert sein
        val hasCommentStyle = annotatedString.spanStyles.any { spanStyle ->
            spanStyle.start == 0 && 
            spanStyle.end == smlText.length && 
            spanStyle.item.color == testColors.commentColor
        }
        
        assertTrue(hasCommentStyle, "Reine Kommentarzeile sollte komplett grau sein")
    }
    
    @Test
    fun `Test double slash in string not treated as comment`() {
        // Ein String, der "//" enthält, sollte nicht als Kommentar behandelt werden
        val smlText = "url: \"http://example.com\""
        
        val result = highlighter.filter(AnnotatedString(smlText))
        val annotatedString = result.text
        
        // Der gesamte String sollte als String markiert sein
        val stringStart = smlText.indexOf("\"")
        val stringEnd = smlText.lastIndexOf("\"") + 1
        val hasStringStyle = annotatedString.spanStyles.any { spanStyle ->
            spanStyle.start == stringStart && 
            spanStyle.end == stringEnd && 
            spanStyle.item.color == testColors.attributeValueColor
        }
        
        assertTrue(hasStringStyle, "String mit // darin sollte vollständig als String markiert sein")
        
        // Es sollte keine Kommentarhervorhebung geben
        val hasNoCommentStyle = annotatedString.spanStyles.none { spanStyle ->
            spanStyle.item.color == testColors.commentColor
        }
        
        assertTrue(hasNoCommentStyle, "// innerhalb eines Strings sollte nicht als Kommentar behandelt werden")
    }
    
    @Test
    fun `Test comment inside string`() {
        // Setzen des inString-Status
        val initString = "color: \""
        highlighter.filter(AnnotatedString(initString))
        
        // Ein String mit Kommentar
        val smlText = "bl//ue\""
        
        val result = highlighter.filter(AnnotatedString(smlText))
        val annotatedString = result.text
        
        // Der gesamte Text sollte als String markiert sein (wir sind in einem String)
        val hasStringStyle = annotatedString.spanStyles.any { spanStyle ->
            spanStyle.start == 0 && 
            spanStyle.end == smlText.length && 
            spanStyle.item.color == testColors.attributeValueColor
        }
        
        assertTrue(hasStringStyle, "Text innerhalb eines Strings sollte komplett als String markiert sein")
        
        // String wird geschlossen, der inString-Status sollte jetzt false sein
        
        // Eine weitere Zeile sollte nicht als String markiert sein
        val nextLine = "normal text"
        val nextResult = highlighter.filter(AnnotatedString(nextLine))
        
        // Der Text sollte nicht als Teil eines Strings markiert sein
        val hasNoStringStyle = nextResult.text.spanStyles.none { spanStyle ->
            spanStyle.item.color == testColors.attributeValueColor
        }
        
        assertTrue(hasNoStringStyle, "Text nach geschlossenem String sollte nicht als String markiert sein")
    }
}
