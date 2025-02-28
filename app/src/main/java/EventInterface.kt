import fr.isen.larryjasontueno.isensmartcompanion.Event
import retrofit2.Call
import retrofit2.http.GET



interface EventInterface {
    @GET("events.json") // Remplacez par l’endpoint réel de votre API
    fun getEvents(): Call<List<Event>>
}