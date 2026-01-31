/**
 * @author Ayakura Yuki
 * @date 2026/01/30-22:01
 */

package cc.ayakurayuki.nginxconfigurationformatter

private val LeftBraceRegex = Regex(".*?\\{(\\s*#.*)?$")
private val RightBraceRegex = Regex(".*?\\}(\\s*#.*)?$")
private val HtmlTagRegex = Regex("</?[a-zA-Z][^>]*>")
private val LineBreakRegex = Regex("\r\n|\r|\n")
private val WordRegex = Regex("\\S+")
private val LeadingSpaceRegex = Regex("^\\s+")

/**
 * Grabs text in between two separators
 * @param input String to extract
 * @param separator1 the first separator
 * @param separator2 the second separator
 * @return extracted part from input
 */
fun extractTextBySeparator(input: String, separator1: String, separator2: String = separator1): String {
    val separator1Regex = Regex.escape(separator1)
    val separator2Regex = Regex.escape(separator2)
    val catchRegex = Regex("$separator1Regex(.*?)$separator2Regex")
    val matchResult = catchRegex.find(input)
    return matchResult?.groupValues?.get(1) ?: ""
}

data class ExtractAllPossibleTextResult(
    var filteredInput: String,
    private val extracted: MutableMap<String, String> = mutableMapOf()
) {
    fun getRestored(): String {
        var textToFix = filteredInput
        for ((key, value) in extracted) {
            textToFix = textToFix.replace(key, value)
        }
        return textToFix
    }

    fun addExtracted(key: String, value: String) {
        extracted[key] = value
    }
}

/**
 * Grabs text in between two separators
 * @param input String to extract
 * @param separator1 the first separator
 * @param separator2 the second separator
 * @return all possible text result
 */
fun extractAllPossibleText(input: String, separator1: String, separator2: String = separator1): ExtractAllPossibleTextResult {
    val result = ExtractAllPossibleTextResult(input)
    var worker = input
    var textInBetween: String
    var cnt = 0
    val separator1CharCode = if (separator1.isNotEmpty()) separator1[0].code else 0
    val separator2CharCode = if (separator2.isNotEmpty()) separator2[0].code else 0

    while (extractTextBySeparator(worker, separator1, separator2).also { textInBetween = it }.isNotEmpty()) {
        val placeholder = "#$#%#$#placeholder$cnt$separator1CharCode$separator2CharCode#$#%#$#"
        val extractedText = separator1 + textInBetween + separator2
        result.addExtracted(placeholder, extractedText)
        worker = worker.replace(extractedText, placeholder)
        cnt++
    }

    result.filteredInput = worker
    return result
}

/**
 * Strips the line and replaces neighboring whitespaces with single space
 * (except when within quotation marks) trim the line before and after
 *
 * @param singleLine the line to strip
 * @return stripped out string without multiple spaces
 */
fun stripLine(singleLine: String): String {
    val trimmed = singleLine.trim()
    // get text without any quotation marks (text found with quotation marks is replaced with a placeholder)
    val removedDoubleQuotation = extractAllPossibleText(trimmed, "\"", "\"")
    // replace multiple spaces with single space, but skip in sub_filter directive
    if (!removedDoubleQuotation.filteredInput.contains("sub_filter")) {
        removedDoubleQuotation.filteredInput = removedDoubleQuotation.filteredInput.replace("\\s\\s+".toRegex(), " ")
    }
    // restore anything of quotation marks
    return removedDoubleQuotation.getRestored()
}

/**
 * put { } on their own separate lines
 * trim the spaces before and after each line
 * trim multi spaces into single spaces
 * trim multi lines into two
 */
fun cleanLines(configContents: String): List<String> {
    val lines = configContents.split(LineBreakRegex).toMutableList()

    var index = 0
    var newline = 0

    while (index < lines.size) {
        lines[index] = lines[index].trim()

        if (!lines[index].startsWith("#") && lines[index].isNotEmpty()) {
            newline = 0
            var line = stripLine(lines[index])
            lines[index] = line

            if (line != "{" && line != "}" && !(line.contains("('{") || line.contains("}')") || line.contains("'{'") || line.contains("'}'"))) {
                val startOfComment = line.indexOf("#")
                // val comment = if (startOfComment >= 0) line.substring(startOfComment) else ""
                var code = if (startOfComment >= 0) line.substring(0, startOfComment) else line

                val removedDoubleQuotation = extractAllPossibleText(code, "\"")
                val removedSingleQuotation = extractAllPossibleText(removedDoubleQuotation.filteredInput, "'")
                code = removedSingleQuotation.filteredInput

                val startOfParenthesis = code.indexOf("}")
                if (startOfParenthesis >= 0) {
                    if (startOfParenthesis > 0) {
                        val nl = stripLine(code.substring(0, startOfParenthesis))
                        lines[index] = nl
                        lines.add(index + 1, "}")
                    }
                    val l2 = stripLine(code.substring(startOfParenthesis + 1))
                    if (l2.isNotEmpty()) {
                        lines.add(index + 2, l2)
                    }
                    code = lines[index]
                }

                val endOfParenthesis = code.indexOf("{")
                if (endOfParenthesis >= 0) {
                    lines[index] = stripLine(code.substring(0, endOfParenthesis))
                    lines.add(index + 1, "{")
                    val l2 = stripLine(code.substring(endOfParenthesis + 1))
                    if (l2.isNotEmpty()) {
                        lines.add(index + 2, l2)
                    }
                }

                removedSingleQuotation.filteredInput = lines[index]
                val restoreFromSingleQuotation = removedSingleQuotation.getRestored()
                removedDoubleQuotation.filteredInput = restoreFromSingleQuotation
                line = removedDoubleQuotation.getRestored()
                lines[index] = line
            }
        } else if (lines[index].isEmpty()) {
            // remove more than two empty lines (newline >= 2)
            if (newline++ >= 2) {
                lines.removeAt(index)
                index--
            }
        }

        index++
    }

    return lines
}

