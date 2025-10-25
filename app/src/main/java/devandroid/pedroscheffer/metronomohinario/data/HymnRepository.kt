package devandroid.pedroscheffer.metronomohinario.data

object HymnRepository {
    private val hymnMap: Map<Int, HymnData> = mapOf(
        1 to HymnData(1, "4/4", 76),
        2 to HymnData(2, "4/4", 80),
        330 to HymnData(330, "2/4", 77)
        // Adicione os outros hinos aqui
    )

    fun getHymnData(hymnNumber: Int): HymnData? {
        return hymnMap[hymnNumber]
    }
}