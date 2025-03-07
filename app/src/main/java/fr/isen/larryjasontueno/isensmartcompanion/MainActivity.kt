package fr.isen.larryjasontueno.isensmartcompanion

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.FlowColumnScopeInstance.weight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.ai.client.generativeai.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import fr.isen.larryjasontueno.isensmartcompanion.ui.theme.ISENSmartCompanionTheme
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log
import kotlin.math.min

data class TabBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
    val badgeAmount: Int? = null
)

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalStdlibApi::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val generativeModel = GenerativeModel(
            // Use a model that's applicable for your use case (see "Implement basic use cases" below)
            modelName = "gemini-1.5-flash", // for Text to text
            // Access your API key as a Build Configuration variable (see "Set up your API key" above)
            apiKey = "AIzaSyCtLoNEMI2lopIpiGN-gjVYxI0AddOfTeU"
        )

        enableEdgeToEdge()
        setContent {
            val homeTab = TabBarItem(title = stringResource(id = R.string.title_home_activity), selectedIcon = Icons.Filled.Home, unSelectedIcon = Icons.Outlined.Home)
            val EvenScreen = TabBarItem(title = stringResource(id = R.string.title_events_screen), selectedIcon = Icons.Filled.PlayArrow, unSelectedIcon = Icons.Outlined.PlayArrow)
            val HistoryScreen = TabBarItem(title = stringResource(id = R.string.title_history_screen), selectedIcon = Icons.Filled.Menu, unSelectedIcon = Icons.Outlined.Menu)

            //Liste de tous les tab
            val tabBarItems = listOf(homeTab, EvenScreen, HistoryScreen)
            //Controlleur de la barre de navigation
            val navController = rememberNavController()

            ISENSmartCompanionTheme {
                Scaffold (bottomBar = { TabView(tabBarItems, navController) }) { innerPadding ->
                    NavHost(navController = navController, startDestination = homeTab.title) {
                        composable(homeTab.title) {
                            Greeting(name = "")
                        }
                        composable(EvenScreen.title) {
                            EventsScreen()
                        }
                        composable(HistoryScreen.title) {
                            EventHistory(name = "")
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun TabView( tabBarItems: List<TabBarItem>, navController: NavController ){
    var selectedTabIndex = rememberSaveable {
        mutableStateOf(0)
    }

//var selectedTabIndex = 0

    NavigationBar {
        tabBarItems.forEachIndexed{ index, tabBarItem ->
            NavigationBarItem(
                selected = selectedTabIndex.value == index,
                onClick = {
                    selectedTabIndex.value = index
                    navController.navigate(tabBarItem.title)
                },

                icon = {
                    TabBarIconView(
                        isSelected = selectedTabIndex.value == index,
                        selectedIcon = tabBarItem.selectedIcon,
                        unselectedIcon = tabBarItem.unSelectedIcon,
                        title = tabBarItem.title,
                        badgeAmount = tabBarItem.badgeAmount
                    )
                },
                label = {Text(tabBarItem.title)})
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabBarIconView(
    isSelected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    title: String,
    badgeAmount: Int? = null
) {
    BadgedBox(badge = { TabBarBadgeView(badgeAmount) }) {
        Icon(
            imageVector = if (isSelected) {selectedIcon} else {unselectedIcon},
            contentDescription = title
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TabBarBadgeView(count: Int? = null) {
    if (count != null) {
        Badge {
            Text(count.toString())
        }
    }
}
// end of the reusable components that can be copied over to any new projects
// ----------------------------------------

// This was added to demonstrate that we are infact changing views when we click a new tab
@Composable
fun MoreView() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Thing 1")
        Text("Thing 2")
        Text("Thing 3")
        Text("Thing 4")
        Text("Thing 5")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val scope = CoroutineScope(Dispatchers.IO)
    var geminiResponse = remember { mutableStateOf("") }
    var textValue = remember { mutableStateOf("") }
    val context = LocalContext.current
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0x7d7df5))
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxSize()
        ) {
            val image = painterResource(id = R.drawable.isen)
            Image(
                painter = image,
                contentDescription ="Envoyer le message",
                modifier = modifier
                    .size(200.dp)
//                    .align(Alignment.TopCenter)
                    .padding(16.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = "Hello $name!",
                modifier = modifier
            )
            Spacer(modifier = Modifier.weight(1f))
            Card(
                modifier = Modifier
                    .width(350.dp)
                    .height(250.dp)
                    .padding(8.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .verticalScroll(rememberScrollState()),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = geminiResponse.value,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Row (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 100.dp)
                    .align(Alignment.End),
                verticalAlignment = Alignment.Bottom
            ) {
                TextField(
                    value = textValue.value,
                    onValueChange = {  textValue.value = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
//                        .heightIn(max = 48.dp)
                        .fillMaxWidth(),
                    placeholder = { Text(stringResource(id = R.string.textvalue_empti)) },
                    singleLine = true
                    )
                Button(onClick = {
                    //textValue="bonjour comment tu vas?"
                    if (textValue.value.isNotEmpty()) {
                        val generativeModelImage = GenerativeModel(
                            // Use a model that's applicable for your use case (see "Implement basic use cases" below)
                            modelName = "gemini-1.5-flash", // for Text to text
                            // Access your API key as a Build Configuration variable (see "Set up your API key" above)
                            apiKey = "AIzaSyCtLoNEMI2lopIpiGN-gjVYxI0AddOfTeU"
                        )
                        scope.launch {
                            val response =generativeModelImage.generateContent(textValue.value)
                            geminiResponse.value = "${response?.text}"
                            println("print2")
                            println("response: ${response?.text}")
                        }

                    } else {

                        Toast.makeText(context, "Veuillez entrer un message", Toast.LENGTH_SHORT).show()
                    }
                }
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Envoyer",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

    }

}


// FETCH EVENTS DATA FROM API

//@Composable
fun fetchEvents(onResult: (List<Event>) -> Unit) {
    val call = RetrofitClient.instance.getEvents()

    call.enqueue(object : Callback<List<Event>> {
        override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
            if (response.isSuccessful) {
                val eventsList = response.body() ?: emptyList()
                Log.d("API_SUCCESS", "Liste des événements : $eventsList")
                onResult(eventsList)
                println("eventlist : $eventsList")
            } else {
                Log.e("API_ERROR", "Erreur : ${response.errorBody()?.string()}")
                onResult(emptyList())
            }
        }
        override fun onFailure(call: Call<List<Event>>, t: Throwable) {
            Log.e("API_FAILURE", "Erreur de connexion : ${t.message}")
            onResult(emptyList())
        }
    })
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ISENSmartCompanionTheme {
        Greeting("Android")
    }
}