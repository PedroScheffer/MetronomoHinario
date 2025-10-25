package devandroid.pedroscheffer.metronomohinario.data

object HymnRepository {
    private val hymnMap: Map<Int, HymnData> = mapOf(
        1 to HymnData(1, "Cristo, meu Mestre...", "4/4", 76, 60, 92),
        2 to HymnData(2, "De Deus tu és eleita", "4/4", 80, 70, 90),
        3 to HymnData(3, "Faz-nos ouvir Tua voz", "4/4", 68, 50, 85),
        4 to HymnData(4, "Ouve a nossa oração", "6/4", 125, 100, 150),
        330 to HymnData(330, "Ao findar a jornada", "4/4", 77, 66, 88)
    )

    fun getHymnData(hymnNumber: Int): HymnData? {
        return hymnMap[hymnNumber]
    }
}