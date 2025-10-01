package ru.mishgan325.cownose.ui.screen.separate.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.mishgan325.cownose.R

@OptIn(markerClass = [ExperimentalMaterial3Api::class])
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    onNavigateToRegisterScreen: () -> Unit,
    onNavigateToSearchScreen: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val bringIntoViewRequester = remember { BringIntoViewRequester() }

    val uiState = loginViewModel.uiState.collectAsState().value

    if (uiState.isSuccess)
        onNavigateToSearchScreen()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .onFocusEvent {
                if (it.isFocused) coroutineScope.launch { bringIntoViewRequester.bringIntoView() }
            }
            .padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.cow),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(size = 120.dp)
                .padding(bottom = 32.dp),
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
        )

        Text(
            text = stringResource(id = R.string.login_title),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
        )

        OutlinedTextField(
            value = uiState.email,
            onValueChange = loginViewModel::onEmailChange,
            textStyle = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background),
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.email),
                    contentDescription = "Email Icon",
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
                )
            },
            label = { Text(text = "Email") },
            placeholder = { Text(text = "example@domain.com") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        OutlinedTextField(
            value = uiState.password,
            onValueChange = loginViewModel::onPasswordChange,
            textStyle = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background),
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.password),
                    contentDescription = "Password Icon",
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
                )
            },
            label = { Text(text = stringResource(id = R.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )

        ExposedDropdownMenuBox(
            expanded = uiState.farmsExpanded,
            onExpandedChange = { loginViewModel.onFarmsExpanded(expanded = !uiState.farmsExpanded) },
        ) {
            OutlinedTextField(
                value = uiState.selectedFarm,
                onValueChange = {},
                readOnly = true,
                textStyle = MaterialTheme.typography.bodyMedium,
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.gite_24px),
                        contentDescription = "Farm Icon",
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
                    )
                },
                label = { Text(text = stringResource(id = R.string.farm)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.farmsExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .background(color = MaterialTheme.colorScheme.background)
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = uiState.farmsExpanded,
                onDismissRequest = { loginViewModel.onFarmsExpanded(expanded = false) }
            ) {
                uiState.farm.forEach { farm ->
                    DropdownMenuItem(
                        text = { Text(text = farm) },
                        onClick = { loginViewModel.onFarmChange(farm = farm) },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }

        Button(
            onClick = loginViewModel::login,
            enabled = !uiState.isLoading,
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .bringIntoViewRequester(bringIntoViewRequester),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Text(text = stringResource(id = R.string.sign_in))
        }

        uiState.error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 10.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.register),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .clickable { onNavigateToRegisterScreen() }
        )
    }
}