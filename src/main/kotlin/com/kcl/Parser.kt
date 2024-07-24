package com.kcl

import com.kcl.syntax.*

class Parser(text: String) {
    val diagnostics = DiagnosticBag()
    private val tokens = arrayListOf<SyntaxToken>()
    private var _position = 0


    init {

        val lexer = Lexer(text)
        lateinit var token: SyntaxToken
        do {
            token = lexer.nextToken()


            if (token.type != SyntaxType.WHITESPACE_TOKEN && token.type != SyntaxType.ERROR_TOKEN) {
                tokens.add(token)
            }
        } while (token.type != SyntaxType.ENDING_TOKEN)

        diagnostics.addAll(lexer.diagnostics)
    }

    private fun peek(offset : Int) : SyntaxToken {
        val index = _position + offset
        if (index >= tokens.size) return tokens.last()

        return tokens[index]
    }

    private fun current() : SyntaxToken = peek(0)

    private fun nextToken() : SyntaxToken {
        val current = current()
        _position++;
        return current
    }

    private fun match(type : SyntaxType) : SyntaxToken {
        if (current().type == type) return nextToken()
        diagnostics.reportUnexpectedToken(current().span, current().type, type)
        return SyntaxToken(type, current().position, null, null)
    }

    fun parse() : SyntaxTree {
        val expression = parseExpression()
        val endingToken = match(SyntaxType.ENDING_TOKEN)
        return SyntaxTree(diagnostics._diagnostics, expression, endingToken)
    }

    private fun parseExpression(parentPrecedence : Int = 0) : ExpressionSyntax {
        val unaryOperatorPrecedence = getUnaryOperatorPrecedence(current().type)
        var left = if (unaryOperatorPrecedence != 0 && unaryOperatorPrecedence >= parentPrecedence) {
            val operatorToken = nextToken()
            val operand = parseExpression(unaryOperatorPrecedence)
            UnaryExpressionSyntax(operatorToken, operand)
        } else {
            parsePrimaryExpression()
        }

        while (true) {
            val precedence = getBinaryOperatorPrecedence(current().type)
            if (precedence == 0 || precedence <= parentPrecedence) break
            val operatorToken = nextToken()
            val right = parseExpression(precedence)
            left = BinaryExpressionSyntax(left, operatorToken, right)
        }

        return left
    }

    companion object {
        fun getKeywordKind(text : String) : SyntaxType {
            return when (text) {
                "true" -> {
                    SyntaxType.TRUE_KEYWORD
                }

                "false" -> {
                    SyntaxType.FALSE_KEYWORD
                }

                else -> {
                    SyntaxType.IDENTIFIER_TOKEN
                }
            }
        }
        fun getUnaryOperatorPrecedence(type : SyntaxType) : Int {
            return when (type) {
                SyntaxType.PLUS_TOKEN,
                SyntaxType.MINUS_TOKEN,
                SyntaxType.BANG_TOKEN-> {
                    6
                }
                else -> {
                    0
                }
            }
        }
        fun getBinaryOperatorPrecedence(type : SyntaxType) : Int {
            return when (type) {
                SyntaxType.STAR_TOKEN,
                SyntaxType.SLASH_TOKEN -> {
                    5
                }
                SyntaxType.PLUS_TOKEN,
                SyntaxType.MINUS_TOKEN -> {
                    4
                }
                SyntaxType.EQUALS_EQUALS_TOKEN,
                SyntaxType.BANG_EQUALS_TOKEN -> {
                    3
                }
                SyntaxType.AMPERSAND_AMPERSAND_TOKEN -> {
                    2
                }
                SyntaxType.PIPE_PIPE_TOKEN -> {
                    1
                }
                else -> {
                    0
                }
            }
        }
    }

    private fun parsePrimaryExpression() : ExpressionSyntax {
        when (current().type) {
            SyntaxType.OPEN_PAREN_TOKEN -> {
                val left = nextToken()
                val expression = parseExpression()
                val right = match(SyntaxType.CLOSE_PAREN_TOKEN)
                return ParenExpressionSyntax(left, expression, right)
            }
            SyntaxType.TRUE_KEYWORD, SyntaxType.FALSE_KEYWORD -> {
                val keywordToken = nextToken()
                val value = keywordToken.type == SyntaxType.TRUE_KEYWORD
                return NumberExpressionSyntax(keywordToken, value)
            }
            else -> {
                val numberToken = match(SyntaxType.NUMBER_TOKEN)
                return NumberExpressionSyntax(numberToken)
            }
        }
    }
}