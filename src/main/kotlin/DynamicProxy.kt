import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.random.Random

@Suppress("UNCHECKED_CAST")
fun main() {

    val numbers = (1..END_RANGE)
        .toList()
        .map {
            Proxy.newProxyInstance(
                ClassLoader.getSystemClassLoader(),
                arrayOf(Comparable::class.java),
                LogHandler(it)
            ) as Comparable<Int>
        }

    val searchNum = Random.nextInt(END_RANGE)
    val index = numbers.binarySearch {
        if (it == searchNum) println(" == $it")
        it.compareTo(searchNum)
    }
    if (index != -1) println("index = $index value = ${numbers[index]}")
}

class LogHandler(private val targetAbject: Any) : InvocationHandler {
    override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any {
        val arguments = args ?: emptyArray<Any>()
        print("$targetAbject ${method.name} ${arguments.toList()}")

        return method.invoke(targetAbject, *arguments)
            .also {
                println(" returned result = $it")
            }
    }
}

private const val END_RANGE = 10000