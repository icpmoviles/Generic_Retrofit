package es.icp.pruebasretrofit.mockdata

import es.icp.genericretrofit.utils.GenericResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface MockService {
    @POST
    suspend fun postData(
        @Url url: String ,
        @Body mockreques: MockModel
    ) : GenericResponse<MockModel>

    @GET
    suspend fun getData(
        @Url url: String
    ) : GenericResponse<List<MockModel>>

}