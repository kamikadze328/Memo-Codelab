package com.kamikadze328.memo

import android.location.Location
import com.kamikadze328.memo.model.Memo
import com.kamikadze328.memo.repository.Repository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.intArrayOf


@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
@OptIn(ExperimentalCoroutinesApi::class)
class LocationUpdateProcessorTest {

    private val repo = mockk<Repository>()
    private val processor = LocationUpdateProcessor(repo)
    private val fakeLocation = Location("mock").apply {
        latitude = 41.0; longitude = 41.0
    }

    @Test
    fun `notifies for each nearby memo`() = runTest {
        val memos = listOf(Memo(1, "t", "d", 0, false))
        coEvery { repo.findNearMemoByFlatDistance(any(), any(), any()) } returns memos
        val called = mutableListOf<Memo>()

        processor.onLocationUpdate(fakeLocation) { called += it }

        assertEquals(memos, called)
        coVerify { repo.findNearMemoByFlatDistance(any(), any(), any()) }
    }
}
