package kotx.ktools

import kotlin.random.Random

fun randomInt(bound: Int) = Random(Random(0).nextInt(Random(0).nextInt(Int.MAX_VALUE))).nextInt(bound)