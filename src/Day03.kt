// (C) 2023 A.Voß, a.voss@fh-aachen.de, kotlin@codebasedlearning.dev
// for advent of code 2023

// solutions part 1: 525911, part 2: 75805607

const val digits = "0123456789"
const val nops = "."

data class NumberInfo(val x: Int, val y: Int, val number: Int, val length: Int)
data class SymbolInfo(val x: Int, val y: Int, val symbol: Char)

class MachineInfos(
    val numbers: MutableList<NumberInfo> = mutableListOf(),
    val symbols: MutableList<SymbolInfo> = mutableListOf()
) {
    private var maxX: Int = -1
    private var maxY: Int = -1

    // analyze line, find numbers and symbols
    fun updateFrom(lineNo: Int, line: String) = this.apply {
        if (maxX == -1) maxX = line.length
        if (lineNo > maxY) maxY = lineNo
        var pos = 0
        while (pos < line.length) {
            when(val c = line[pos]) {
                in nops -> ++pos
                in digits -> {
                    var cnt = pos
                    while (cnt < line.length && line[cnt] in digits)
                        ++cnt
                    numbers.add(NumberInfo(x = pos,y = lineNo,
                        number = line.substring(pos..< cnt).toInt(), length = cnt-pos))
                    pos = cnt
                }
                else -> {
                    symbols.add(SymbolInfo(x = pos, y = lineNo, symbol = c))
                    ++pos
                }
            }
        }
    }

    // find adjacent symbols
    fun isSymbolAdjacent(y:Int, x:Int, len:Int, sc:Char=Char.MIN_VALUE):Pair<Boolean,SymbolInfo?> {
        fun check(y:Int,x:Int):Pair<Boolean,SymbolInfo?> {
            if (x<0 || x>=maxX || y<0 || y>=maxY) return false to null
            for (sym in symbols) {
                if (sc==Char.MIN_VALUE && sym.y==y && sym.x==x)
                    return true to sym
                if (sc==sym.symbol && sym.y==y && sym.x==x)
                    return true to sym
            }
            return false to null
        }

        fun checks() = sequence {
            yield(check(y,x-1))
            yield(check(y,x+len))
            for (i in (x-1..x+len))
                yield(check(y-1,i))
            for (i in (x-1..x+len))
                yield(check(y+1,i))
        }

        for (check in checks())
            if (check.first) return check

        return false to null
    }
}

fun main() {
    sequenceOfIndexed(filename = "Day03.txt").fold(MachineInfos()) {
            cc, (lineNo, line) ->  cc.updateFrom(lineNo, line)
    }.apply {

        // part 1
        val sumNumbers = numbers
            .filter { isSymbolAdjacent(y=it.y,x=it.x,it.length).first }
            .sumOf { it.number }

        // part 2
        val sumStars = mutableMapOf<Pair<Int,Int>,MutableList<Int>>().also { stars ->
            for (numInfo in numbers) {
                val (check, symInfo) = isSymbolAdjacent(y = numInfo.y, x = numInfo.x, numInfo.length, '*')
                if (check && symInfo != null)
                    stars.getOrPut(Pair(symInfo.x, symInfo.y)) { mutableListOf() }.add(numInfo.number)
            }
        }.values.filter { it.size==2 }.sumOf { it[0]*it[1]}

        println("02.12.23 AoC | part 1: $sumNumbers, part 2: $sumStars")
    }
}
