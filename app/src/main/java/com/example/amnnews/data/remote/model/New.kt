package com.example.amnnews.data.remote.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey


@Parcelize
@Entity(tableName = "news")
data class New(
    @PrimaryKey(autoGenerate = true) val idPk: Int = 0,  // Auto-generate the primary key

    val author: String?,
    val authors: List<String>?,
    val id: Int?,
    val image: String?,
    val language: String?,
    val publish_date: String?,
    val sentiment: Double?,
    val source_country: String?,
    val summary: String?,
    val text: String?,
    val title: String?,
    val url: String?
):Parcelable