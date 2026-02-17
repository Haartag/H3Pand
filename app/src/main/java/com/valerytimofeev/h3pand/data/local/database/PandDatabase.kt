package com.valerytimofeev.h3pand.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [
        UnitItem::class,
        AdditionalValueItem::class,
        BoxValueItem::class,
        //UnitCharItem::class,
    ],
    version = 2,
    exportSchema = false
)

abstract class PandDatabase : RoomDatabase() {
    abstract fun pandDao(): PandDao
}