package es.icp.genericretrofit.communication

import android.util.Log
import es.icp.genericretrofit.communication.RetrofitBase.mGson
import es.icp.genericretrofit.models.NetworkResponse
import es.icp.genericretrofit.utils.HttpCodes
import es.icp.genericretrofit.utils.HttpCodes.ACCEPTED
import es.icp.genericretrofit.utils.HttpCodes.NOT_CONTENT
import es.icp.genericretrofit.utils.HttpCodes.OK
import es.icp.genericretrofit.utils.TAG
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.*
import java.io.IOException


internal class NetworkResponseCall<S: Any, E: Any> (
    private val delegate: Call<S>,
    private val errorConverter: Converter<ResponseBody, E>
        ): Call<NetworkResponse<S, E>> {


    override fun enqueue(callback: Callback<NetworkResponse<S, E>>) {

        return delegate.enqueue(object : Callback<S> {
            override fun onResponse(call: Call<S>, response: Response<S>) {
                val body = response.body()
                val code = response.code()
                val error = response.errorBody()
                Log.w("$TAG CALL", response.toString())
                Log.w("$TAG BODY", mGson.toJson(body))


                if (response.isSuccessful){
                    when (code) {
                         in OK..ACCEPTED -> {
                             body?.let {
                                     callback.onResponse(
                                     this@NetworkResponseCall,
                                     Response.success(NetworkResponse.Success(it))
                                 )
                            }
                        }
                        NOT_CONTENT -> {
                            callback.onResponse(
                                this@NetworkResponseCall,
                                Response.success(NetworkResponse.UnknownError(Error("El recurso solicitado no ha devuelto contenido.")))
                            )
                        }

                        else -> callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(NetworkResponse.HttpError(code, response.headers()["message"] ?: response.message() ))
                        )

                    }
                } else {
                    callback.onResponse(
                        this@NetworkResponseCall,
                        Response.success(NetworkResponse.HttpError(code,  response.headers()["message"] ?: response.message()))
                    )
                }

            }

            override fun onFailure(call: Call<S>, throwable: Throwable) {
                Log.w("$TAG ONFAILURE",call.request().toString())
                throwable.printStackTrace()
                val networkResponse = when (throwable) {
                    is NoInternetException -> NetworkResponse.NetworkError(
                        error = throwable,
                        code = HttpCodes.ERROR_NO_INTERNET
                    )
                    is IOException ->
                        NetworkResponse.NetworkError(
                            error = throwable,
                            code = HttpCodes.ERROR_IO_EXCEPTION
                        )
                    else -> NetworkResponse.UnknownError(throwable)
                }
                callback.onResponse(this@NetworkResponseCall, Response.success(networkResponse))
            }
        })
    }

    override fun isExecuted() = delegate.isExecuted

    override fun clone() = NetworkResponseCall( delegate.clone(), errorConverter)

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<NetworkResponse<S, E>> {
        throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")
    }

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}