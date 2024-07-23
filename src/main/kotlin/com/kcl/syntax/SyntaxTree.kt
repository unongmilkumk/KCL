package com.kcl.syntax

import com.kcl.Diagnostic
import com.kcl.ExpressionSyntax
import com.kcl.Parser

class SyntaxTree(var diagnostics : List<Diagnostic>, var root : ExpressionSyntax, var endingToken : SyntaxToken) {
    companion object {
        fun parse(code : String) {
            val parser = Parser(code)
            parser.parse()
        }
    }
}