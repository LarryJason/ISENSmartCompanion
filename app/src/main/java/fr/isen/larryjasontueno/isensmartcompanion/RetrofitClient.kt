package fr.isen.larryjasontueno.isensmartcompanion
import EventInterface
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


object RetrofitClient {
    private const val BASE_URL = "https://isen-smart-companion-default-rtdb.europe-west1.firebasedatabase.app/"

    private val gson = GsonBuilder()
        .setLenient() // Accepte les JSON mal formés
        .create()

    val instance: EventInterface by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)) // Utilisation de Gson tolérant
            .build()
            .create(EventInterface::class.java)
    }
}