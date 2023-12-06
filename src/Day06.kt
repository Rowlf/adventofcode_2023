// (C) 2023 A.Voß, a.voss@fh-aachen.de, kotlin@codebasedlearning.dev
// for advent of code 2023

// solutions part 1: 1413720, part 2: 30565288
// 288 / 71503

private fun String.shrinkIf(predicate: Boolean) = if (predicate) replace(" ","") else this

fun main() {
    sequenceOf(filename = "Day06.txt").toList().also { (timesStr,distStr) ->
        print("06.12.23 AoC |")
        for (puzzle in 1..2) {
            val times = timesStr.split(":")[1].shrinkIf(puzzle==2).toLongList()
            val dists = distStr.split(":")[1].shrinkIf(puzzle==2).toLongList()

            val ways = times.zip(dists).fold(1) { rc, (time,dist) -> rc * (0..time).count { dt -> (time-dt)*dt > dist } }
            print(" part $puzzle: $ways")
        }
        println()
    }
}
