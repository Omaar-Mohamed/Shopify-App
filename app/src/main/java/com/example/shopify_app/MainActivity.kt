package com.example.shopify_app

import android.os.Bundle
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
import com.example.shopify_app.core.utils.PUBLISHED_KEY
import com.example.shopify_app.core.widgets.bottomnavbar.BottomNav
import com.example.shopify_app.ui.theme.ShopifyAppTheme
import com.stripe.android.PaymentConfiguration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PaymentConfiguration.init(applicationContext, "pk_test_51PTyhCKETkVYJYruNJuoAIWJjUmMTYNG8sUUFLGCafj3zBvu5OwV7YY4H2S2bhXHqrFK41a2XOlfy62R4sLHFamR00ZLBPvYAV")
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