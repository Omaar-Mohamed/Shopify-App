package com.example.shopify_app.features.personal_details.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopify_app.features.personal_details.data.model.Gender

@Composable
fun MidSection(
    modifier: Modifier = Modifier
){
    var gender by remember {
        mutableStateOf<Gender>(Gender.Male)
    }
    Column {
        InputField(inputName = "Name"){
            TextField(
                value = "",
                onValueChange = {},
                placeholder = { Text(text = "Enter your Name")},
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent
                ),
                maxLines = 1,
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Start,
                    fontSize = 18.sp
                ),
                enabled = false
            )
        }
        InputField(inputName = "Email") {
            TextField(
                value = "",
                onValueChange = {},
                placeholder = { Text(text = "Enter your email")},
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                )
                ,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email
                ),
                enabled = false
            )
        }

    }
}
@Composable
fun RadioButtonCard(
    modifier: Modifier = Modifier,
    optionName : String,
    isChecked : Boolean,
    onClick : () -> Unit
){
    val contentColor = if(isChecked) Color.White else Color.Black
    Card(
        modifier = modifier.widthIn(min = 100.dp, max = 100.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isChecked) Color.Black else Color.White
        ),
        onClick = {onClick()},
        border = BorderStroke(1.dp,Color.Black)
    ) {
        Row(
            modifier = modifier.padding(start = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = optionName, color = contentColor)
            Spacer(modifier = Modifier.weight(1f))
            RadioButton(
                selected = isChecked,
                onClick = {onClick()},
                colors = RadioButtonDefaults.colors(
                    selectedColor = contentColor,
                ),

            )
        }
    }
}
//@Composable
//fun RadioButtonOption(
//    modifier: Modifier = Modifier,
//    optionName: String,
//    isChecked : Boolean,
//    onClick : () -> Unit
//){
//    val alpha : Float?
//    val backGroundColor : Color
//    if(isChecked){
//        alpha = 1f
//    }
//    else{
//        alpha = 0.5f
//    }
//    Surface(
//        modifier = modifier
//            .border(1.dp, Color.Gray, shape = RoundedCornerShape(5.dp))
//            .padding(end = 10.dp)
//            .alpha(alpha)
//            .background(Color.Black),
//        shape = RoundedCornerShape(5.dp),
//
//    ) {
//        Row (
//            verticalAlignment = Alignment.CenterVertically
//        ){
//            RadioButton(
//                selected = isChecked, onClick = { /*TODO*/ },
//                colors = RadioButtonDefaults.colors(
//                    selectedColor = Color.Black
//                )
//            )
//            Text(
//                text = optionName,
//                fontSize = 18.sp,
//                fontWeight = FontWeight.Bold
//            )
//        }
//    }
//}
@Composable
fun InputField(
    modifier: Modifier = Modifier,
    inputName : String,
    content : @Composable () -> Unit
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.padding(5.dp)
    ) {
        Text(
            text = inputName,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = modifier
                .alpha(0.5f)
                .widthIn(min = 100.dp, max = 120.dp),
            color = Color.Black
        )
        content()
    }
}
//@Composable
//fun RadioButtonOption(
//
//)
@Composable
@Preview(showSystemUi = true)
fun MidSectionPreview(

){
    MidSection()
}