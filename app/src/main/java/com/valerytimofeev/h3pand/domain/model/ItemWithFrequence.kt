package com.valerytimofeev.h3pand.domain.model

data class ItemWithFrequence(
    val value: Int,
    val frequency: Int
) {
    companion object {
        /**
         * List of special value/frequence
         */
        val specialItemsWithFrequence = listOf(
            //Random artifacts
            ItemWithFrequence(2000, 150),
            ItemWithFrequence(5000, 150),
            ItemWithFrequence(10000, 150),
            ItemWithFrequence(20000, 150),
            //Prisons
            ItemWithFrequence(2500, 30),
            ItemWithFrequence(5000, 30),
            ItemWithFrequence(10000, 30),
            ItemWithFrequence(20000, 30),
            ItemWithFrequence(30000, 30),
            //Seer`s hut
            //Gold
            ItemWithFrequence(2000, 10),
            ItemWithFrequence(5333, 10),
            ItemWithFrequence(8666, 10),
            ItemWithFrequence(12000, 10),
            //Exp.
            ItemWithFrequence(2000, 10),
            ItemWithFrequence(5333, 10),
            ItemWithFrequence(8666, 10),
            ItemWithFrequence(12000, 10),
        )
    }
}
