package com.ryanconway.spaceflightnews.data.api.retrofit

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object SpaceFlightApi {

    inline fun <reified T> getService(): T {
        val moshiFactory = MoshiConverterFactory.create()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://test.spaceflightnewsapi.net/")
            .addConverterFactory(moshiFactory)
            .build()
        return retrofit.create(T::class.java)
    }
}