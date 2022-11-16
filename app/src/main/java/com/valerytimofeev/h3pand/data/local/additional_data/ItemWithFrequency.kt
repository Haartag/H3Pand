package com.valerytimofeev.h3pand.data.local.additional_data

/**
 * Pair of value and frequency of items to determine the chance of dropping boxes.
 * Contains items with variable value not represented in the database and some other things.
 */
data class ItemWithFrequency(
    val value: Int,
    val frequency: Int
) {
    companion object {
        /**
         * List of special value/frequency
         */
        val specialItemsWithFrequency = listOf(
            //Random artifacts
            ItemWithFrequency(2000, 150),
            ItemWithFrequency(5000, 150),
            ItemWithFrequency(10000, 150),
            ItemWithFrequency(20000, 150),
            //Prisons
            ItemWithFrequency(2500, 30),
            ItemWithFrequency(5000, 30),
            ItemWithFrequency(10000, 30),
            ItemWithFrequency(20000, 30),
            ItemWithFrequency(30000, 30),
            //Seer`s hut
            //Gold
            ItemWithFrequency(2000, 10),
            ItemWithFrequency(5333, 10),
            ItemWithFrequency(8666, 10),
            ItemWithFrequency(12000, 10),
            //Exp.
            ItemWithFrequency(2000, 10),
            ItemWithFrequency(5333, 10),
            ItemWithFrequency(8666, 10),
            ItemWithFrequency(12000, 10),
        )
    }
}
