package com.valerytimofeev.h3pand.data.additional_data


data class TextWithLocalization(
    val enText: String,
    val ruText: String
)

enum class TextStorage(
    val text: TextWithLocalization
) {

    MapTitle(TextWithLocalization("Choose map", "Выбор карты")),

    SettingsTitle(TextWithLocalization("Settings", "Настройки")),
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

    SheetChooseGuard(TextWithLocalization("Choose a guard\n", "Выбрать охрану\n")),
    SheetTotalValue(TextWithLocalization("Total value:", "Ценность:")),
    SheetWeekAndMonth(TextWithLocalization("month: %s, week: %s", "месяц: %s, неделя: %s")),
    SheetNumberOfTownZones(
        TextWithLocalization(
            "Same zones: %s",
            "Одинаковые зоны: %s"
        )
    ),
    SheetZoneName(TextWithLocalization("Type of zone: %s", "Тип зоны: %s")),
    SheetZoneTownName(TextWithLocalization("Town of zone:", "Город зоны:")),

    DialogSearch(TextWithLocalization("Search", "Поиск")),

    ItemGuard(TextWithLocalization("Guard: %s", "Охрана: %s")),
    ItemMostLikely(TextWithLocalization("most likely %s", "скорее всего %s")),

    AboutTitle(TextWithLocalization("About", "О приложении")),
    AboutWhatsNew(TextWithLocalization("What's new in this version:", "Что нового в этой версии:")),
    AboutPointOne(
        TextWithLocalization(
            "– Brand new version with Jebus Cross map support.\n",
            "– Совершенно новая версия с поддержкой карты Jebus Cross.\n"
        )
    ),

    HelpTitle(TextWithLocalization("Description", "Описание")),
    ContactTitle(TextWithLocalization("Contacts", "Контакты"))

}