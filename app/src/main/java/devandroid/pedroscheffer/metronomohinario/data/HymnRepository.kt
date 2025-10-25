package devandroid.pedroscheffer.metronomohinario.data

object HymnRepository {
    private val hymnMap: Map<Int, HymnData> = mapOf(
        1 to HymnData(1, "Hino da Esperança", "4/4", 76, 60, 92),
        2 to HymnData(2, "Hino da Alegria", "4/4", 80, 70, 90),
        3 to HymnData(3, "Hino da Paz", "4/4", 68, 50, 85),
        4 to HymnData(4, "Hino do Louvor", "6/4", 125, 100, 150),
        330 to HymnData(330, "Hino 330", "2/4", 77, 65, 90)
    )

    fun getHymnData(hymnNumber: Int): HymnData? {
        return hymnMap[hymnNumber]
    }
}