package devandroid.pedroscheffer.metronomohinario.data

data class HymnData(
    val number: Int,
    val name: String,
    val timeSignature: String,
    val averageBpm: Int,
    val minBpm: Int,
    val maxBpm: Int
)