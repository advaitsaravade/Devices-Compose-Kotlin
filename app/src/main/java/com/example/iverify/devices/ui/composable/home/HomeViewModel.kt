package com.example.iverify.devices.ui.composable.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iverify.devices.R
import com.example.iverify.devices.models.entities.network.DeviceNetworkEntity
import com.example.iverify.devices.models.usecases.UseCaseFailReason
import com.example.iverify.devices.usecases.IGetDevicesUsesCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.lang.Integer.max
import java.lang.Integer.min
import javax.inject.Inject

// Annotate the ViewModel with HiltViewModel for dependency injection
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getDevicesUsesCase: IGetDevicesUsesCase
) : ViewModel() {

    // Define a constant for the page size
    companion object {
        const val PAGE_SIZE = 10
    }

    // Define a data class to represent the state of the Home screen
    data class HomeState(
        val isLoading: Boolean = false,
        val devices: List<DeviceNetworkEntity> = emptyList(),
        val filteredDevices: List<DeviceNetworkEntity> = emptyList(),
        val page: Int = 1,
        val maxPageIndex: Int = 1,
        val isPrevEnabled: Boolean = false,
        val isNextEnabled: Boolean = true,
        val searchTerm: String = ""
    )

    // Define sealed class for events that can occur on the Home screen
    sealed class HomeEvent {
        data class ShowError(val message: Int): HomeEvent()
    }

    // Initialize the state and event variables
    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state
    private val _event = MutableSharedFlow<HomeEvent>()
    val event = _event.asSharedFlow()

    init {
        getDevices()
    }

    // Job variable to keep track of the ongoing getDevices coroutine
    private var getDevicesJob: Job? = null
    // Function to initiate the retrieval of devices from a use case
    fun getDevices() {
        // Cancel any existing job if it's running
        getDevicesJob?.cancel()
        // Launch a new coroutine in the viewModelScope
        getDevicesJob = viewModelScope.launch {
            // Call the getDevicesUseCase and collect the result
            getDevicesUsesCase(
                page = state.value.page,
                pageSize = PAGE_SIZE
            ).collect { getDevicesState ->
                when(getDevicesState) {
                    IGetDevicesUsesCase.GetDevicesState.Loading -> {
                        // Set loading state and reset device list
                        isLoading(true)
                        _state.value = state.value.copy(
                            devices = emptyList()
                        )
                        filterDevices()
                    }
                    is IGetDevicesUsesCase.GetDevicesState.Failure -> {
                        when(getDevicesState.reason) {
                            UseCaseFailReason.NoInternet -> {
                                // Inform user no internet
                                _event.emit(HomeEvent.ShowError(R.string.no_internet))
                            }
                            UseCaseFailReason.ServerError -> {
                                // Inform user server error
                                _event.emit(HomeEvent.ShowError(R.string.server_error))
                            }
                            is UseCaseFailReason.Unknown -> {
                                // Inform user generic failure
                                _event.emit(HomeEvent.ShowError(R.string.generic_error))
                            }
                        }
                        // Reset loading state
                        isLoading(false)
                    }
                    is IGetDevicesUsesCase.GetDevicesState.Success -> {
                        // Set loading state, update device list, and set max page index
                        isLoading(false)
                        _state.value = state.value.copy(
                            devices = getDevicesState.devices,
                            maxPageIndex = getDevicesState.pages
                        )
                        filterDevices()
                    }
                }
            }
        }
    }

    // Function to handle search term updates
    fun searchTermUpdated(searchTerm: String) {
        _state.value = state.value.copy(
            searchTerm = searchTerm,
        )
        filterDevices()
    }

    // Function to filter devices based on the search term
    private fun filterDevices() {
        _state.value.searchTerm.lowercase().let { validSearchTerm ->
            _state.value = state.value.copy(
                filteredDevices = _state.value.devices.filter {
                    it.name.lowercase().contains(validSearchTerm) ||
                            it.code.lowercase().contains(validSearchTerm)
                }
            )
        }
    }

    // Function to update the loading state
    private fun isLoading(isLoading: Boolean) {
        _state.value = state.value.copy(
            isLoading = isLoading
        )
    }

    // Placeholder function to handle clicking on a device item
    fun deviceClicked(device: DeviceNetworkEntity) {
        // Do something with the selected device
    }

    // Function to navigate to the previous page of devices
    fun previousPage() {
        val page = max(1, state.value.page - 1)
        _state.value = state.value.copy(
            page = page,
            isPrevEnabled = page != 1,
            isNextEnabled = page != _state.value.maxPageIndex
        )
        // Fetch devices for the new page
        getDevices()
    }

    // Function to navigate to the next page of devices
    fun nextPage() {
        val page = min(state.value.page + 1, _state.value.maxPageIndex)
        _state.value = state.value.copy(
            page = page,
            isPrevEnabled = page != 1,
            isNextEnabled = page != _state.value.maxPageIndex
        )
        // Fetch devices for the new page
        getDevices()
    }
}