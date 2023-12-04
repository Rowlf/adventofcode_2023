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

/** Same as above, returns a sequence of line numbers and lines */
fun sequenceOfIndexed(filename: String) = sequence {
    Path("src/data/$filename")
        .toFile()
        .bufferedReader(Charsets.UTF_8)
        .use {
            val iter = it.lineSequence().iterator()
            var lineNo = 0
            while (iter.hasNext()) {
                yield( (lineNo++) to iter.next())
            }
        }
}

// fun readTextfile(filename: String) = Path("src/data/$filename").readLines()
