import kotlin.math.abs

// (C) 2023 A.Voß, a.voss@fh-aachen.de, kotlin@codebasedlearning.dev
// for advent of code 2023

// solutions part 1: 9693756, part 2: 717878258016
// example x / y

/*
    little time...
 */

fun main() {
    val universe = TextBlock(filename = "Day11.txt")
    // println("size universe ${universe.cols to universe.rows}")

    fun findEmptySpaces() =
        universe.withIndex().filter { it.value.indexOf('#')==-1 }.map { row->row.index } to
                (0..<universe.rows).filter { col->universe.withIndex().all { it.value[col]=='.' } }

    val (spaceRows, spaceCols) = findEmptySpaces()
    // println("additional rows ${spaceRows.joinToString()}; cols ${spaceCols.joinToString()}")

    print("11.12.23 AoC |")
    for (puzzle in 1..2) {
        val factor = if (puzzle==1) 2 else 1000000

        val galaxies = mutableListOf<Pair<Int,Int>>()
        universe.forEachIndexed { row, line ->
            val cols = line.withIndex().filter { it.value == '#' }.map { col->col.index }
            cols.forEach { col ->
                val rowsAdd = spaceRows.filter { r -> r < row }.size
                val colsAdd = spaceCols.filter { c -> c < col }.size
                galaxies.add((col + colsAdd * (factor - 1)) to (row + rowsAdd * (factor - 1)))
            }
        }

        fun pairsOf(range: IntRange): List<Pair<Int,Int>> =
            range.flatMap { n0 -> range.map { n1 -> Pair(n0, n1) } }.filter { it.first < it.second }

        val sum = pairsOf(0..<galaxies.size).sumOf { (g1, g2) ->
            val dx = (galaxies[g1].first.toLong() - galaxies[g2].first.toLong())
            val dy = (galaxies[g1].second.toLong() - galaxies[g2].second.toLong())
            abs(dx) + abs(dy)
        }

        print(" part $puzzle: $sum")
    }
    println()
}
