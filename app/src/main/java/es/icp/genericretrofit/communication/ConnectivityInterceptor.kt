package es.icp.genericretrofit.communication

import android.content.Context
import es.icp.genericretrofit.utils.NetworkUtil
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ConnectivityInterceptor(private val mContext: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!NetworkUtil.isOnline(mContext)) {
            throw NoInternetException()
        }
        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }

}