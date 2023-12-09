// (C) 2023 A.Voß, a.voss@fh-aachen.de, kotlin@codebasedlearning.dev
// for advent of code 2023

// solutions part 1: 21389, part 2: 21083806112641
// example 2 / 6 / 6

import kotlin.math.abs

private fun gcd(a: Long, b: Long): Long = if (b == 0L) abs(a) else gcd(b, a % b)
private fun lcm(a: Long, b: Long): Long = abs(a / gcd(a, b) * b)
private fun lcm(numbers: List<Long>): Long = numbers.reduce { a, b -> lcm(a, b) }

data class LR(val left:String, val right:String)

fun main() {
    var pattern = ""
    val network = sequenceOf(filename = "Day08.txt")
        .filter { it.isNotBlank() }
        .fold(mutableMapOf<String,LR>()) { map, line -> map.apply {
            if (pattern.isEmpty())
                pattern = line
            else
                line.split('=').also { (key, lr) ->
                    this[key.trim()] = lr.split(',').let { (l, r) ->
                        LR(l.trim().substring(1), r.trim().removeSuffix(")"))
                    }
                }
            }
        }

    fun lookForZ(start: String, isPuzzle1: Boolean):Long {
        var steps = 0L
        var dir = 0
        var pos = start
        while ( (isPuzzle1 && pos != "ZZZ") || (!isPuzzle1 && !pos.endsWith("Z"))) {
            pos = network[pos]!!.let { m -> if (pattern[dir] == 'L') m.left else m.right }
            dir = (dir + 1) % pattern.length
            ++steps
        }
        return steps
    }

    val steps1 = lookForZ("AAA", isPuzzle1 = true)

    // This strategy ignores the fact that there could be a shorter solution
    // if we continued after the first position ending on 'Z'.
    // However, it works...
    val counts = network.keys.filter { it.endsWith("A") }
        .map { pos -> lookForZ(pos, isPuzzle1 = false) }
    val steps2 = lcm(counts)  // 23147, 19631, 12599, 21389, 17873, 20803

    println("08.12.23 AoC | part 1: $steps1, part 2: $steps2")
}
