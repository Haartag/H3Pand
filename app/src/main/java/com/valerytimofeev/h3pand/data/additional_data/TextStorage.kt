package com.valerytimofeev.h3pand.data.additional_data


data class TextWithLocalization(
    val enText: String,
    val ruText: String
)

enum class TextStorage(
    val text: TextWithLocalization
) {
    MapTitle(TextWithLocalization("Choose map", "Выбор карты")),

    SettingsLanguageHint(
        TextWithLocalization(
            "Select language for application:",
            "Выберите язык для приложения:"
        )
    ),
    SettingsLanguageEng(TextWithLocalization("English", "Английский")),
    SettingsLanguageRus(TextWithLocalization("Russian", "Русский")),

    SettingsLanguageSandboxText(
        TextWithLocalization(
            "To change the language, you need to restart the application.",
            "Чтобы изменить язык, необходимо перезапустить приложение."
        )
    ),
    SettingsLanguageSandboxButton(
        TextWithLocalization(
            "Apply and restart",
            "Применить и перезапустить"
        )
    ),

    SheetTotalValue(TextWithLocalization("Total value:", "Общая ценность:")),
    SheetWeekAndMonth(TextWithLocalization("month: %s, week: %s", "месяц: %s, неделя: %s")),
    SheetNumberOfTownZones(
        TextWithLocalization(
            "Number of town's zones: %s",
            "Количество зон города: %s"
        )
    ),
    SheetZoneName(TextWithLocalization("Type of zone: %s", "Тип зоны: %s")),
    SheetZoneTownName(TextWithLocalization("Town of zone: %s", "Город зоны: %s")),

    DialogSearch(TextWithLocalization("Search", "Поиск")),

    ItemGuard(TextWithLocalization("Guard: %s,", "Охрана: %s,")),
    ItemMostLikely(TextWithLocalization("most likely %s", "вероятнее всего %s"))

}