package at.crowdware.nocode.texteditor.syntax

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class MarkdownSyntaxHighlighter(val colors: ExtendedColors) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(applyMarkdownHighlighting(text), OffsetMapping.Identity)
    }

    private fun applyMarkdownHighlighting(text: AnnotatedString): AnnotatedString {
        val builder = AnnotatedString.Builder(text)

        // Apply all the highlight rules
        highlightHeaders(builder, text)
        highlightBold(builder, text)
        highlightItalic(builder, text)
        highlightLinks(builder, text)
        highlightCodeBlocks(builder, text)
        highlightListItems(builder, text)
        highlightInlineHtml(builder, text)
        return builder.toAnnotatedString()
    }

    // Header highlighting: "# Title" and "## Subtitle"
    private fun highlightHeaders(builder: AnnotatedString.Builder, text: AnnotatedString) {
        val headerRegex = Regex("(^#+)\\s(.+)", RegexOption.MULTILINE)
        headerRegex.findAll(text.text).forEach { match ->
            // Berechnung des Bereichs für headerMarks
            val headerMarks = match.groups[1]?.value ?: ""
            val matchStart = match.range.first
            val headerMarksStart = matchStart + match.value.indexOf(headerMarks)
            val headerMarksEnd = headerMarksStart + headerMarks.length
            val headerMarksRange = headerMarksStart until headerMarksEnd

            // Berechnung des Bereichs für headerText
            val headerText = match.groups[2]?.value ?: ""
            val headerTextStart = matchStart + match.value.indexOf(headerText)
            val headerTextEnd = headerTextStart + headerText.length
            val headerTextRange = headerTextStart until headerTextEnd

            // "#" in syntax color
            builder.addStyle(
                style = SpanStyle(color = colors.syntaxColor),
                start = headerMarksRange.first,
                end = headerMarksRange.last + 1
            )
            // Text in magenta
            builder.addStyle(
                style = SpanStyle(color = colors.mdHeader),
                start = headerTextRange.first,
                end = headerTextRange.last + 1
            )
        }
    }

    // Bold highlighting: "**bold**"
    private fun highlightBold(builder: AnnotatedString.Builder, text: AnnotatedString) {
        val boldRegex = Regex("\\*\\*(.*?)\\*\\*")
        boldRegex.findAll(text.text).forEach { match ->
            val boldText = match.groups[1]?.value ?: ""
            val matchStart = match.range.first
            val boldTextStart = matchStart + match.value.indexOf(boldText)
            val boldTextEnd = boldTextStart + boldText.length
            val boldTextRange = boldTextStart until boldTextEnd

            // "**" in syntax color (white)
            builder.addStyle(
                style = SpanStyle(color = colors.syntaxColor),
                start = match.range.first,
                end = match.range.first + 2
            )
            builder.addStyle(
                style = SpanStyle(color = colors.syntaxColor),
                start = match.range.last - 1,
                end = match.range.last + 1
            )

            // Bold text in default text color
            builder.addStyle(
                style = SpanStyle(fontWeight = FontWeight.Bold, color = colors.defaultTextColor),
                start = boldTextRange.first,
                end = boldTextRange.last + 1
            )
        }
    }

    // Italic highlighting: "*italic*"
    private fun highlightItalic(builder: AnnotatedString.Builder, text: AnnotatedString) {
        val italicRegex = Regex("\\*(.*?)\\*")
        italicRegex.findAll(text.text).forEach { match ->
            val italicText = match.groups[1]?.value ?: ""
            val matchStart = match.range.first
            val italicTextStart = matchStart + match.value.indexOf(italicText)
            val italicTextEnd = italicTextStart + italicText.length
            val italicTextRange = italicTextStart until italicTextEnd

            // "*" in syntax color (white)
            builder.addStyle(
                style = SpanStyle(color = colors.syntaxColor),
                start = match.range.first,
                end = match.range.first + 1
            )
            builder.addStyle(
                style = SpanStyle(color = colors.syntaxColor),
                start = match.range.last,
                end = match.range.last + 1
            )

            // Italic text in default text color
            builder.addStyle(
                style = SpanStyle(fontStyle = FontStyle.Italic, color = colors.defaultTextColor),
                start = italicTextRange.first,
                end = italicTextRange.last + 1
            )
        }
    }

    // Link highlighting: "[link](url)"
    private fun highlightLinks(builder: AnnotatedString.Builder, text: AnnotatedString) {
        val linkRegex = Regex("\\[(.+?)\\]\\((.+?)\\)")
        linkRegex.findAll(text.text).forEach { match ->
            val linkText = match.groups[1]?.value ?: ""
            var matchStart = match.range.first
            val linkTextStart = matchStart + match.value.indexOf(linkText)
            val linkTextEnd = linkTextStart + linkText.length
            val linkTextRange = linkTextStart until linkTextEnd

            val urlText = match.groups[2]?.value ?: ""
            val urlTextStart = matchStart + match.value.indexOf(urlText)
            val urlTextEnd = urlTextStart + urlText.length
            val urlRange = urlTextStart until urlTextEnd

            // "[" and "]" in blue (covering the entire link text with brackets)
            builder.addStyle(
                style = SpanStyle(color = colors.linkColor),
                start = match.range.first, // The '['
                end = linkTextRange.last + 2 // The ']'
            )

            // "(" and ")" in green
            builder.addStyle(
                style = SpanStyle(color = colors.attributeValueColor),
                start = linkTextRange.last + 2, // The '('
                end = urlRange.last + 2 // The ')' (include the final parenthesis)
            )

            // URL in blue
            builder.addStyle(
                style = SpanStyle(color = colors.linkColor),
                start = urlRange.first,
                end = urlRange.last + 1
            )
        }
    }

    private fun highlightCodeBlocks(builder: AnnotatedString.Builder, text: AnnotatedString) {
        // Regex für Code-Blöcke: ```code```
        val codeBlockRegex = Regex("```[\\s\\S]*?```", RegexOption.MULTILINE)
        codeBlockRegex.findAll(text.text).forEach { match ->
            // Render the code block in a different color
            builder.addStyle(
                style = SpanStyle(color = colors.syntaxColor),
                start = match.range.first,
                end = match.range.last + 1
            )
        }
    }

    // List item highlighting: "- Item"
    private fun highlightListItems(builder: AnnotatedString.Builder, text: AnnotatedString) {
        val listItemRegex = Regex("^(-)\\s", RegexOption.MULTILINE)
        listItemRegex.findAll(text.text).forEach { match ->
            builder.addStyle(
                style = SpanStyle(color = colors.syntaxColor),
                start = match.range.first,
                end = match.range.last
            )
        }
    }

    private fun highlightInlineHtml(builder: AnnotatedString.Builder, text: AnnotatedString) {
        val htmlTagRegex = Regex("<([a-zA-Z]+)(\\s+[a-zA-Z]+=\"[^\"]*\")*\\s*/?>")
        htmlTagRegex.findAll(text.text).forEach { match ->

            val tagName = match.groups[1]?.value ?: ""
            val matchStart = match.range.first
            val tagNameStart = matchStart + match.value.indexOf(tagName)
            val tagNameEnd = tagNameStart + tagName.length
            val tagNameRange = tagNameStart until tagNameEnd
            val attributesRegex = Regex("([a-zA-Z]+)=(\"[^\"]*\")")
            val attributesMatch = attributesRegex.findAll(match.value)

            builder.addStyle(
                style = SpanStyle(color = colors.syntaxColor), // Purple for HTML tag
                start = match.range.first,
                end = tagNameRange.last + 1
            )

            // HTML attributes in attributeNameColor and values in attributeValueColor
            attributesMatch.forEach { attrMatch ->
                // Berechnung des Bereichs für attrName
                val attrName = attrMatch.groups[1]?.value ?: ""
                val attrMatchStart = attrMatch.range.first
                val attrNameStart = attrMatchStart + attrMatch.value.indexOf(attrName)
                val attrNameEnd = attrNameStart + attrName.length
                val attrNameRange = attrNameStart until attrNameEnd

                // Berechnung des Bereichs für attrValue
                val attrValue = attrMatch.groups[2]?.value ?: ""
                val attrValueStart = attrMatchStart + attrMatch.value.indexOf(attrValue)
                val attrValueEnd = attrValueStart + attrValue.length
                val attrValueRange = attrValueStart until attrValueEnd

                // Attribute name in attributeNameColor (e.g., src, id)
                builder.addStyle(
                    style = SpanStyle(color = colors.attributeNameColor),
                    start = match.range.first + attrNameRange.first,
                    end = match.range.first + attrNameRange.last + 1
                )

                // `=` and value in attributeValueColor (e.g., ="link")
                builder.addStyle(
                    style = SpanStyle(color = colors.attributeValueColor),
                    start = match.range.first + attrValueRange.first - 1, // Include `=`
                    end = match.range.first + attrValueRange.last + 1 // Include `"`
                )
            }

            // Close tag `/>` in purple
            val closeTagIndex = match.range.last - 1
            if (text.text[closeTagIndex] == '/') {
                builder.addStyle(
                    style = SpanStyle(color = colors.syntaxColor),
                    start = closeTagIndex,
                    end = match.range.last + 1 // Cover `/>`
                )
            } else {
                builder.addStyle(
                    style = SpanStyle(color = colors.syntaxColor),
                    start = match.range.last,
                    end = match.range.last + 1
                )
            }
        }
    }
}
