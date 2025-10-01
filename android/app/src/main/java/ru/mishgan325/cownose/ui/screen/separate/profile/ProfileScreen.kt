package ru.mishgan325.cownose.ui.screen.separate.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import ru.mishgan325.cownose.R

@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel) {
    val isLoading = rememberSaveable { mutableStateOf(value = true) }
    val photoUpdating = rememberSaveable { mutableStateOf(value = false) }

    val uiState = profileViewModel.uiState.collectAsState().value
    val fullName = remember(key1 = MutableStateFlow(value = uiState)) { "John Smith" }
    val email = remember(key1 = MutableStateFlow(value = uiState)) { "example@domain.com" }

    LaunchedEffect(key1 = Unit) {
        delay(timeMillis = 1000L)
        isLoading.value = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Surface(
                    modifier = Modifier
                        .size(size = 200.dp)
                        .clip(CircleShape),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = {
                                /* TODO: profileViewModel.onChangePhoto(); */
                                photoUpdating.value = true
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "User Profile",
                                modifier = Modifier.size(size = 100.dp)
                            )
                        }
                    }
                }
            }

            if (isLoading.value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .semantics { contentDescription = "Loading" },
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(space = 16.dp)
                ) {
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(space = 12.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.about_user),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            ProfileInfoRow(label = "Email", value = email)
                            ProfileInfoRow(label = "Full name", value = fullName)
                        }
                    }

                    Button(
                        onClick = { /* TODO: profileViewModel.onEdit() */ },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = photoUpdating.value,
                        shape = MaterialTheme.shapes.extraLarge
                    ) {
                        Text(text = stringResource(id = R.string.edit_profile))
                    }
                }
            }
        }

        if (photoUpdating.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
        }
    }
}