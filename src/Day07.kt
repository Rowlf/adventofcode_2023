// (C) 2023 A.Voß, a.voss@fh-aachen.de, kotlin@codebasedlearning.dev
// for advent of code 2023

// solutions part 1: 249638405, part 2: 249776650
// example 6440 / 5905

typealias Hand = String

enum class HandValue(val value: Int) {
    FiveOfAKind(7), FourOfAKind(6), FullHouse(5), ThreeOfAKind(4), TwoPair(3), OnePair(2), HighCard(1)
}

fun Hand.toValue() = groupingBy { it }.eachCount().let { map ->
    when (map.size) {
        1 -> HandValue.FiveOfAKind.value
        2 -> map[this[0]]!!.let { n -> if (n==1 || n==4) HandValue.FourOfAKind.value else HandValue.FullHouse.value }
        3 -> if (map.values.max()==3) HandValue.ThreeOfAKind.value else HandValue.TwoPair.value
        4 -> HandValue.OnePair.value
        else -> HandValue.HighCard.value
    }
}

fun Hand.toValue(useJoker: Boolean)
    = if (!useJoker || !contains('J') || this=="JJJJJ")
        toValue()
    else
        toCharArray().filter { c -> c!='J' }.toSet()
            .maxOf { c -> replace('J',c).toValue() }

val card2ValueRegular = mapOf('A' to 14, 'K' to 13, 'Q' to 12, 'J' to 11, 'T' to 10, '9' to 9, '8' to 8, '7' to 7, '6' to 6, '5' to 5, '4' to 4, '3' to 3, '2' to 2)
val card2ValueWithJoker = mapOf('A' to 14, 'K' to 13, 'Q' to 12, 'T' to 10, '9' to 9, '8' to 8, '7' to 7, '6' to 6, '5' to 5, '4' to 4, '3' to 3, '2' to 2, 'J' to 1)

fun Hand.compareToHand(other: Hand, useJoker: Boolean):Int {
    other.toValue(useJoker).compareTo(toValue(useJoker)).also { if (it!=0) return it }  // switched
    val useMap = if (useJoker) card2ValueWithJoker else card2ValueRegular
    this.zip(other).forEach { (c1,c2) ->
        useMap[c2]!!.compareTo(useMap[c1]!!).also { if (it!=0) return it }              // switched
    }
    return 0
}

fun main() {
    sequenceOf(filename = "Day07.txt").fold(mutableListOf<Pair<String,Int>>()) { lst, line ->
        lst.apply { add(line.split(" ").let { (hand,bid) -> hand.trim() to bid.trim().toInt() }) }
    }.also { handWithBid ->
        print("07.12.23 AoC |")
        for (puzzle in 1..2) {
            val sum = handWithBid.sortedWith { hb1, hb2 -> hb1.first.compareToHand(hb2.first, useJoker = puzzle == 2) }
                .reversed().withIndex().sumOf { (i,hb) -> (hb.second)*(i+1) }
            print(" part $puzzle: $sum")
        }
        println()
    }
}
