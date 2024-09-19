package com.note.mynote.data.remote


import com.note.mynote.constant.Constants.BASE_URL
import com.note.mynote.data.models.Note
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @POST(BASE_URL)
    suspend fun createNote(@Body note : Note) : Response<Note>

    @GET(BASE_URL)
    suspend fun getNotes() : Response<List<Note>>

    @GET("$BASE_URL{id}")
    suspend fun getNote(@Path("id") id : Int) : Response<Note>

    @PUT("$BASE_URL{id}/update")
    suspend fun updateNote(@Path("id") id : Int,@Body note : Note)

    @DELETE("$BASE_URL{id}")
    suspend fun deleteNote(@Path("id") id : Int)

    @DELETE(BASE_URL)
    suspend fun deleteAllNotes()

}