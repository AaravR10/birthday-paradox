import kotlin.random.Random

fun hash16(message: String): Int {
    var hash = 5381L
    for (char in message) {
        hash = ((hash shl 5) + hash) + char.code
    }
    return (hash and 0xFFFF).toInt()
}
/*
fun hash16BitAlpha(message: String): Int{
    var hash = 0
    for (char in message){
        hash = (hash * 31) + char.code
        hash = hash % 65536
    }
    return hash
}
*/
fun main() {
    val baseGoodMessage = "I, Alice, am a pacifist"
    val baseBadMessage = "I, Alice, am an anarchist"
    val goodMessageHashes = HashMap<Int, String>()

    println("--- Birthday Attack Simulation (Custom 16-bit Hash) ---")
    println("Target space size: 65536 possible values")

    var goodAttempts = 0
    while (goodMessageHashes.size < 2000) {
        val variant = "$baseGoodMessage [ID: $goodAttempts]"
        val hash = hash16(variant)
        goodMessageHashes[hash] = variant
        goodAttempts++
    }
    println("Stored ${goodMessageHashes.size} unique variations of the GOOD message.")
    println("\nSearching for a bad message variation that yields a colliding hash...")

    var badAttempts = 0
    var collisionFound = false

    while (!collisionFound) {
        badAttempts++
        val badVariant = "$baseBadMessage [ID: ${Random.nextInt(1, 1000000)}]"
        val badHash = hash16(badVariant)

        if (goodMessageHashes.containsKey(badHash)) {
            val matchingGoodMessage = goodMessageHashes[badHash]
            println("\n[!] SUCCESS: Collision found after $badAttempts bad message attempts!")
            println("================================================================")
            println("Good Message Alice signs:\n\"$matchingGoodMessage\"")
            println("Hash Value: $badHash")
            println("----------------------------------------------------------------")
            println("Bad Message Bob exchanges:\n\"$badVariant\"")
            println("Hash Value: $badHash")
            println("================================================================")
            collisionFound = true
        }
    }
}
