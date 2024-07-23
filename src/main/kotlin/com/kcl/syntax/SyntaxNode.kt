package com.kcl.syntax

interface SyntaxNode {
    var type : SyntaxType

    fun getChildren() : Iterable<SyntaxNode>
}