// (C) 2023 A.Voß, a.voss@fh-aachen.de, kotlin@codebasedlearning.dev
// for advent of code 2023

// solutions part 1: 25571, part 2: 8805731
fun main() {
    var sumPoints = 0
    val cardCounts = mutableMapOf<Int,Int>()     // remember the card count

    sequenceOfIndexed(filename = "Day04.txt").forEach { (lineNo,line) ->
        fun String.toIntSet() = this.split(' ').filter { it.trim().isNotBlank() }.map { it.toInt() }.toSet()

        val (drawnNumbers,myNumbers) = line.split('|').let { (drawnStr,myStr) ->
            drawnStr.split(':')[1].toIntSet() to myStr.toIntSet()
        }

        cardCounts[lineNo] = cardCounts.getOrPut(lineNo) { 0 } + 1

        val (points,cnt) = drawnNumbers.filter { num -> num in myNumbers }
            .fold(0 to 0) { (pts,ct), _ -> (if (pts==0) 1 else pts*2) to ct+1}

        sumPoints += points

        if (cnt>0) {
            // if you are lucky, add cards to card count
            val addCards = cardCounts[lineNo]!!
            (lineNo + 1..lineNo + cnt).forEach { i->
                cardCounts[i] = cardCounts.getOrPut(i) { 0 } + addCards
            }
        }
    }
    val sumCards = cardCounts.values.sum()
    println("04.12.23 AoC | part 1: $sumPoints, part 2: $sumCards")
}
