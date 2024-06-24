package com.example.shopify_app.features.profile.ui

import android.annotation.SuppressLint
import android.graphics.Paint.Style
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopify_app.R
import com.example.shopify_app.core.datastore.StoreCustomerEmail
import kotlinx.coroutines.launch
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun UserCard(
    modifier: Modifier = Modifier
){
    val coroutineScope = rememberCoroutineScope()
    val customerStore = StoreCustomerEmail(LocalContext.current)
    var email by rememberSaveable {
        mutableStateOf("")
    }
    var name by rememberSaveable {
        mutableStateOf("")
    }
    coroutineScope.launch {
        customerStore.getEmail.collect{
            if (it != null) {
                email = it
            }
        }
    }
    coroutineScope.launch {
        customerStore.getName.collect{
            if (it != null) {
                name = it
                Log.i("namecustomer", "UserCard: $name")
            }
        }
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(width = 1.dp, color = Color.Transparent),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(5.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.img1),
                contentDescription = null,
                modifier = modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp)),
            )
            Spacer(modifier = modifier.width(5.dp))
            Column(
                modifier = modifier
            ) {
                Text(
                    modifier = modifier,
                    text = name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = email,
                    fontSize = 10.sp,
                    modifier = modifier.alpha(0.5f)
                )
            }
        }
    }
}
@Composable
@Preview(showSystemUi = true)
fun UserCardPreview(){
    UserCard()
}