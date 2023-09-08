package com.example.iverify.devices.ui.composable.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.iverify.devices.R
import com.example.iverify.devices.models.entities.network.DeviceNetworkEntity
import com.example.iverify.devices.ui.theme.Bounds1x
import com.example.iverify.devices.ui.theme.Bounds2x
import com.example.iverify.devices.ui.theme.BoundsHalf
import com.example.iverify.devices.ui.theme.Highlight
import com.example.iverify.devices.ui.theme.IVerifyDevicesTheme
import com.example.iverify.devices.ui.theme.Light
import com.example.iverify.devices.ui.theme.RoundedCornerBound
import com.example.iverify.devices.ui.theme.Typography
import com.example.iverify.devices.util.toHumanReadableTimeAgo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeComposable(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = true, block = {
        viewModel.event.collect { event ->
            when(event) {
                is HomeViewModel.HomeEvent.ShowError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    })
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .fillMaxWidth(),
                title = {
                    Text(
                        stringResource(id = R.string.app_name),
                        color = Light
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Highlight)
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(
                        top = innerPadding.calculateTopPadding(),
                        bottom = innerPadding.calculateBottomPadding(),
                        start = Bounds1x,
                        end = Bounds1x
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if(viewModel.state.value.isLoading) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                        Text(
                            modifier = Modifier.fillMaxWidth().padding(top = Bounds2x),
                            style = Typography.titleLarge,
                            text = stringResource(id = R.string.loading_title),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = Bounds1x, start = Bounds2x, end = Bounds2x),
                            style = Typography.bodyLarge,
                            text = stringResource(id = R.string.loading_subtitle),
                            textAlign = TextAlign.Center,
                        )
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .padding(top = Bounds1x)
                            .fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = Bounds1x),
                            value = viewModel.state.value.searchTerm,
                            label = {
                                Text(stringResource(R.string.search_label))
                            },
                            trailingIcon = {
                                if (viewModel.state.value.searchTerm.isNotEmpty()) {
                                    IconButton(onClick = {
                                        viewModel.searchTermUpdated("")
                                    }) {
                                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                                    }
                                }
                            },
                            onValueChange = { searchTerm ->
                                viewModel.searchTermUpdated(searchTerm)
                            }
                        )
                        IconButton(
                            modifier = Modifier.size(30.dp),
                            onClick = { viewModel.getDevices() }
                        ) {
                            Icon(
                                modifier = Modifier.fillMaxSize(),
                                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_refresh_24), // Replace with your own icon
                                contentDescription = "Refresh data",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    if (viewModel.state.value.filteredDevices.isNotEmpty()) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(top = Bounds1x, start = BoundsHalf, end = BoundsHalf),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                modifier = Modifier.weight(0.5f),
                                enabled = viewModel.state.value.isPrevEnabled,
                                onClick = { viewModel.previousPage() }
                            ) {
                                Text(stringResource(id = R.string.prev_page))
                            }
                            Spacer(Modifier.width(Bounds1x))
                            Button(
                                modifier = Modifier.weight(0.5f),
                                enabled = viewModel.state.value.isNextEnabled,
                                onClick = { viewModel.nextPage() }
                            ) {
                                Text(stringResource(id = R.string.next_page))
                            }
                        }
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = Bounds1x)
                        ) {
                            items(viewModel.state.value.filteredDevices) { filteredDevice ->
                                DeviceItem(
                                    device = filteredDevice,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.deviceClicked(filteredDevice)
                                        }
                                )
                                Spacer(
                                    modifier = Modifier.height(Bounds1x)
                                )
                            }
                        }
                    } else {
                        EmptyPlaceholder(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            viewModel.getDevices()
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun DeviceItem(
    device: DeviceNetworkEntity,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(RoundedCornerBound)
            )
            .padding(Bounds1x),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column {
            Text(
                modifier = Modifier.padding(bottom = BoundsHalf),
                style = Typography.titleMedium,
                text = device.name
            )
            Text(
                style = Typography.bodyMedium,
                text = device.code,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                modifier = Modifier.padding(bottom = BoundsHalf),
                style = Typography.titleMedium,
                text = device.latestScanDate?.toHumanReadableTimeAgo() ?: stringResource(id = R.string.unknown_time)
            )
            Text(
                style = Typography.bodyMedium,
                text = device.device ?: stringResource(id = R.string.unknown_device),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun EmptyPlaceholder(
    modifier: Modifier,
    refresh: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = Typography.titleLarge,
            text = stringResource(id = R.string.empty_title),
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Bounds1x, start = Bounds2x, end = Bounds2x),
            style = Typography.bodyLarge,
            text = stringResource(id = R.string.empty_subtitle),
            textAlign = TextAlign.Center,
        )
        Button(
            modifier = Modifier
                .padding(top = Bounds1x, start = Bounds2x, end = Bounds2x),
            onClick = {
            refresh()
        }) {
            Text(stringResource(id = R.string.refresh_devices))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    IVerifyDevicesTheme {
        HomeComposable(
            navController = rememberNavController(),
        )
    }
}