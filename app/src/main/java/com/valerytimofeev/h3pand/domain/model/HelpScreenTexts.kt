package com.valerytimofeev.h3pand.domain.model

import com.valerytimofeev.h3pand.R
import com.valerytimofeev.h3pand.data.additional_data.TextWithLocalization

enum class HelpScreenTexts(
    val img: Int,
    val imgDescription: String,
    val text: TextWithLocalization,
) {

    ChooseGuard(
        img = R.drawable.ic_dice,
        imgDescription = "Choose guard",
        text = TextWithLocalization(
            enText = "Pandora's box guard selection. If you want to set the exact number of guards, " +
                    "use a long press on the guard icon after selecting guards.",
            ruText = "Выбор охраны коробки пандоры. Если вы хотите задать точное количество охраны, " +
                    "после выбора охраны используйте долгое нажатие на иконку охраны."
        )
    ),

    AddValue(
        img = R.drawable.ic_question,
        imgDescription = "Additional value",
        text = TextWithLocalization(
            enText = "To determine the contents of the Pandora's box, you must specify all map " +
                    "objects located behind the same guard as the box.",
            ruText = "Для определения содержимого коробки Пандоры необходимо указать все объекты " +
                    "карты, расположенные за той-же охраной, что и коробка."
        )
    ),

    RandomResource(
        img = R.drawable.av_resource,
        imgDescription = "Resource",
        text = TextWithLocalization(
            enText = "Due to the nature of the GSC, any pile of resources on the map may turn out " +
                    "to be a \"random resource\" with a value of 1500. In this case, " +
                    "the calculation results may be inaccurate.",
            ruText = "Из-за особенностей ГСК любая кучка ресурсов на карте может оказаться " +
                    "\"случайным ресурсом\", имеющим ценность 1500. В этом случае результаты " +
                    "вычислений могут оказаться неточными."
        )
    ),

    QuestArtifact(
        img = R.drawable.av_artifact,
        imgDescription = "Resource",
        text = TextWithLocalization(
            enText = "On maps with allowed \"seer`s huts\" any treasure artifact (value 2000) that " +
                    "is not part of the combination artifact can be the target of the quest. In this case, the calculation results may be inaccurate.",
            ruText = "На картах с разрешённым \"хижинами провидца\" любой " +
                    "артефакт-сокровище(ценностью 2000), не являющийся частью сборного артефакта, " +
                    "может оказаться целью квеста. " +
                    "В этом случае результаты вычислений могут оказаться неточными."
        )
    ),

    Sliders(
        img = R.drawable.ic_slider,
        imgDescription = "Slider",
        text = TextWithLocalization(
            enText = "The sliders should set the type of this zone (e.g. start zone, center, etc.), " +
                    "as well as the expected number of zones with the same castle as in this zone",
            ruText = "Слайдерами необходимо задать тип этой зоны (например респ, центр и т.д.), " +
                    "а также предполагаемое количество зон с тем же замком, что и в этой зоне"
        )
    ),

/*    RandomResourceOld(
    img = R.drawable.av_resource,
    imgDescription = "Resource",
    text = TextWithLocalization(
    enText = "Due to the nature of the GSC, any pile of resources on the map may turn out " +
    "to be a \"random resource\" with a value of 1500. When selecting an additional " +
    "value, use a long press on any selected pile of resources to change the " +
    "specific resource/random resource selection.",
    ruText = "Из-за особенностей ГСК любая кучка ресурсов на карте может оказаться " +
    "\"случайным ресурсом\", имеющим ценность 1500. При выборе дополнительной " +
    "ценности используйте долгое нажатие по иконке любого ресурса, чтобы поменять " +
    "выбор конкретный ресурс / случайный ресурс."
    )
    )*/
}

