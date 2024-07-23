package com.kcl.binding

import com.kcl.Diagnostic
import com.kcl.DiagnosticBag
import com.kcl.ExpressionSyntax
import com.kcl.syntax.*
import java.lang.reflect.Type

@Suppress("NON_EXHAUSTIVE_WHEN_STATEMENT")
class Binder {
    val diagnostics = DiagnosticBag()

    fun bindExpression(syntax: ExpressionSyntax) : BoundExpression {
        when (syntax.type) {
            SyntaxType.PAREN_EXPRESSION -> return bindParenthesizedExpression(syntax as ParenExpressionSyntax);
            SyntaxType.NUMBER_EXPRESSION -> return bindLiteralExpression(syntax as NumberExpressionSyntax)
            SyntaxType.UNARY_EXPRESSION -> return bindUnaryExpression(syntax as UnaryExpressionSyntax)
            SyntaxType.BINARY_EXPRESSION -> return bindBinaryExpression(syntax as BinaryExpressionSyntax)
        }
        throw Exception("Unexpected syntax")
    }

    private fun bindLiteralExpression(numberExpressionSyntax: NumberExpressionSyntax): BoundExpression {
        val value = numberExpressionSyntax.value ?: 0
        return BoundLiteralExpression(value)
    }

    private fun bindUnaryExpression(unaryExpressionSyntax: UnaryExpressionSyntax): BoundExpression {
        val boundOperand = bindExpression(unaryExpressionSyntax.operand)
        val boundOperator = BoundUnaryOperator.bind(unaryExpressionSyntax.operator.type, boundOperand.type)
        if (boundOperator == null) {
            diagnostics.reportUndefinedUnaryOperator(unaryExpressionSyntax.operator.span, unaryExpressionSyntax.operator.text, boundOperand.type)
            return boundOperand
        }
        return BoundUnaryExpression(boundOperator, boundOperand)
    }

    private fun bindBinaryExpression(binaryExpressionSyntax: BinaryExpressionSyntax): BoundExpression {
        val boundLeft = bindExpression(binaryExpressionSyntax.left)
        val boundRight = bindExpression(binaryExpressionSyntax.right)
        val boundOperator = BoundBinaryOperator.bind(binaryExpressionSyntax.operatorToken.type, boundLeft.type, boundRight.type)
        if (boundOperator == null) {
            diagnostics.reportUndefinedBinaryOperator(binaryExpressionSyntax.operatorToken.span, binaryExpressionSyntax.operatorToken.text, boundLeft.type, boundRight.type)
            return boundLeft
        }
        return BoundBinaryExpression(boundLeft, boundOperator, boundRight)
    }

    private fun bindParenthesizedExpression(syntax: ParenExpressionSyntax) : BoundExpression {
        return bindExpression(syntax.expression)
    }
}