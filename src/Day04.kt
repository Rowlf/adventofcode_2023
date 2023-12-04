// (C) 2023 A.Voß, a.voss@fh-aachen.de, kotlin@codebasedlearning.dev
// for advent of code 2023

// solutions part 1: 25571, part 2: 8805731
fun main() {
    var sumPoints = 0
    val cardCounts = mutableMapOf<Int,Int>()     // remember the card count

    sequenceOfIndexed(filename = "Day04.txt").forEach { (lineNo,line) ->
        val lineSplit = line.split('|')
        val drawnSplit = lineSplit[0].split(':')
        val drawnNumbers = drawnSplit[1].split(' ').filter { it.trim().isNotBlank() }.map { it.toInt() }.toSet()
        val myNumbers = lineSplit[1].split(' ').filter { it.trim().isNotBlank() }.map { it.toInt() }.toSet()

        cardCounts[lineNo] = cardCounts.getOrPut(lineNo) { 0 } + 1

        var points = 0
        var cnt = 0
        drawnNumbers.forEach { no ->
            if (no in myNumbers) {
                points = if (points==0) 1 else points*2
                ++cnt
            }
        }
        sumPoints += points

        if (cnt>0) {
            // if you are lucky, add cards to card count
            for (j in 0..<cardCounts[lineNo]!!) {
                for (i in lineNo + 1..lineNo + cnt)
                    cardCounts[i] = cardCounts.getOrPut(i) { 0 } + 1
            }
        }
    }
    val sumCards = cardCounts.values.sum()
    println("04.12.23 AoC | calibration value: $sumPoints $sumCards")
}
