package com.note.mynote.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tbl_note")
data class Note(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "title", defaultValue = "")
    var title: String? = "",
    @ColumnInfo(name = "description", defaultValue = "")
    var description: String? = "",
    @ColumnInfo(name = "time", defaultValue = "")
    var time: String? = "",
    @ColumnInfo(name = "date", defaultValue = "")
    var date: String? = ""
) : Parcelable {
    constructor(title: String, description: String, time: String, date: String) : this() {
        this.title = title
        this.description = description
        this.time = time
        this.date = date
    }
}
