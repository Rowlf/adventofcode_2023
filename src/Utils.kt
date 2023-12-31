// (C) 2023 A.Voß, a.voss@fh-aachen.de, kotlin@codebasedlearning.dev
// for advent of code 2023

import kotlin.io.path.Path
import kotlin.io.path.readLines

/** Returns a text file (from src/data) as a sequence of lines, then closes the file. */
fun sequenceOf(filename: String) = sequence {
    Path("src/data/$filename")
        .toFile()
        .bufferedReader(Charsets.UTF_8)
        .use { yieldAll(it.lineSequence()) }
}

fun readTextfile(filename: String) = Path("src/data/$filename").readLines()

class TextBlock(val filename: String, filterNotBlank:Boolean=true)
    : List<String> by (readTextfile(filename = filename)
    .filter { !filterNotBlank || it.trim().isNotBlank() })
{
    val rows get() = size
    val cols get() = this[0].length
}

fun String.toIntList() = this.split(' ').filter { it.trim().isNotBlank() }.map { it.toInt() }
fun String.toLongList() = this.split(' ').filter { it.trim().isNotBlank() }.map { it.toLong() }

