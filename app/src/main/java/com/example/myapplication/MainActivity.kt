package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var timezones: List<String>
    private lateinit var selectedTimezone: String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val timezoneListView = findViewById<ListView>(R.id.listView)
        val okButton = findViewById<Button>(R.id.okButton)

        CoroutineScope(Dispatchers.Main).launch {
            timezones = RetrofitInstance.api.getTimezones()

            // Sort: USA timezones first
            val usaZones = timezones.filter { it.contains("America") }
            val otherZones = timezones.filterNot { it.contains("America") }
            val sortedList = usaZones + otherZones

            val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_single_choice, sortedList)
            timezoneListView.adapter = adapter
            timezoneListView.choiceMode = ListView.CHOICE_MODE_SINGLE

            timezoneListView.setOnItemClickListener { parent, view, position, id ->
                selectedTimezone = sortedList[position]
            }

            okButton.setOnClickListener {
                if (::selectedTimezone.isInitialized) {
                    val intent = Intent(this@MainActivity, TimeSelectionActivity::class.java)
                    intent.putExtra("timezone", selectedTimezone)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@MainActivity, "Please select a timezone", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}