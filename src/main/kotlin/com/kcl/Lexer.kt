package com.kcl

import com.kcl.syntax.SyntaxToken
import com.kcl.syntax.SyntaxType

class Lexer(private val _text : String) {
    private var _position : Int = 0
    val diagnostics = DiagnosticBag()

    private fun current() : Char = peek(0)
    private fun lookAhead() : Char = peek(1)

    fun peek(offset : Int) : Char {
        val index = _position + offset
        if (index >= _text.length) return (0).toChar()
        return _text[index]
    }

    fun next() {
        _position += 1
    }

    fun nextToken() : SyntaxToken {

        if (_position >= _text.length) {
            return SyntaxToken(SyntaxType.ENDING_TOKEN, _position, (0).toChar().toString(), null)
        }

        if (Character.isDigit(current())) {
            val start = _position

            while (Character.isDigit(current())) {
                next()
            }

            val end = _position
            val text = _text.substring(start, end)
            if (text.toIntOrNull() == null) diagnostics.reportInvalidNumber(TextSpan(start, end - start), _text, java.lang.Integer::class.java)
            val value = if (text.toIntOrNull() == null) 0 else text.toInt()
            return SyntaxToken(SyntaxType.NUMBER_TOKEN, start, text, value)
        }

        if (current() == ' ') {
            val start = _position

            while (current() == ' ') {
                next()
            }

            val end = _position
            val text = _text.substring(start, end)
            return SyntaxToken(SyntaxType.WHITESPACE_TOKEN, start, text, null)
        }

        if (current().isLetter()) {
            val start = _position

            while (current().isLetter()) {
                next()
            }

            val end = _position
            val text = _text.substring(start, end)
            val kind = Parser.getKeywordKind(text)
            return SyntaxToken(kind, start, text, null)
        }

        when (current()) {
            '+' -> return SyntaxToken(SyntaxType.PLUS_TOKEN, _position++, "+",  null)
            '-' -> return SyntaxToken(SyntaxType.MINUS_TOKEN, _position++, "-",  null)
            '*' -> return SyntaxToken(SyntaxType.STAR_TOKEN, _position++, "*",  null)
            '/' -> return SyntaxToken(SyntaxType.SLASH_TOKEN, _position++, "/",  null)
            '(' -> return SyntaxToken(SyntaxType.OPEN_PAREN_TOKEN, _position++, "(",  null)
            ')' -> return SyntaxToken(SyntaxType.CLOSE_PAREN_TOKEN, _position++, ")",  null)
            '!' ->{
                if (lookAhead() == '=') {
                    val syntaxToken = SyntaxToken(SyntaxType.BANG_EQUALS_TOKEN, _position, "!=", null)
                    _position += 2
                    return syntaxToken
                } else return SyntaxToken(SyntaxType.BANG_TOKEN, _position++, "!",  null)
            }
            '&' -> {
                if (lookAhead() == '&') {
                    val syntaxToken = SyntaxToken(SyntaxType.AMPERSAND_AMPERSAND_TOKEN, _position , "&&",  null)
                    _position += 2
                    return syntaxToken
                }
            }
            '|' -> {
                if (lookAhead() == '|') {
                    val syntaxToken = SyntaxToken(SyntaxType.PIPE_PIPE_TOKEN, _position, "&&", null)
                    _position += 2
                    return syntaxToken
                }
            }
            '=' -> {
                if (lookAhead() == '=') {
                    val syntaxToken = SyntaxToken(SyntaxType.EQUALS_EQUALS_TOKEN, _position, "==", null)
                    _position += 2
                    return syntaxToken
                }
            }
        }

        diagnostics.reportBadCharacter(_position, current())
        return SyntaxToken(SyntaxType.ERROR_TOKEN, _position++, _text.substring(_position - 1, _position), null)
    }
}