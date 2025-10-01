package com.kamikadze328.memo.feature.memo.details

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kamikadze328.memo.domain.model.Memo
import com.kamikadze328.memo.feature.choose.location.ChooseLocationArgs
import com.kamikadze328.memo.feature.choose.location.ChooseLocationContract
import com.kamikadze328.memo.feature.memo.details.databinding.ActivityViewMemoBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


/**
 * Activity that allows a user to see the details of a memo.
 */
@AndroidEntryPoint
class ViewMemoActivity : AppCompatActivity() {
    companion object {
        const val BUNDLE_MEMO_ID = "memoId"
    }

    private lateinit var binding: ActivityViewMemoBinding
    private val viewModel: ViewMemoViewModel by viewModels()
    private val locationLauncher = registerForActivityResult(ChooseLocationContract(), {})

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.memo.collect { value ->
                    value?.let { memo ->
                        updateUI(memo)
                    }
                }
            }
        }
        if (savedInstanceState == null) {
            val id = intent?.data?.getQueryParameter("id")?.toLongOrNull()
                ?: intent.getLongExtra(BUNDLE_MEMO_ID, -1)
            viewModel.loadMemo(id)
        }
        initUi()
    }

    private fun initUi() {
        with(binding.contentCreateMemo) {
            memoTitle.isEnabled = false
            memoDescription.isEnabled = false
        }
    }

    /**
     * Updates the UI with the given memo details.
     *
     * @param memo - the memo whose details are to be displayed.
     */
    private fun updateUI(memo: Memo) {
        binding.contentCreateMemo.run {
            memoTitle.setText(memo.title)
            memoDescription.setText(memo.description)

            val location = memo.reminderLocation
            if (location != null) {
                chooseLocationText.text = getString(
                    R.string.location_data,
                    location.latitude,
                    location.longitude
                )
                chooseLocationButton.text = getString(R.string.see_location)
                chooseLocationButton.setOnClickListener {
                    locationLauncher.launch(
                        ChooseLocationArgs(
                            location = location,
                            canChooseLocation = false,
                        )
                    )
                }
            } else {
                chooseLocationButton.isVisible = false
                chooseLocationText.isVisible = false
            }
        }
    }
}
