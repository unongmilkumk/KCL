package com.kcl.syntax

import com.kcl.ExpressionSyntax

class NumberExpressionSyntax(var numberToken : SyntaxToken, val value : Any?) : ExpressionSyntax {
    constructor(numberToken: SyntaxToken) : this(numberToken, numberToken.value)
    override var type: SyntaxType = SyntaxType.NUMBER_EXPRESSION
    override fun getChildren(): Iterable<SyntaxNode> {
        return arrayListOf(numberToken)
    }
}

class BinaryExpressionSyntax(var left : ExpressionSyntax, var operatorToken : SyntaxToken, var right : ExpressionSyntax) :
    ExpressionSyntax {
    override var type: SyntaxType = SyntaxType.BINARY_EXPRESSION
    override fun getChildren(): Iterable<SyntaxNode> {
        return arrayListOf(left, operatorToken, right)
    }
}

class ParenExpressionSyntax(var openParenToken : SyntaxToken, var expression : ExpressionSyntax, var closeParenToken: SyntaxToken) :
    ExpressionSyntax {
    override var type: SyntaxType = SyntaxType.PAREN_EXPRESSION
    override fun getChildren(): Iterable<SyntaxNode> {
        return arrayListOf(openParenToken, expression, closeParenToken)
    }
}

class UnaryExpressionSyntax(var operator : SyntaxToken, var operand : ExpressionSyntax) :
    ExpressionSyntax {
    override var type: SyntaxType = SyntaxType.UNARY_EXPRESSION
    override fun getChildren(): Iterable<SyntaxNode> {
        return arrayListOf(operator, operand)
    }
}