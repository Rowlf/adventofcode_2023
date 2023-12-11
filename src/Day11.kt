import kotlin.math.abs

// (C) 2023 A.Voß, a.voss@fh-aachen.de, kotlin@codebasedlearning.dev
// for advent of code 2023

// solutions part 1: 9693756, part 2: 717878258016
// example x / y

/*

    little time... refactor

 */

fun main() {
    val universe = readTextfile(filename = "Day11.txt").filter { it.trim().isNotBlank() }.toMutableList()
    val sizeUniverse = universe[0].length to universe.size

    // change from 2 to 1000000 for part 2
    var factor = 1000000

    fun findSpaces():Pair<MutableList<Long>,MutableList<Long>> {
        val rows = mutableListOf<Long>()
        val cols = mutableListOf<Long>()
        universe.forEachIndexed { row, line ->
            if (line.indexOf('#')==-1) rows.add(row.toLong())
        }
        (0..<sizeUniverse.second).forEach { col ->
            if (universe.withIndex().all { p -> p.value[col]=='.' }) cols.add(col.toLong())
        }
        return rows to cols
    }
    println("size universe $sizeUniverse")

    val (spaceRows, spaceCols) = findSpaces()
    println("additional rows ${spaceRows.joinToString()}, cols ${spaceCols.joinToString()}")

    val galaxies = mutableListOf<Pair<Long,Long>>()
    universe.forEachIndexed { row, line ->
        val cols = line.withIndex().filter { it.value=='#' }.map { it.index }
        cols.forEach { col ->
            val yAdd = spaceRows.filter { r -> r<row }.size.toLong()
            val xAdd = spaceCols.filter { c -> c<col }.size.toLong()
            galaxies.add((col+xAdd*(factor-1)) to (row+yAdd*(factor-1)))
        }
    }

    fun pairsOf(range: LongRange): List<Pair<Long,Long>>
        = range.flatMap { n0 -> range.map { n1 -> Pair(n0,n1) } }.filter { it.first < it.second }

    val galaxyPairs = pairsOf(0L..<galaxies.size)

    var sum = 0L
    galaxyPairs.forEach { (g1,g2) ->
        val dx = (galaxies[g1.toInt()].first - galaxies[g2.toInt()].first)      // toInt... todo
        val dy = (galaxies[g1.toInt()].second - galaxies[g2.toInt()].second)
        sum += abs(dx)+abs(dy)
    }
    val partX = sum

    println("11.12.23 AoC | part i: $partX")
}
