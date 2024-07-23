package com.kcl.binding

import com.kcl.Diagnostic
import com.kcl.DiagnosticBag
import com.kcl.ExpressionSyntax
import com.kcl.syntax.BinaryExpressionSyntax
import com.kcl.syntax.NumberExpressionSyntax
import com.kcl.syntax.SyntaxType
import com.kcl.syntax.UnaryExpressionSyntax
import java.lang.reflect.Type

@Suppress("NON_EXHAUSTIVE_WHEN_STATEMENT")
class Binder {
    val diagnostics = DiagnosticBag()

    fun bindExpression(syntax: ExpressionSyntax) : BoundExpression {
        when (syntax.type) {
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

    private fun bindUnaryOperatorKind(type: SyntaxType, operandType : Type): BoundUnaryOperatorKind? {
        if (operandType == java.lang.Integer::class.java) {
            when (type) {
            SyntaxType.PLUS_TOKEN -> return BoundUnaryOperatorKind.IDENTITY
            SyntaxType.MINUS_TOKEN -> return BoundUnaryOperatorKind.NEGATION
            }
        }

        if (operandType == java.lang.Boolean::class.java) {
            when (type) {
                SyntaxType.BANG_TOKEN -> return BoundUnaryOperatorKind.LOGICAL_NEGATION
            }
        }

        return null
    }

    private fun bindBinaryOperatorKind(type: SyntaxType, leftType: Type, rightType: Type): BoundBinaryOperatorKind? {
        if (leftType == java.lang.Integer::class.java && rightType == java.lang.Integer::class.java) {
            when (type) {
                SyntaxType.PLUS_TOKEN -> return BoundBinaryOperatorKind.ADDICTION
                SyntaxType.MINUS_TOKEN -> return BoundBinaryOperatorKind.SUBTRACTION
                SyntaxType.STAR_TOKEN -> return BoundBinaryOperatorKind.MULTIPLICATION
                SyntaxType.SLASH_TOKEN -> return BoundBinaryOperatorKind.DIVISION
            }
            throw Exception("Unexpected syntax")
        }

        if (leftType == java.lang.Boolean::class.java && rightType == java.lang.Boolean::class.java) {
            when (type) {
                SyntaxType.AMPERSAND_AMPERSAND_TOKEN -> return BoundBinaryOperatorKind.LOGICAL_AND
                SyntaxType.PIPE_PIPE_TOKEN -> return BoundBinaryOperatorKind.LOGICAL_OR
            }
            throw Exception("Unexpected syntax")
        }

        return null
    }
}