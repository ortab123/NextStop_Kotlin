package com.example.final_nextstop.data.remote_db

import android.content.Context
import com.example.final_nextstop.R
import com.example.final_nextstop.util.Resource
import retrofit2.Response
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

open class BaseDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        return try {
            val result = call()
            if (result.isSuccessful) {
                val body = result.body()
                if (body != null) return Resource.success(body)
            }
            Resource.error(
                context.getString(R.string.network_call_has_failed_for_the_following_reason) +
                        "${result.message()} ${result.code()}"
            )
        } catch (e: Exception) {
            Resource.error(
                context.getString(R.string.network_call_has_failed_for_the_following_reason) +
                        (e.localizedMessage ?: e.toString())
            )
        }
    }
}
