package com.notey.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.notey.app.ui.components.GlassCard
import com.notey.app.ui.components.GradientBackground
import com.notey.app.ui.theme.AccentCyan
import com.notey.app.ui.theme.TextPrimary

@Composable
fun LockScreen(
    isSetup: Boolean,
    isBiometricAvailable: Boolean = false,
    onUnlock: (String) -> Boolean,
    onSetPin: (String) -> Unit,
    onBiometricClick: () -> Unit = {}
) {
    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    
    // Auto-trigger biometric on first load if available and not setup
    LaunchedEffect(isBiometricAvailable, isSetup) {
        if (isBiometricAvailable && !isSetup) {
            onBiometricClick()
        }
    }
    
    GradientBackground {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isSetup) "🔐 Setup PIN" else "🔐 Enter PIN",
                        color = TextPrimary,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    OutlinedTextField(
                        value = pin,
                        onValueChange = { input ->
                            if (input.length <= 6 && input.all { it.isDigit() }) {
                                pin = input
                                error = ""
                            }
                        },
                        label = { Text("PIN (4-6 digits)") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = AccentCyan,
                            unfocusedBorderColor = TextPrimary.copy(alpha = 0.5f)
                        )
                    )
                    
                    if (error.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(error, color = MaterialTheme.colorScheme.error)
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Button(
                        onClick = {
                            if (pin.length < 4) {
                                error = "PIN must be at least 4 digits"
                                return@Button
                            }
                            
                            if (isSetup) {
                                onSetPin(pin)
                            } else {
                                if (onUnlock(pin)) {
                                    // Success
                                } else {
                                    error = "Incorrect PIN"
                                    pin = ""
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentCyan),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (isSetup) "Set PIN" else "Unlock")
                    }
                    
                    // Biometric button (only show if available and not in setup mode)
                    if (isBiometricAvailable && !isSetup) {
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        OutlinedButton(
                            onClick = onBiometricClick,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("👆 Use Fingerprint")
                        }
                    }
                }
            }
        }
    }
}
