package com.example.players.data

/** Моковый репозиторий. Позже здесь появится сетевой запрос. */
object PlayerRepository {

    private val spots = listOf(
        FieldSpot(0.43f, 0.13f),
        FieldSpot(0.10f, 0.45f),
        FieldSpot(0.30f, 0.45f),
        FieldSpot(0.64f, 0.45f),
        FieldSpot(0.43f, 0.85f, isCurrent = true)
    )

    private val players = listOf(
        mock(1, "Alexey Ionov", 11, 35, "Полузащитник", PlayerStats(4, 1, 1, 1, 0)),
        mock(2, "Erik Bicfalvi", 10, 35, "Полузащитник", PlayerStats(6, 2, 3, 0, 0)),
        mock(3, "Yuriy Zheleznov", 14, 31, "Защитник", PlayerStats(5, 0, 1, 2, 0)),
        mock(4, "Andrey Egorychev", 5, 33, "Защитник", PlayerStats(7, 1, 0, 3, 1)),
        mock(5, "Egor Mosin", 42, 24, "Вратарь", PlayerStats(3, 0, 0, 0, 0)),
        mock(6, "Ivan Petrov", 7, 27, "Нападающий", PlayerStats(8, 5, 2, 1, 0)),
        mock(7, "Dmitry Sokolov", 9, 29, "Нападающий", PlayerStats(9, 6, 1, 2, 0)),
        mock(8, "Sergey Volkov", 3, 30, "Защитник", PlayerStats(10, 0, 2, 4, 0)),
        mock(9, "Nikita Orlov", 21, 22, "Полузащитник", PlayerStats(6, 1, 4, 0, 0)),
        mock(10, "Pavel Smirnov", 1, 32, "Вратарь", PlayerStats(10, 0, 0, 0, 0)),
        mock(11, "Artem Kuznetsov", 17, 25, "Полузащитник", PlayerStats(7, 2, 2, 1, 0)),
        mock(12, "Maxim Lebedev", 4, 28, "Защитник", PlayerStats(8, 1, 0, 3, 1)),
        mock(13, "Oleg Morozov", 19, 23, "Нападающий", PlayerStats(5, 3, 0, 0, 0)),
        mock(14, "Kirill Fedorov", 8, 26, "Полузащитник", PlayerStats(9, 2, 5, 2, 0)),
        mock(15, "Anton Belov", 23, 34, "Защитник", PlayerStats(4, 0, 0, 1, 0))
    )

    private fun mock(
        id: Int, name: String, number: Int, age: Int, position: String, stats: PlayerStats
    ) = Player(
        id = id,
        name = name,
        number = number,
        age = age,
        position = position,
        team = "ФК Урал Екатеринбург",
        socialUrl = "https://vk.com",
        resumeUrl = "https://example.com/resume/$id",
        stats = stats,
        fieldSpots = spots,
        comments = listOf(
            "Отличный матч, много движения.",
            "Нужно лучше играть в обороне."
        )
    )

    fun getPlayers(): List<Player> = players

    fun getPlayer(id: Int): Player? = players.firstOrNull { it.id == id }
}
