package com.android_learn.mynote.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tbl_note")
data class Note @Ignore constructor(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var title: String? = "",
    var description: String? = "",
    var time: String? = "",
    var date: String? = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    constructor(title: String, description: String, time: String, date: String) : this() {
        this.title = title
        this.description = description
        this.time = time
        this.date = date
    }
    constructor():this(0)

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }
}
