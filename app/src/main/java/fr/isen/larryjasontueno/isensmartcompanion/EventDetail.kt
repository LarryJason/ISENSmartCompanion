package fr.isen.larryjasontueno.isensmartcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.larryjasontueno.isensmartcompanion.ui.theme.ISENSmartCompanionTheme


class EventDetail : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val event = intent.getSerializableExtra("event") as? Event
        enableEdgeToEdge()
        setContent {
            ISENSmartCompanionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if(event != null)
                    EventDetail(
                        event = event,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun EventDetail(event: Event, modifier: Modifier = Modifier) {
    Card(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)
    ) {
        Column (
            modifier = Modifier.padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
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
    Text(
        text = "event",
        modifier = modifier
    )
}

//@Preview(showBackground = true)
@Composable
fun GreetingPreview3(event: Event) {
    ISENSmartCompanionTheme {
        EventDetail(
            Event(
                id = event.id,
                description = event.description,
                title = event.title,
                date = event.date,
                location = event.location,
                category = event.category,
                imageRes = event.imageRes
        )
        )
    }
}