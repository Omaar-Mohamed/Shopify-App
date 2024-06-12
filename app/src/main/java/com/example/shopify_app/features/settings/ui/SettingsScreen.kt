package com.example.shopify_app.features.settings.ui

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.rounded.KeyboardArrowRight
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shopify_app.R
import com.example.shopify_app.features.profile.ui.OptionCard
import kotlin.math.exp

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }
    val language by rememberSaveable {
        mutableStateOf("English")
    }
    var isMale by rememberSaveable {
        mutableStateOf(true)
    }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
            .verticalScroll(rememberScrollState())
    ){

        IconButton(
            onClick = { /*TODO*/ },
            colors = IconButtonDefaults.iconButtonColors(),
        ) {
            Image(
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = null,
                modifier = modifier.size(30.dp)
            )
        }
        Spacer(modifier = modifier.height(15.dp))
        Text(
            text = "Settings",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
            )
        Spacer(modifier = modifier.height(15.dp))
        // Add your Composables here
        com.example.shopify_app.features.profile.ui.MidSection {
            SettingsOptionCard( iconResource = R.drawable.language, optionName = "Language" ){
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(text = { Text(text = "English") }, onClick = {expanded = false })
                        DropdownMenuItem(text = { Text(text = "Arabic")}, onClick = { expanded = false })
                    }
                    Text(
                        text = language
                    )
                    Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null)
                }

            }
            SettingsOptionCard(iconResource = R.drawable.notification, optionName = "Notification" ) {
                Switch(
                    checked = false,
                    onCheckedChange = {},
                    colors = SwitchDefaults.colors(
                        checkedIconColor = Color.Black,
                        checkedThumbColor = Color.Black,
                        uncheckedThumbColor = Color.Black,
                        checkedBorderColor = Color.Black,
                        uncheckedTrackColor = Color.Transparent,
                        checkedTrackColor = Color.Black
                    ),
                )
            }
            SettingsOptionCard(iconResource = R.drawable.dark_mode , optionName = "Dark Mode" ) {
                Switch(
                    checked = false,
                    onCheckedChange = {},
                    colors = SwitchDefaults.colors(
                        checkedIconColor = Color.Black,
                        checkedThumbColor = Color.Black,
                        uncheckedThumbColor = Color.Black,
                        checkedBorderColor = Color.Black,
                        uncheckedTrackColor = Color.Transparent,
                        checkedTrackColor = Color.Black
                    )
                )
            }
            SettingsOptionCard(iconResource = R.drawable.help_24dp, optionName = "Help Center" ) {
                Icon(imageVector = Icons.Rounded.KeyboardArrowRight, contentDescription = null )
            }

        }
    }
}

@Composable
fun SettingsOptionCard(
    modifier: Modifier = Modifier,
    iconResource : Int,
    optionName: String,
    content : @Composable ()-> Unit
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp)
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = iconResource ),
            contentDescription = null,
            modifier = modifier
                .size(30.dp)
                .background(
                    color = Color(0xFFEEEEEE),
                    shape = RoundedCornerShape(5.dp)
                )
                .padding(5.dp),
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