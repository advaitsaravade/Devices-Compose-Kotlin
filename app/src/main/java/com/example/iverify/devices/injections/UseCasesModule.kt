package com.example.iverify.devices.injections

import com.example.iverify.devices.data.repository.IDevicesRepository
import com.example.iverify.devices.usecases.GetDevicesUsesCase
import com.example.iverify.devices.usecases.IGetDevicesUsesCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCasesModule {

    @Provides
    @Singleton
    fun providesGetDevices(
        devicesRepository: IDevicesRepository
    ): IGetDevicesUsesCase {
        return GetDevicesUsesCase(
            devicesRepository = devicesRepository
        )
    }

}