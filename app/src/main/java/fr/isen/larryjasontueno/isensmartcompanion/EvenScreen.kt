package fr.isen.larryjasontueno.isensmartcompanion

import android.content.Intent
import android.media.tv.SectionResponse
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import fr.isen.larryjasontueno.isensmartcompanion.ui.theme.ISENSmartCompanionTheme
import java.io.Serializable
import kotlin.math.E
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call

data class Event(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val category: String,
    val imageRes: Int
): Serializable



@Composable
fun EventsScreen(){
    var events by remember { mutableStateOf<List<Event>>(emptyList()) }
    //var events = remember { mutableStateListOf<Event>() }
    LaunchedEffect(Unit) {
        fetchEvents { fetchedEvents ->
            println("fetched data:  $fetchedEvents")
            events = fetchedEvents
            println("fetched events: $events")
        }
    }


    Box(modifier = Modifier
        .padding(top = 50.dp)
        .fillMaxSize()
        .background(Color.White))
    {
        LazyColumn (
            modifier = Modifier.fillMaxSize()
        ) {
            items(events) {
                Event -> EventItem(Event)
            }
        }
        FloatingActionButton(
            onClick = {

            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 100.dp, end = 50.dp)
                .width(100.dp)
                .height(50.dp)
        ){
            Icon(painter = painterResource(id = R.drawable.add), contentDescription = "Add element")
            Text(text = "Ajouter", color = Color.Black)
        }
    }
}



@Composable
fun EventItem(event: Event){
    val context = LocalContext.current
    Card (
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .clickable {
                val intent = Intent(context, EventDetail::class.java).apply {
                    putExtra("event", event)
                }
                context.startActivity(intent)
            }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ){
//            Image(
//                painter = painterResource(id = event.imageRes),
//                contentDescription = event.title,
//                modifier = Modifier
//                    .size(80.dp)
//                    .clip(RoundedCornerShape(8.dp)),
//                contentScale = ContentScale.Crop
//            )
            Spacer(modifier = Modifier.width(16.dp))
            Column (
                modifier = Modifier.weight(1f)
            ) {
                Text(text = event.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = event.description, fontSize = 14.sp, color = Color.Gray)
                Text(text = "📅 ${event.date}", fontSize = 12.sp, color = Color.White)
                Text(text = "📍 ${event.location}", fontSize = 12.sp, color = Color.White)
                Text(text = "🎭 ${event.category}", fontSize = 12.sp, color = Color.White)
            }
        }
    }
}

//@Composable
//fun Greeting2(name: String, modifier: Modifier = Modifier) {
//    val context = LocalContext.current
//    Box(modifier = Modifier.fillMaxSize() ){
//        Column (
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(top = 200.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//        }
//    }
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview2() {
//    ISENSmartCompanionTheme {
//        Greeting2("Android")
//    }
//}