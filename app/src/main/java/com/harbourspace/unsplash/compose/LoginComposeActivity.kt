package com.harbourspace.unsplash.compose

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginComposeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                LoginPage()
            }
        }
    }
    @Composable
    fun LoginPage() {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val email = remember { mutableStateOf(TextFieldValue()) }
            val password = remember { mutableStateOf(TextFieldValue()) }

            Text(text = "Login", style = TextStyle(fontSize = 40.sp))

            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text(text = "Email") },
                value = email.value,
                onValueChange = { email.value = it })

            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text(text = "Password") },
                value = password.value,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { password.value = it })

            Spacer(modifier = Modifier.height(20.dp))
            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Button(
                    onClick = {
                        val auth = Firebase.auth
                        auth.signInWithEmailAndPassword(email.value.text, password.value.text)
                            .addOnCompleteListener(this@LoginComposeActivity) { task ->
                                if (task.isSuccessful) {
                                    Log.d("Info", "signInWithEmail:success")
                                    val user = auth.currentUser
                                    openMainActivity(user)
                                } else {
                                    Log.w("Error", "signInWithEmail:failure", task.exception)
                                    Toast.makeText(baseContext, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show()
                                }
                            }
                    },
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Login")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
    private fun openMainActivity(user: FirebaseUser?) {
        val intent = Intent(this, MainComposeActivity::class.java)
        startActivity(intent)
    }
}

