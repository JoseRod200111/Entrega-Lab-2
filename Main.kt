import java.util.*

class Calculadora {

    fun evaluarExpresion(expresion: String): Double {
        val postfix = convertirPostfijo(expresion)
        return evaluarPostfijo(postfix)
    }

    private fun convertirPostfijo(expresion: String): List<String> {
        val output = mutableListOf<String>()
        val operadores = Stack<Char>()
        val tokens = expresion.replace("(", " ( ").replace(")", " ) ").split(" ")

        val precedencia = mapOf(
            '+' to 1, '-' to 1,
            '*' to 2, '/' to 2,
            '^' to 3
        )

        for (token in tokens) {
            when {
                token.isEmpty() -> continue
                token.toDoubleOrNull() != null -> output.add(token)
                token == "(" -> operadores.push('(')
                token == ")" -> {
                    while (operadores.isNotEmpty() && operadores.peek() != '(') {
                        output.add(operadores.pop().toString())
                    }
                    operadores.pop()
                }
                else -> {
                    while (operadores.isNotEmpty() && precedencia[token[0]] ?: 0 <= precedencia[operadores.peek()] ?: 0) {
                        output.add(operadores.pop().toString())
                    }
                    operadores.push(token[0])
                }
            }
        }
        while (operadores.isNotEmpty()) {
            output.add(operadores.pop().toString())
        }
        return output
    }

    private fun evaluarPostfijo(postfix: List<String>): Double {
        val pila = Stack<Double>()

        for (token in postfix) {
            when {
                token.toDoubleOrNull() != null -> pila.push(token.toDouble())
                else -> {
                    val b = pila.pop()
                    val a = pila.pop()
                    val resultado = when (token) {
                        "+" -> a + b
                        "-" -> a - b
                        "*" -> a * b
                        "/" -> a / b
                        "^" -> Math.pow(a, b)
                        else -> throw IllegalArgumentException("Operador desconocido: $token")
                    }
                    pila.push(resultado)
                }
            }
        }
        return pila.pop()
    }
}

fun main() {
    val calculadora = Calculadora()
    println("Ingrese una expresión matemática:")
    val expresion = readLine() ?: ""
    try {
        val resultado = calculadora.evaluarExpresion(expresion)
        println("El resultado de la expresión '$expresion' es: $resultado")
    } catch (e: Exception) {
        println("Error al evaluar la expresión: ${e.message}")
    }
}
