package es.icp.genericretrofit.communication

import java.io.IOException

class NoInternetException : IOException() {

    override val message: String
        get() = "No dispones de conexión a internet."
}