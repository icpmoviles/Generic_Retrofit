package es.icp.pruebasretrofit

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import es.icp.genericretrofit.communication.ConnectivityInterceptor
import es.icp.genericretrofit.communication.RetrofitBase
import es.icp.genericretrofit.utils.onError
import es.icp.genericretrofit.utils.onException
import es.icp.genericretrofit.utils.onSuccess
import es.icp.pruebasretrofit.databinding.FragmentFirstBinding
import es.icp.pruebasretrofit.mockdata.MockModel
import es.icp.pruebasretrofit.mockdata.MockService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.create
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val service by lazy {
        val retrofit = RetrofitBase.getInstance(
            baseUrl = "https://63b54fad0f49ecf508a0abcb.mockapi.io/mock/",
            client =
            OkHttpClient.Builder().apply {
                interceptors().add(ConnectivityInterceptor(requireContext()))
                readTimeout(1, TimeUnit.MINUTES)
                writeTimeout(1, TimeUnit.MINUTES)
                connectTimeout(1, TimeUnit.MINUTES)
            }.build()
        )
        retrofit.create<MockService>()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val request = MockModel("5550", Date(), "2")
                service.postData(url = "prueba", mockreques = request)
                    .onSuccess { body, headers ->
                        Log.w("main onSucces", it.toString())

                    }
                    .onError { code, message ->
                        Log.w("main on error", "code: $code, mensaje: $message")
                    }
                    .onException { e: Throwable ->
                        Log.w("main on Execption", e.message ?: "Error desconocido")

                    }

//                service.getData(url = "prueba")
//                    .onSuccess {
//                        Log.w("main onSucces", it.toString())
//                    }
//                    .onError { code, message ->
//                        Log.w("main on error", "code: $code, mensaje: $message")
//                    }
//                    .onException { e: Throwable ->
//                        Log.w("main on Execption", e.message ?: "Error desconocido")
//
//                    }
            }



        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}