package com.kcl

import com.kcl.binding.Binder
import com.kcl.syntax.SyntaxNode
import com.kcl.syntax.SyntaxToken

fun main(args: Array<String>) {
    var showTree = false
    while (true) {
        print(">>> ")
        val code = readln()

        if (code == "#showTree") {
            showTree = !showTree
            println("showTree setting is now be like - $showTree")
            continue
        }

        val parser = Parser(code)
        val syntaxTree = parser.parse()
        val compilation = Compilation(syntaxTree)
        val result = compilation.evaluate()

        val diagnostics = result.diagnostics

        if (showTree) prettyPrint(syntaxTree.root)

        if (diagnostics.any()) {
            diagnostics.forEach {
                println(it)
                println("       $code")
            }
        } else {
            println(result.value)
        }
    }
}

fun prettyPrint(node : SyntaxNode, indent : String = "", isLast : Boolean = true) {
    print(indent)
    print(if (isLast) "└──" else "├──")
    print(node.type)
    if (node is SyntaxToken && node.value != null) {
        print(" ")
        print(node.value)
    }

    println()

    node.getChildren().forEach {
        prettyPrint(it, indent + if (isLast) "    " else "│   ",  it == node.getChildren().last())
    }
}