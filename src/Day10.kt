// (C) 2023 A.Voß, a.voss@fh-aachen.de, kotlin@codebasedlearning.dev
// for advent of code 2023

// solutions part 1: 6682, part 2: 353

/*

    This snippet is far from optimized... however, there was little time on that day.

 */

data class XY(val x:Int, val y:Int)

fun main() {
    val pipes = readTextfile(filename = "Day10.txt").filter { it.trim().isNotBlank() }.toMutableList()

    // assume all lines have the same length and 'S' exists only once
    val sizePipes = XY(pipes[0].length, pipes.size)
    println("pipes size $sizePipes")

    fun XY.valid()=(x in 0..<sizePipes.x && y in 0..<sizePipes.y)

    var xyS = XY(-1,-1)
    run searchS@ {
        pipes.forEachIndexed { y, line ->
            val x = line.indexOf('S')
            if (x >= 0) {
                xyS = XY(x,y)
                return@searchS
            }
        }
    }
    println("found 'S' at $xyS")

    // search valid neighbor of 'S' -> should be two
    fun nextToS(xy:XY):List<Pair<XY,Int>> {
        val list = mutableListOf<Pair<XY,Int>>()
        (-1..1).forEach { dy ->
            (-1..1).forEach { dx ->
                val xyc = XY(xy.x+dx,xy.y+dy)
                if (xyc.valid()) {
                    val cc = pipes[xyc.y][xyc.x]
                    if (dy==-1 && dx==0 && cc in "|7F")
                        list.add(xyc to 0) // up
                    if (dy==1 && dx==0 && cc in "|LJ")
                        list.add(xyc to 1) // dn
                    if (dy==0 && dx==-1 && cc in "-FL")
                        list.add(xyc to 2) // left
                    if (dy==0 && dx==+1 && cc in "-J7")
                        list.add(xyc to 3) // right
                }
            }
        }
        return list
    }

    // replace 'S' and find the clockwise way

    val loopStarts = nextToS(xyS)
    if (loopStarts.size!=2) throw RuntimeException("start pos next to 'S' not correct")

    val (typ1,typ2) = loopStarts[0].second to loopStarts[1].second
    val hiddenStartPipe = mapOf(
        0 to mapOf(1 to '|', 2 to 'J', 3 to 'L'),
        1 to mapOf(0 to '|', 2 to '7', 3 to 'F'),
        2 to mapOf(0 to 'J', 1 to '7', 3 to '-'),
        3 to mapOf(0 to 'L', 1 to 'F', 2 to '-'),
    )
    val pipeS = hiddenStartPipe[typ1]!![typ2]!!
    println("'S' replaced with '$pipeS'")
    pipes[xyS.y] = pipes[xyS.y].replace('S',pipeS)

    fun followFrom(xy:XY, xyOld:XY):XY {
        val (x,y) = xy
        val (xOld,yOld) = xyOld
        val c = pipes[y][x]
        if (xOld==x-1 && yOld==y) {
            if (c=='-') return XY(x+1,y)
            if (c=='7') return XY(x,y+1)
            if (c=='J') return XY(x,y-1)
        }
        if (xOld==x+1 && yOld==y) {
            if (c=='-') return XY(x-1,y)
            if (c=='F') return XY(x,y+1)
            if (c=='L') return XY(x,y-1)
        }
        if (xOld==x && yOld==y-1) {
            if (c=='|') return XY(x,y+1)
            if (c=='L') return XY(x+1,y)
            if (c=='J') return XY(x-1,y)
        }
        if (xOld==x && yOld==y+1) {
            if (c=='|') return XY(x,y-1)
            if (c=='7') return XY(x-1,y)
            if (c=='F') return XY(x+1,y)
        }
        throw RuntimeException("oops in $x $y")
    }
    val loopPaths = mutableListOf(mutableListOf(xyS), mutableListOf(xyS))
    val loopSet = mutableSetOf(xyS)

    // collect loops (assume, the path is connected)
    var part1 = -1
    loopStarts.forEachIndexed { loopIndex, firstPos ->
        var xy = firstPos.first
        var xyOld = xyS
        var steps = 0
        while (true) {
            loopPaths[loopIndex].add(xy)
            loopSet.add(xy) // twice
            steps += 1
            xy = followFrom(xy, xyOld.apply { xyOld = xy })
            if (xy==xyS) {
                steps += 1
                // println("loop $loopIndex, find 'S' after $steps -> max dist: ${steps/2}")
                if (part1==-1) part1=steps/2
                break
            }
        }
    }

    // search for the first outer element, start at col of 'S', because there is one
    val lx = xyS.x
    var ly = 0
    while (XY(lx,ly) !in loopSet)
        ++ly

    // the problem is that we do not know which path is clockwise, so we check the next elements
    // (not so elegant...)
    val xyStart0 = loopPaths[0].indexOf(XY(lx,ly))
    val xyNext0 = if (xyStart0+1<loopSet.size) xyStart0+1 else 0
    val xyStart1 = loopPaths[1].indexOf(XY(lx,ly))
    val xyNext1 = if (xyStart1+1<loopSet.size) xyStart1+1 else 0
    val xyNextToStart = when(pipes[ly][lx]) {
        '-' -> XY(lx+1,ly)
        'F' -> XY(lx+1,ly)
        '7' -> XY(lx,ly+1)
        else -> XY(-1,-1)
    }

    // determine the correct loop and the element in loop
    val (clockWise,xyStart)  = when (xyNextToStart) {
        loopPaths[0][xyNext0] -> 0 to xyStart0
        loopPaths[1][xyNext1] -> 1 to xyStart1
        else -> throw RuntimeException("cannot determine correct clockwise loop")
    }

    // println("clockwise loop is loop $clockWise at $xyStart")

    val curPath = loopPaths[clockWise]
    val pathLen = curPath.size

    // collect all 'inner' positions on clockwise loop

    val inFieldOnLoop = mutableSetOf<XY>()
    var right:Boolean? = null
    var down:Boolean? = null
    var isFirst = true
    (0..<pathLen).forEach { iAbs ->
        val i = (iAbs+xyStart) % pathLen
        val (x,y) = curPath[i]
        val c = pipes[y][x]
        val addPoints = when {
            (isFirst || right==true) && c=='-' -> listOf( XY(x,y+1) ).also { isFirst=false }
            (isFirst || right==true) && c=='7' -> listOf<XY>().also { right=null;down=true;isFirst=false }
            right==true && c=='J' -> listOf(XY(x,y+1), XY(x+1,y+1), XY(x+1,y)).also { right=null;down=false }
            right==false && c=='-' -> listOf( XY(x,y-1) )
            right==false && c=='L' -> listOf<XY>().also { right=null;down=false }
            right==false && c=='F' -> listOf(XY(x,y-1), XY(x-1,y-1), XY(x-1,y)).also { right=null;down=true }
            down==true && c=='|' -> listOf( XY(x-1,y) )
            down==true && c=='J' -> listOf<XY>().also { right=false;down=null }
            down==true && c=='L' -> listOf( XY(x-1,y), XY(x-1,y+1), XY(x,y+1)).also { right=true;down=null }
            down==false && c=='|' -> listOf( XY(x+1,y) )
            (isFirst || down==false) && c=='F' -> listOf<XY>().also { right=true;down=null;isFirst=false }
            down==false && c=='7' -> listOf( XY(x+1,y), XY(x+1,y-1), XY(x,y-1)).also { right=false;down=null }
            else -> listOf()
        }
        inFieldOnLoop.addAll(addPoints.filter { xy->xy.valid() && (xy !in loopSet) })
    }

    // flood inField
    val inField = mutableSetOf<XY>()
    while (inFieldOnLoop.isNotEmpty()) {
        val newPos = mutableSetOf<XY>()
        inFieldOnLoop.forEach { (x,y) ->
            (-1..1).forEach { dx ->
                (-1..1).forEach { dy ->
                    val xyNew = XY(x+dx,y+dy)
                    if (xyNew.valid()) {
                        if ((xyNew !in inFieldOnLoop) && (xyNew !in inField) && (xyNew !in loopSet)) {
                            newPos.add(xyNew)
                        }
                    }
                }
            }
            inField.add(XY(x,y))
        }
        inFieldOnLoop.clear()
        inFieldOnLoop.addAll(newPos)
    }
    val part2 = inField.size

    println("10.12.23 AoC | part 1: $part1, part 2: $part2")
}
