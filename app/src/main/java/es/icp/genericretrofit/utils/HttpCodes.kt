package es.icp.genericretrofit.utils

object HttpCodes {
    const val OK = 200
    const val CREATED = 201
    const val ACCEPTED = 202
    const val NOT_CONTENT = 204

    const val BAD_REQUEST = 400
    const val UNAUTHORIZED = 401
    const val NOT_FOUND = 404
    const val REQUEST_TIMEOUT = 408
    const val PRECONDITION_FAILED = 412

    const val INTERNAL_SERVER_ERROR = 500
    const val NOT_IMPLEMENTED = 501
    const val BAD_GATEWAY = 502
    const val SERVICE_UNAVAILABLE = 503

    const val ERROR_WITH_SOUND = 700
    const val ERROR_WITH_VIBRATE = 701
    const val ERROR_WITH_SOUND_VIBRATE = 702

    const val ERROR_NO_INTERNET = -1
    const val ERROR_IO_EXCEPTION = -2
    const val ERROR_204_NOT_CONTENT = -3

}