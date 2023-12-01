// (C) 2023 A.Voß, a.voss@fh-aachen.de, kotlin@codebasedlearning.dev
// for advent of code 2023

// solutions part 1: 54697, part 2: 54885
fun main() {
    // for part 1 comment out 'number words'
    val digits = mapOf(
        "0" to 0, "1" to 1, "2" to 2, "3" to 3, "4" to 4,
        "5" to 5, "6" to 6, "7" to 7, "8" to 8, "9" to 9,
        "one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5,
        "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9
    )
    sequenceOf(filename = "Day01.txt").sumOf { line ->
        line.findAnyOf(digits.keys)?.let { d1 ->
            line.findLastAnyOf(digits.keys)!!.let { d0 ->       // all '!!' are safe to assume
                digits[d1.second]!! * 10 + digits[d0.second]!!
            }
        } ?: 0
    }.also { println("01.12.23 AoC | calibration value: $it") }
}
