// (C) 2023 A.Voß, a.voss@fh-aachen.de, kotlin@codebasedlearning.dev
// for advent of code 2023

// solutions part 1 sumId: 2061, part 2 sumPower: 72596

enum class Colors(val color: String) { RED("red"), GREEN("green"), BLUE("blue") }

class ColorCounter(map: Map<Colors,Int> = Colors.entries.associateWith { 0 })
    : MutableMap<Colors,Int> by map.toMutableMap(), Comparable<ColorCounter>
{
    // e.g. "12 red, 13 green, 14 blue"
    constructor(game: String) : this() {
        game.split(",").forEach { part -> keys.forEach { enum ->
            if (part.endsWith(enum.color)) this[enum] = part.removeSuffix(enum.color).trim().toInt()
        } }
    }

    // slightly incorrect, '<' is actual '<=' (we just need '<=')
    override fun compareTo(other: ColorCounter): Int =
        if (Colors.entries.all { enum -> (this[enum]!! <= other[enum]!!) }) -1 else +1

    override fun toString(): String = this.keys.joinToString { "$it:${this[it]}"}
}

data class Stats(var sumId: Int = 0, var sumPower: Int = 0)

fun main() {
    val counterLimit = ColorCounter("12 red, 13 green, 14 blue")

    sequenceOf(filename = "Day02.txt").fold(Stats()) { stats, line ->
        val (id,games) = line.split(":").let {
            it[0].removePrefix("Game").trim().toInt() to it[1].split(";").map { game -> ColorCounter(game) }
        }
        ColorCounter(map = Colors.entries.associateWith { enum -> games.maxBy { counter -> counter[enum]!! }[enum]!! })
            .let { counterMax -> Stats(
                stats.sumId + if (counterMax <= counterLimit) id else 0,
                stats.sumPower + counterMax.values.fold(1) { power, max -> power*max })
        }
    }.also { println("02.12.23 AoC | $it") }
}
