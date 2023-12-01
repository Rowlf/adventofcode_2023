// (C) 2023 A.Voß, a.voss@fh-aachen.de, kotlin@codebasedlearning.dev
// for advent of code 2023

import kotlin.io.path.Path

/** Returns a text file (from src/data) as a sequence of lines, then closes the file. */
fun sequenceOf(filename: String) = sequence {
    Path("src/data/$filename")
        .toFile()
        .bufferedReader(Charsets.UTF_8)
        .use { yieldAll(it.lineSequence()) }
}
