package com.kamikadze328.memo

import com.kamikadze328.memo.model.MemoLocation
import com.kamikadze328.memo.view.create.location.ChooseLocationArgs
import com.kamikadze328.memo.view.create.location.ChooseLocationViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ChooseLocationViewModelTest {

    private val vm = ChooseLocationViewModel()

    @Test fun `updateLocation updates flow value`() = runTest {
        vm.updateLocation(10.0, 20.0)
        assertEquals(MemoLocation(10.0, 20.0), vm.data.value)
    }

    @Test fun `initArgs overrides location if provided`() = runTest {
        val args = ChooseLocationArgs(MemoLocation(30.0, 40.0))
        vm.initArgs(args)
        assertEquals(30.0, vm.data.value.latitude, 0.001)
    }
}
