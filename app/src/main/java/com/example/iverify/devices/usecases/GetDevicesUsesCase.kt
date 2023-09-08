package com.example.iverify.devices.usecases

import com.example.iverify.devices.data.repository.IDevicesRepository
import com.example.iverify.devices.models.entities.network.DeviceNetworkEntity
import com.example.iverify.devices.models.usecases.UseCaseFailReason
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface IGetDevicesUsesCase {

    // Define a sealed class for the different states of the GetDevicesUseCase
    sealed class GetDevicesState {
        object Loading : GetDevicesState()
        data class Failure(val reason: UseCaseFailReason) : GetDevicesState()
        data class Success(val devices: List<DeviceNetworkEntity>, val pages: Int) : GetDevicesState()
    }

    // Define an operator function to asynchronously fetch devices with paging support
    suspend operator fun invoke(page: Int, pageSize: Int): Flow<GetDevicesState>
}

class GetDevicesUsesCase @Inject constructor(
    val devicesRepository: IDevicesRepository
) : IGetDevicesUsesCase {

    // Implementation of the invoke function to fetch devices using the repository
    override suspend fun invoke(
        page: Int,
        pageSize: Int
    ): Flow<IGetDevicesUsesCase.GetDevicesState> =
        devicesRepository.getDevices(page = page, pageSize = pageSize)

}