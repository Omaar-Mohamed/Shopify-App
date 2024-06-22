package com.example.shopify_app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.shopify_app.core.routing.RootNavGraph
import com.example.shopify_app.core.widgets.bottomnavbar.BottomNav
import com.example.shopify_app.ui.theme.ShopifyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShopifyAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    RootNavGraph(navController = navController)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // Handle the new intent if the activity was already running
        handleDeepLinkIntent(intent)
    }

    private fun handleDeepLinkIntent(intent: Intent?) {
        intent?.data?.let { uri ->
            if (uri.toString().startsWith("https://shopify_app.example.com/success")) {
                Toast.makeText(this, "Payment successful", Toast.LENGTH_SHORT).show()
                // Navigate to the success screen or handle success action
            } else if (uri.toString().startsWith("https://shopify_app.example.com/cancel")) {
                Toast.makeText(this, "Payment cancelled", Toast.LENGTH_SHORT).show()
                // Navigate to the cancel screen or handle cancel action
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
    ShopifyAppTheme {
        Greeting("Android")
    }
}