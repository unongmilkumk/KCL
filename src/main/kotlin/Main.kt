fun main(args: Array<String>) {
    while (true) {
        print(">>> ")
        val code = readln()

        var parser = Parser(code)
        var expression = parser.parse()

        prettyPrint(expression)

        if (parser.diagnostics.any()) {
            parser.diagnostics.forEach {
                println(it)
            }
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

class Lexer(private val _text : String) {
    private var _position : Int = 0
    val diagnostics = arrayListOf<String>()

    private fun current() : Char {
        if (_position >= _text.length) return (0).toChar()
        return _text[_position]
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
            val value = if (text.toIntOrNull() == null) 0 else text.toInt()
            return SyntaxToken(SyntaxType.WHITESPACE_TOKEN, start, text, value)
        }

        when (current()) {
            '+' -> return SyntaxToken(SyntaxType.PLUS_TOKEN, _position++, "+",  null)
            '-' -> return SyntaxToken(SyntaxType.MINUS_TOKEN, _position++, "-",  null)
            '*' -> return SyntaxToken(SyntaxType.STAR_TOKEN, _position++, "*",  null)
            '/' -> return SyntaxToken(SyntaxType.SLASH_TOKEN, _position++, "/",  null)
            '(' -> return SyntaxToken(SyntaxType.OPEN_PAREN_TOKEN, _position++, "(",  null)
            ')' -> return SyntaxToken(SyntaxType.CLOSE_PAREN_TOKEN, _position++, ")",  null)
        }

        diagnostics.add("Error : Bad Character Input : ${current()}")
        return SyntaxToken(SyntaxType.ERROR_TOKEN, _position++, _text.substring(_position - 1, _position), null)
    }
}

class SyntaxToken(override var type : SyntaxType, val position : Int, val text : String?, val value : Any?) : SyntaxNode {
    override fun getChildren(): Iterable<SyntaxNode> {
        return arrayListOf()
    }
}

enum class SyntaxType {
    NUMBER_TOKEN,
    WHITESPACE_TOKEN,
    PLUS_TOKEN,
    MINUS_TOKEN,
    STAR_TOKEN,
    SLASH_TOKEN,
    OPEN_PAREN_TOKEN,
    CLOSE_PAREN_TOKEN,
    ERROR_TOKEN,
    ENDING_TOKEN,
    NUMBER_EXPRESSION,
    BINARY_EXPRESSION
}

interface SyntaxNode {
     var type : SyntaxType

     fun getChildren() : Iterable<SyntaxNode>
}

interface ExpressionSyntax : SyntaxNode

class NumberExpressionSyntax(var numberToken : SyntaxToken) : ExpressionSyntax {
    override var type: SyntaxType = SyntaxType.NUMBER_EXPRESSION
    override fun getChildren(): Iterable<SyntaxNode> {
        return arrayListOf(numberToken)
    }
}

class BinaryExpressionSyntax(var left : ExpressionSyntax, var operatorToken : SyntaxToken, var right : ExpressionSyntax) : ExpressionSyntax {
    override var type: SyntaxType = SyntaxType.BINARY_EXPRESSION
    override fun getChildren(): Iterable<SyntaxNode> {
        return arrayListOf(left, operatorToken, right)
    }
}

class SyntaxTree(var root : ExpressionSyntax, var endingToken : SyntaxToken) {
    
}

class Parser(text: String) {
    val diagnostics = arrayListOf<String>()
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
        diagnostics.add("Error : Unexpected Token ${current().type}, expected $type")
        return SyntaxToken(type, current().position, null, null)
    }

    fun parse() : ExpressionSyntax {
        var left = parsePrimaryExpression();
        while (current().type == SyntaxType.PLUS_TOKEN || current().type == SyntaxType.PLUS_TOKEN) {
            val operatorToken = nextToken()
            val right = parsePrimaryExpression();
            left = BinaryExpressionSyntax(left, operatorToken, right)
        }
        return left
    }

    private fun parsePrimaryExpression() : ExpressionSyntax {
        var numberToken = match(SyntaxType.NUMBER_TOKEN)
        return NumberExpressionSyntax(numberToken)
    }
}