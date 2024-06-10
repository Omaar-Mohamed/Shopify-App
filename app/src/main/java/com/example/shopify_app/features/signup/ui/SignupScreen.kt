package com.example.shopify_app.features.signup.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shopify_app.R
import com.example.shopify_app.core.networking.AuthState
import com.example.shopify_app.features.signup.viewmodel.SignupViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SignupScreen(
    signupViewModel: SignupViewModel = viewModel(),
    navController: NavController
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPassword: String by remember { mutableStateOf("") }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val authState by signupViewModel.authState.observeAsState()
    val emailVerificationState by signupViewModel.emailVerificationState.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription ="logo",
            modifier = Modifier
                .fillMaxWidth(),
            contentScale= ContentScale.FillHeight
        )
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Sign Up",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Create an new account",
            color = Color.Gray,
            fontSize = 16.sp,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        BasicTextField(
            value = username,
            onValueChange = { username = it },
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    if (username.isEmpty()) {
                        Text("User Name")
                    }
                    innerTextField()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        BasicTextField(
            value = email,
            onValueChange = { email = it },
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    if (email.isEmpty()) {
                        Text("Email")
                    }
                    innerTextField()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            BasicTextField(
                value = password,
                onValueChange = { password = it },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .background(Color.White)
                            .padding(16.dp)
                    ) {
                        if (password.isEmpty()) {
                            Text("Password")
                        }
                        innerTextField()
                    }
                },
                modifier =  Modifier
                    .weight(1f),
            )
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_eye),
                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                modifier = Modifier
                    .clickable { passwordVisible = !passwordVisible }
                    .padding(8.dp),
                tint = if (passwordVisible) Color.Gray else Color.Black
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            BasicTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .background(Color.White)
                            .padding(16.dp)
                    ) {
                        if (confirmPassword.isEmpty()) {
                            Text("Confirm Password")
                        }
                        innerTextField()
                    }
                },
                modifier =  Modifier
                    .weight(1f),
            )
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_eye),
                contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password",
                modifier = Modifier
                    .clickable { confirmPasswordVisible = !confirmPasswordVisible }
                    .padding(8.dp),
                tint = if (confirmPasswordVisible) Color.Gray else Color.Black
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "By Creating an account you have to agree with your them & condication",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && isChecked) {
                    if (password == confirmPassword) {
                        signupViewModel.signup(email, password)
                    }else{
                        Toast.makeText(context, "Password and confirm password is not matching", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(context, "Please check you have some filed empty", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(16.dp))

        authState?.let {
            when (it) {
                is AuthState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
                }
                is AuthState.Success -> {
                    it.user?.let { user ->
                        signupViewModel.sendEmailVerification()
                        navController.navigate("login_screen")
                    }
                }
                is AuthState.Error -> {
                    Toast.makeText(context, "Error: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        emailVerificationState?.let { verified ->
            if (verified) {
                Toast.makeText(context, "Verification email sent.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to send verification email.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignupPreview() {
    //SignupScreen()
}