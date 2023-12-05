import java.util.*

// (C) 2023 A.Voß, a.voss@fh-aachen.de, kotlin@codebasedlearning.dev
// for advent of code 2023

enum class Category(val category: String) {
    NONE("none"), SEED("seed"), SOIL("soil"), FERTILIZER("fertilizer"), WATER("water"), LIGHT("light"),
    TEMPERATURE("temperature"), HUMIDITY("humidity"), LOCATION("location") }

data class Ranges(val srcRange:LongRange, val dstRange:LongRange)

class CatMap(val dstCategory: Category, val ranges: MutableList<Ranges> = mutableListOf()) {
    fun mapTo(n:Long): Long {
        for (r in ranges) {
            if (n in r.srcRange)
                return n-r.srcRange.first +r.dstRange.first
        }
        return n
    }
}

fun String.toLongList() = this.split(' ').filter { it.trim().isNotBlank() }.map { it.toLong() }

class Planting(private val map: MutableMap<Category,CatMap> = mutableMapOf()) {

    private var lastFromCat: Category = Category.NONE

    // depends on the lastFromCat state... we can live with that for now
    fun update(line: String) {
        // assume a map exists only once and the file format is ok
        val trimmedLine = line.trim()
        when  {
            trimmedLine.isBlank() -> lastFromCat = Category.NONE
            trimmedLine.endsWith("map:") -> {
                val parts = trimmedLine.split("-to-")
                val fromCat = Category.valueOf(parts[0].trim().uppercase(Locale.getDefault()))
                val toCat = Category.valueOf(parts[1].split(' ')[0].trim().uppercase(Locale.getDefault()))
                lastFromCat = fromCat
                map[lastFromCat] = CatMap(toCat)
            }
            else -> {
                trimmedLine.toLongList().also {  map[lastFromCat]!!.ranges.add(
                    Ranges(srcRange = LongRange(it[1],it[1]+it[2]-1), dstRange = LongRange(it[0],it[0]+it[2]-1))
                ) }
            }
        }
    }

    fun mapTo(sd: Long) : Long {
        var n = sd
        var catFrom = Category.SEED
        while (catFrom!=Category.LOCATION) {
            n = map[catFrom]!!.apply { catFrom = dstCategory }.mapTo(n)
        }
        return n
    }
}

// solutions part 1: 265018614, part 2: 63179500
// Day05a: 35 / 46
fun main() {
    var seeds = listOf<Long>()
    val planting = Planting()
    sequenceOf(filename = "Day05.txt").forEach { line ->
        if (seeds.isEmpty() && line.isNotBlank() && line.startsWith("seeds:")) {
            seeds  = line.removePrefix("seeds:").toLongList()
        } else {
            planting.update(line)
        }
    }

    val minPart1 = seeds.minOf { n -> planting.mapTo(n) }
    val minPart2 = (seeds.indices step 2).map { i -> LongRange(seeds[i],seeds[i]+seeds[i + 1]-1) }
        .minOf { rg -> rg.minOf { n -> planting.mapTo(n) } }

    // needs about 11-12 min
    println("05.12.23 AoC | part 1: $minPart1, part 2: $minPart2")
}
