package com.kcl.syntax

import com.kcl.TextSpan

class SyntaxToken(override var type : SyntaxType, val position : Int, val text : String?, val value : Any?) : SyntaxNode {
    val span : TextSpan
        get() = TextSpan(position, text!!.length)
    override fun getChildren(): Iterable<SyntaxNode> {
        return arrayListOf()
    }
}