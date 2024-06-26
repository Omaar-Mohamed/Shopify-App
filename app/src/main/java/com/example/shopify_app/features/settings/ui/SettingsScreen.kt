package com.example.shopify_app.features.settings.ui

import android.util.Log
import android.widget.ImageButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Help
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.rounded.CurrencyExchange
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Doorbell
import androidx.compose.material.icons.rounded.Help
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.shopify_app.R
import com.example.shopify_app.core.helpers.ConnectionStatus
import com.example.shopify_app.core.models.Currency
import com.example.shopify_app.core.models.Language
import com.example.shopify_app.core.viewmodels.SettingsViewModel
import com.example.shopify_app.core.widgets.UnavailableInternet
import com.example.shopify_app.core.widgets.bottomnavbar.connectivityStatus
import com.example.shopify_app.features.profile.ui.OptionCard
import kotlinx.coroutines.flow.last
import kotlin.math.exp
import kotlin.math.log

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    sharedViewModel: SettingsViewModel = viewModel()
) {
    val connection by connectivityStatus()
    val isConnected = connection === ConnectionStatus.Available
    if(isConnected){
        val language by sharedViewModel.language.collectAsState()
        val currency by sharedViewModel.currency.collectAsState()
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
                .verticalScroll(rememberScrollState())
        ){
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Black, shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(text = "Settings",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

            }
            Spacer(modifier = modifier.height(15.dp))
            // Add your Composables here
            com.example.shopify_app.features.profile.ui.MidSection {
                SettingsOptionCard( imageVector = Icons.Rounded.CurrencyExchange, optionName = "Currency" ){
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        var expanded by rememberSaveable {
                            mutableStateOf(false)
                        }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            DropdownMenuItem(text = { Text(text = Currency.EGP.name) }, onClick = {
                                expanded = false
                                sharedViewModel.updateCurrency(Currency.EGP)
                            })
                            DropdownMenuItem(text = { Text(text = Currency.USD.name)}, onClick = {
                                expanded = false
                                sharedViewModel.updateCurrency(Currency.USD)
                            })
                        }
                        Text(
                            text = currency.name,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(
                            modifier = modifier,
                            onClick = {
                                expanded = true
                            }
                        ){
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                contentDescription = null,
                            )
                        }
                    }
                }
                SettingsOptionCard(imageVector = Icons.AutoMirrored.Rounded.Help, optionName = "Help Center" ) {
                    IconButton(
                        modifier = modifier,
                        onClick = {
                            navController.navigate("faqs_screen")
                        }
                    ){
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                            contentDescription = null,
                        )
                    }
                }

            }
        }
    }
    else{
        UnavailableInternet()
    }
}

@Composable
fun SettingsOptionCard(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    optionName: String,
    content : @Composable ()-> Unit
){
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(5.dp)
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            modifier = modifier
                .size(40.dp)
                .background(
                    color = Color(0xFFEEEEEE),
                    shape = RoundedCornerShape(5.dp)
                )
                .padding(7.dp),
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = optionName,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = modifier.weight(1f))
        content()
    }
}
@Composable
@Preview(showSystemUi = true)
fun SettingsScreenPreview(

){
    SettingsScreen()
}