data class FormatOptions(
    var indentation: String = "\t",
    var tailingBlankLines: Boolean = false
)

private var options = FormatOptions()

/**
 * Join opening brackets to the previous line
 */
fun joinOpeningBracket(lines: List<String>): List<String> {
    val result = lines.toMutableList()
    var i = 0
    while (i < result.size) {
        val line = result[i]
        if (line == "{") {
            // just make sure we don't put anything before 0
            if (i >= 1) {
                result[i] = result[i - 1] + " {"
                if (options.tailingBlankLines && result.size > (i + 1) && result[i + 1].isNotEmpty()) {
                    result.add(i + 1, "")
                }
                result.removeAt(i - 1)
                i--
            }
        }
        i++
    }
    return result
}

/**
 * Indents the lines according to their nesting level determined by curly brackets
 */
fun performIndentation(lines: List<String>): List<String> {
    val indentedLines = mutableListOf<String>()
    var currentIndent = 0
    var inMultilineStatement = false

    for (line in lines) {
        val trimmedLine = line.trim()

        // when met the `}`
        if (!trimmedLine.startsWith("#") && RightBraceRegex.matches(trimmedLine) && currentIndent > 0) {
            currentIndent -= 1
            inMultilineStatement = false
        }

        var lineIndent = currentIndent
        if (inMultilineStatement) {
            // for continuation lines of multi-line statements, add an extra level of indentation
            lineIndent += 1
        }

        if (trimmedLine.isNotEmpty()) {
            indentedLines.add(options.indentation.repeat(lineIndent) + trimmedLine)
        } else {
            indentedLines.add("")
        }

        // when met the `{`
        if (!trimmedLine.startsWith("#") && LeftBraceRegex.matches(trimmedLine)) {
            currentIndent += 1
            inMultilineStatement = false
        }

        // detect multiline statement
        if (trimmedLine.isNotEmpty() &&
            !LeftBraceRegex.matches(trimmedLine) &&
            !trimmedLine.startsWith("#") &&
            !RightBraceRegex.matches(trimmedLine) &&
            !trimmedLine.endsWith(";")
        ) {
            inMultilineStatement = true
        } else if (trimmedLine.endsWith(";")) {
            inMultilineStatement = false // ends with `;` as ends with multiline statement
        }
    }

    return indentedLines
}

/**
 * Align attribute values
 */
fun performAlignment(lines: List<String>): List<String> {
    val allLies = lines.toMutableList()
    val attributeLines = mutableListOf<String>()
    var minAlignColumn = 0

    for (line in lines) {
        if (line.isNotEmpty() && /* ignore empty line */
            !LeftBraceRegex.matches(line) && /* ignore block start */
            !line.trim().startsWith("#") && /* ignore comments */
            !RightBraceRegex.matches(line) && /* ignore block end */
            !line.trim().startsWith("upstream") && /* ignore upstream blocks */
            !line.trim().contains("location") && /* ignore location blocks */
            !HtmlTagRegex.containsMatchIn(line) && /* ignore html tags */
            line.trim().endsWith(";") /* only match single line attribute */
        ) {
            val splitLine = WordRegex.findAll(line).map { it.value }.toList()
            if (splitLine.size > 1) {
                attributeLines.add(line)
                val indentMatch = LeadingSpaceRegex.find(line)
                val indentLength = indentMatch?.value?.length ?: 0
                val alignColumn = indentLength + splitLine[0].length + 1
                if (minAlignColumn < alignColumn) {
                    minAlignColumn = alignColumn
                }
            }
        }
    }

    for (index in allLies.indices) {
        val line = allLies[index]
        if (attributeLines.contains(line)) {
            val split = WordRegex.findAll(line).map { it.value }.toList()
            val indent = LeadingSpaceRegex.find(line)?.value ?: ""
            val spaces = maxOf(1, minAlignColumn - split[0].length - indent.length)
            val newline = indent + split[0] +
                    " ".repeat(spaces) +
                    split.subList(1, split.size).joinToString(" ")
            allLies[index] = newline
        }
    }

    return allLies
}

/**
 * Main formatting function
 * @param configContents the nginx config content
 * @param indentation the indentation string (default: TAB)
 * @param alignValues whether to align attribute values
 * @return formatted config content
 */
fun formatNginxConfig(
    configContents: String,
    indentation: String = "\t",
    alignValues: Boolean = false
): String {
    options.indentation = indentation
    options.tailingBlankLines = false

    var lines = cleanLines(configContents)
    lines = joinOpeningBracket(lines)
    lines = performIndentation(lines)

    if (alignValues) {
        lines = performAlignment(lines)
    }

    return lines.joinToString("\n")
}
