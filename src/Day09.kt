// (C) 2023 A.Voß, a.voss@fh-aachen.de, kotlin@codebasedlearning.dev
// for advent of code 2023

// solutions part 1: 1953784198, part 2: 957
// example 114 / 2

typealias DiffLists = MutableList<List<Long>>

fun lineToDiffLists(line: String):DiffLists = mutableListOf<List<Long>>().apply {
    add(line.toLongList())
    while (last().any { it!=0L }) {
        add(MutableList(last().size - 1) { i -> last()[i + 1] - last()[i] })
    }
}

fun main() {
    sequenceOf(filename = "Day09.txt").filter { it.isNotBlank() }
        .map { line -> lineToDiffLists(line) }
        .run { fold(0L to 0L) { (n,m), lists ->
            val (dn,dm) = lists.reversed().fold(0L to 0L) { (s,t), nums -> nums.last() + s to nums.first() - t }
            n+dn to m+dm
        }
    }.apply { println("09.12.23 AoC | part 1: $first, part 2: $second") }
}
