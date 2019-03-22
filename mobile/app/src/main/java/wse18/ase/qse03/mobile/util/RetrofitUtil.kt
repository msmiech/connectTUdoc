package wse18.ase.qse03.mobile.util

import com.fasterxml.jackson.databind.ObjectMapper
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.text.SimpleDateFormat
import okhttp3.OkHttpClient


object RetrofitUtil {

    var MEDCONNECT_BACKEND_URL = "https://192.168.43.245:8443"
    //const val MEDCONNECT_BACKEND_URL = "https://dev.smiech.at:8443" //"https://37.252.187.196:8443"
    const val FIREBASE_FUNCTION_URL = "https://us-central1-medconnect-5ac1b.cloudfunctions.net/api/"

    const val DATE_FORMAT = "yyyy-MM-dd HH:mm"

    @JvmStatic
    fun getMedconnectRetrofit(): Retrofit {

        val jacksonObjectMapper = ObjectMapper()
        jacksonObjectMapper.dateFormat = SimpleDateFormat(DATE_FORMAT)
        val conFactory = JacksonConverterFactory.create(jacksonObjectMapper)

        //The following methods allow communication with unverified hosts
        val httpClientBuilder = OkHttpClient().newBuilder()
        httpClientBuilder.hostnameVerifier { hostname, session -> true }
        val okHttpClient = httpClientBuilder.build()

        return Retrofit.Builder()
            .addConverterFactory(conFactory)
            .client(okHttpClient)
            .baseUrl(MEDCONNECT_BACKEND_URL)
            .build()
    }

    @JvmStatic
    fun getFirebaseFunctionRetrofit(): Retrofit {

        val jacksonObjectMapper = ObjectMapper()
        val conFactory = JacksonConverterFactory.create(jacksonObjectMapper)

        return Retrofit.Builder()
                .addConverterFactory(conFactory)
                .baseUrl(FIREBASE_FUNCTION_URL)
                .build()
    }
}
