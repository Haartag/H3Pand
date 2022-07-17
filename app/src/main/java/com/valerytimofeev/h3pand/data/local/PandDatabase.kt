package com.valerytimofeev.h3pand.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [GuardItem::class, AdditionalValueItem::class],
    version = 1,
    exportSchema = false
)

abstract class PandDatabase: RoomDatabase() {
    abstract fun pandDao(): PandDao
}