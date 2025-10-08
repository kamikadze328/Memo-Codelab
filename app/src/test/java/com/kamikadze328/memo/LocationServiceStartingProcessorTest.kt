package com.kamikadze328.memo

import android.content.Context
import androidx.core.content.ContextCompat
import com.kamikadze328.memo.model.Memo
import com.kamikadze328.memo.model.MemoLocation
import com.kamikadze328.memo.repository.Repository
import com.kamikadze328.memo.utils.permissions.isFineLocationGranted
import com.kamikadze328.memo.utils.permissions.isPostNotificationsGranted
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkObject
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
@OptIn(ExperimentalCoroutinesApi::class)
class LocationServiceStartingProcessorTest {

    private val context = mockk<Context>(relaxed = true)

    @Before
    fun setUp() {
        mockkStatic(ContextCompat::class)
        mockkStatic("com.kamikadze328.memo.utils.permissions.PermissionsUtilsKt")
    }

    @After
    fun tearDown() {
        unmockkStatic(ContextCompat::class)
        unmockkStatic("com.kamikadze328.memo.utils.permissions.PermissionsUtilsKt")
        unmockkObject(Repository)
    }

    @Test
    fun `startService is called only when permissions granted`() = runTest {
        LocationService.isRunning.set(false)

        every { context.isFineLocationGranted() } returns true
        every { context.isPostNotificationsGranted() } returns true

        every { context.applicationContext } returns context

        every { ContextCompat.startForegroundService(any(), any()) } returns mockk()

        mockkObject(Repository)
        coEvery { Repository.collectOpened() } returns flowOf(
            listOf(Memo(1, "T", "D", 0, false, MemoLocation(1.0, 1.0)))
        )

        LocationServiceStartingProcessor.observeAndManageLocationService(context)

        verify(exactly = 1) { ContextCompat.startForegroundService(eq(context), any()) }
    }
}