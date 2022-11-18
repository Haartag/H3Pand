package com.valerytimofeev.h3pand.data.local.additional_data


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

    SettingsItemListHint(
        TextWithLocalization(
            "Group the awards tabs",
            "Группировать вкладки наград"
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

    SpecifyGuardDialogTitleText(
        TextWithLocalization(
            "Exact number of the guards",
            "Точное количество охраны"
        )
    ),

    ItemGuard(TextWithLocalization("Guard: %s", "Охрана: %s")),
    ItemMostLikely(TextWithLocalization("most likely %s", "скорее всего %s")),

    TileNameExp(TextWithLocalization("Experience", "Опыт")),
    TileNameGold(TextWithLocalization("Gold", "Золото")),
    TileNameSpell(TextWithLocalization("Spells", "Заклинания")),
    TileNameUnit(TextWithLocalization("Creatures", "Существа")),

    AboutTitle(TextWithLocalization("About", "О приложении")),
    AboutWhatsNew(TextWithLocalization("What's new in this version:", "Что нового в этой версии:")),
    AboutPointOne(
        TextWithLocalization(
            "– Increased accuracy in calculation the contents of the boxes for " +
                    "weeks after the first.\n",
            "– Увеличена точность определения содержимого коробок для недель после первой.\n"
        )
    ),
    AboutPointTwo(
        TextWithLocalization(
            "– Now, when selecting the exact number of guards, for the contents of " +
                    "the box is shown all the available range of guards for it.\n",
            "– Теперь при выборе точного количества охраны, для содержимого " +
                    "коробки показывается весь доступный для неё диапазон охран.\n"
        )
    ),
    AboutPointThree(
        TextWithLocalization(
            "– Jebus Cross L (medium guard power) is available in test mode.\n",
            "– В тестовом режиме доступен Jebus Cross L (средняя сила охраны).\n"
        )
    ),

    HelpTitle(TextWithLocalization("Description", "Описание")),
    ContactTitle(TextWithLocalization("Contacts", "Контакты"))

}