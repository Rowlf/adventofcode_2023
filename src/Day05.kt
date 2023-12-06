import java.util.*

// (C) 2023 A.Voß, a.voss@fh-aachen.de, kotlin@codebasedlearning.dev
// for advent of code 2023

enum class Category(val category: String) {
    NONE("none"), SEED("seed"), SOIL("soil"), FERTILIZER("fertilizer"), WATER("water"), LIGHT("light"),
    TEMPERATURE("temperature"), HUMIDITY("humidity"), LOCATION("location") }

data class Ranges(val srcRange:LongRange, val dstRange:LongRange)

class CatMap(val dstCategory: Category, val ranges: MutableList<Ranges> = mutableListOf()) {
    fun mapTo(n: Long): Long {
        for (rg in ranges) {
            if (n in rg.srcRange)
                return n-rg.srcRange.first + rg.dstRange.first
        }
        return n
    }
}

class Planting(private val map: MutableMap<Category,CatMap> = mutableMapOf()) {

    private var lastFromCat: Category = Category.NONE
    val seeds = mutableListOf<Long>()

    private val order = mutableListOf<CatMap>() // or Pair<Category,CatMap>

    // depends on the lastFromCat state... we can live with that for now
    fun update(line: String) = apply {
        // assume a map exists only once and the file format is ok
        val trimmedLine = line.trim()
        when  {
            trimmedLine.isBlank() -> lastFromCat = Category.NONE
            trimmedLine.startsWith("seeds:")
                -> seeds.addAll(line.removePrefix("seeds:").toLongList())
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

    fun prepareMapping() {
        var catFrom = Category.SEED
        do {
            order.add(map[catFrom]!!.apply { catFrom = dstCategory })
        } while (catFrom != Category.LOCATION)
    }

    // needs preparation to gain speed
    fun mapTo(seed: Long) : Long = order.fold(seed) { n, map -> map.mapTo(n) }
}

// solutions part 1: 265018614, part 2: 63179500
// Day05a: 35 / 46
// needs about 6-7 min
fun main() {
    sequenceOf(filename = "Day05.txt").fold(Planting()) { planting, line ->
        planting.update(line)
    }.apply {
        prepareMapping()

        print("05.12.23 AoC | ")

        val minPart1 = seeds.minOf { n -> mapTo(n) }
        print("part 1: $minPart1")

        val minPart2 = (seeds.indices step 2).map { i -> seeds[i]..<seeds[i]+seeds[i + 1] }
            .minOf { rg -> rg.minOf { n -> mapTo(n) } }
        println(", part 2: $minPart2")
    }
}
