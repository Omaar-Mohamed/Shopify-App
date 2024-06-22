package com.example.shopify_app.core.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.shopify_app.R

//@Composable
//fun HeaderSection(
//    modifier : Modifier = Modifier,
//    title : String,
//    navController: NavHostController = rememberNavController()
//){
//    Row(
//        Modifier.fillMaxWidth(),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        IconButton(onClick = { navController.popBackStack()}) {
//            Image(
//                painter = painterResource(id = R.drawable.back_arrow),
//                contentDescription = null,
//                modifier = modifier.size(40.dp)
//            )
//        }
//        Text(
//            text = title,
//            style = MaterialTheme.typography.titleLarge.copy(
//                fontWeight = FontWeight.ExtraBold
//            ),
//            modifier = modifier.fillMaxWidth(),
//        )
//    }
//}
//
//@Composable
//@Preview(showSystemUi = true)
//fun P(
//
//){
//    HeaderSection( title = "Settings")
//}