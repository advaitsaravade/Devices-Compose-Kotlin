package com.example.iverify.devices.data.repository

import com.example.iverify.devices.BuildConfig
import com.example.iverify.devices.data.network.IInternetAPI
import com.example.iverify.devices.models.usecases.UseCaseFailReason
import com.example.iverify.devices.usecases.IGetDevicesUsesCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

interface IDevicesRepository {

    // Function to asynchronously get a list of devices with paging support
    suspend fun getDevices(page: Int, pageSize: Int): Flow<IGetDevicesUsesCase.GetDevicesState>

}

class DevicesRepository @Inject constructor(
    val api: IInternetAPI
): IDevicesRepository {

    override suspend fun getDevices(page: Int, pageSize: Int): Flow<IGetDevicesUsesCase.GetDevicesState> = flow {
        // Emit a loading state to indicate that data retrieval is in progress
        emit(IGetDevicesUsesCase.GetDevicesState.Loading)
        try {
            // Attempt to fetch devices from the API
            val result = api.getDevices(
                page = page,
                pageSize = pageSize
            )
            // Emit a success state with the fetched devices and page information
            emit(IGetDevicesUsesCase.GetDevicesState.Success(result.devices, pages = result.pages))
        } catch (e: HttpException) {
            // Handle HTTP exceptions (e.g., server errors)
            if(BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            emit(IGetDevicesUsesCase.GetDevicesState.Failure(reason = UseCaseFailReason.ServerError))
        } catch (e: IOException) {
            // Handle IO exceptions (e.g., network connectivity issues)
            if(BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            emit(IGetDevicesUsesCase.GetDevicesState.Failure(reason = UseCaseFailReason.NoInternet))
        } catch (e: Exception) {
            // Handle other exceptions (e.g., unknown errors)
            if(BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            emit(IGetDevicesUsesCase.GetDevicesState.Failure(reason = UseCaseFailReason.Unknown()))
        }
    }

}