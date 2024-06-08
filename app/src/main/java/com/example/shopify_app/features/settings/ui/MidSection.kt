package com.example.shopify_app.features.settings.ui

import android.renderscript.ScriptGroup.Input
import android.widget.SpinnerAdapter
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp

@Composable
fun MidSection(
    modifier: Modifier = Modifier
){
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
            )
        }
        InputField(inputName = "Gender") {
            RadioButtonOption(optionName = "Male", isChecked = true)
            Spacer(modifier = Modifier.width(10.dp))
            RadioButtonOption(optionName = "Female", isChecked = false)
        }
        InputField(inputName = "Age") {
            TextField(
                value = "",
                onValueChange = {},
                placeholder = { Text(text = "Enter your age")},
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                )
                ,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
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
                )
            )
        }

    }
}
@Composable
fun RadioButtonOption(
    modifier: Modifier = Modifier,
    optionName: String,
    isChecked : Boolean
){
    val alpha : Float?
    val backGroundColor : Color
    if(isChecked){
        alpha = 1f
    }
    else{
        alpha = 0.5f
    }
    Surface(
        modifier = modifier
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(5.dp))
            .padding(end = 10.dp)
            .alpha(alpha),
        shape = RoundedCornerShape(5.dp),
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            RadioButton(
                selected = isChecked, onClick = { /*TODO*/ },
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color.Black
                )
            )
            Text(
                text = optionName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
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