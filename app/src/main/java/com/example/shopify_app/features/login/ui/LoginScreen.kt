package com.example.shopify_app.features.login.ui

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.shopify_app.core.datastore.StoreCustomerEmail
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.core.networking.AppRemoteDataSourseImpl
import com.example.shopify_app.core.networking.AuthState
import com.example.shopify_app.features.home.data.models.LoginCustomer.LoginCustomer
import com.example.shopify_app.features.home.ui.ErrorView
import com.example.shopify_app.features.home.ui.LoadingView
import com.example.shopify_app.features.login.data.LoginRepo
import com.example.shopify_app.features.login.data.LoginRepoImpl
import com.example.shopify_app.features.login.viewmodel.LoginViewModel
import com.example.shopify_app.features.login.viewmodel.LoginViewModelFactory
import com.example.shopify_app.features.signup.data.model.CustomerRequest.CustomerXX
import com.example.shopify_app.features.signup.data.model.CustomerRequest.SignupRequest
import com.example.shopify_app.features.signup.data.repo.SignupRepo
import com.example.shopify_app.features.signup.data.repo.SignupRepoImpl
import com.example.shopify_app.features.signup.viewmodel.SignUpViewModelFactory
import com.example.shopify_app.features.signup.viewmodel.SignupViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    repo: LoginRepo = LoginRepoImpl.getInstance(AppRemoteDataSourseImpl),
    navController: NavController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var circularProgress by remember { mutableStateOf(false) }

    val factory = LoginViewModelFactory(repo)
    val viewModel: LoginViewModel = viewModel(factory = factory)
    val signupFactory = SignUpViewModelFactory(SignupRepoImpl.getInstance(AppRemoteDataSourseImpl))
    val signupViewModel: SignupViewModel = viewModel(factory = signupFactory)

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = StoreCustomerEmail(context)

    val authState by viewModel.authState.observeAsState()
    val isEmailVerifiedState by viewModel.isEmailVerifiedState.observeAsState()

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account?.idToken?.let { idToken ->
                viewModel.signInWithGoogle(idToken)
                scope.launch{
                    val customer = viewModel.getCustomer(account.email!!)
                    scope.async {
                        Log.i("Email", "LoginScreen: ${customer.customers[0].email}")
                        dataStore.setEmail("ahmed.abufoda1999@gmail.com")
                    }.onAwait
                    Log.i("account", "LoginScreen: $customer")
                    if(customer.customers.isEmpty()){
                        Log.i("account", "LoginScreen: empty")
                        signupViewModel.signUpApi(SignupRequest(CustomerXX(email = account.email!!, first_name = account.displayName!!, password = account.id!!, password_confirmation =account.id!!)))
                    }
                }
            }
        } catch (e: ApiException) {
            Toast.makeText(context, "Google sign-in failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, gso)

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
            text = "Welcome",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Please Login to continue to app",
            color = Color.Gray,
            fontSize = 16.sp,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

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


        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()){
                    viewModel.login(email,password)
                    scope.launch {
                        dataStore.setEmail(email)
                    }
                }else{
                    Toast.makeText(context, "Wrong User Name or Password", Toast.LENGTH_SHORT).show()
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

        Text("or", fontSize = 14.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val signInIntent = googleSignInClient.signInIntent
                googleSignInLauncher.launch(signInIntent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            border = BorderStroke(2.dp, Color.Gray)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.google),
                contentDescription = "Google Icon",
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Continue with Google", color = Color.Black)
        }
        Spacer(modifier = Modifier.height(16.dp))

        LaunchedEffect(authState) {
            when (authState) {
                is AuthState.Loading -> {
                    circularProgress = true
                }
                is AuthState.Success -> {
                    val user = (authState as AuthState.Success).user
                    if (user != null) {
                        viewModel.checkEmailVerification()
                    }
                }
                is AuthState.Error -> {
                    val errorMessage = (authState as AuthState.Error).exception?.message
                    Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
        if (circularProgress){
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        }

        LaunchedEffect(isEmailVerifiedState) {
            isEmailVerifiedState?.let { verified ->
                if (verified) {
                    navController.navigate("bottom_nav")
                } else {
                    Toast.makeText(context, "Email is not verified.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    //LoginScreen()
}