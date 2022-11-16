package com.valerytimofeev.h3pand.data.local.additional_data

enum class SpecialDwellings(
    val dwellingName: String,
    val units: List<SpecialDwellingUnit>,
) {
    ElementalConflux(
        dwellingName = "Elemental conflux",
        units = listOf(
            SpecialDwellingUnit("Air elemental", 356, 6, 9),
            SpecialDwellingUnit("Water elemental", 315, 6, 9),
            SpecialDwellingUnit("Fire elemental", 345, 5, 9),
            SpecialDwellingUnit("Earth elemental", 330, 4, 9),
        )
    ),
    GolemFactory(
        dwellingName = "Golem factory",
        units = listOf(
            SpecialDwellingUnit("Stone golem", 250, 6, 3),
            SpecialDwellingUnit("Iron golem", 412, 6, 3),
            SpecialDwellingUnit("Gold golem", 600, 3, 0),
            SpecialDwellingUnit("Diamond golem", 775, 2, 0),
        )
    )
}

data class SpecialDwellingUnit(
    val name: String,
    val unitValue: Int,
    val unitWeeklyGain: Int,
    val unitCastle: Int,
)