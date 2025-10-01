package com.kamikadze328.memo.view.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kamikadze328.memo.R
import com.kamikadze328.memo.databinding.ActivityViewMemoBinding
import com.kamikadze328.memo.model.Memo
import com.kamikadze328.memo.view.create.location.ChooseLocationArgs
import com.kamikadze328.memo.view.create.location.ChooseLocationContract
import kotlinx.coroutines.launch

internal const val BUNDLE_MEMO_ID: String = "memoId"

/**
 * Activity that allows a user to see the details of a memo.
 */
internal class ViewMemo : AppCompatActivity() {

    private lateinit var binding: ActivityViewMemoBinding
    private val locationLauncher = registerForActivityResult(ChooseLocationContract(), {})

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        // Initialize views with the passed memo id
        val model = ViewModelProvider(this)[ViewMemoViewModel::class.java]
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.memo.collect { value ->
                    value?.let { memo ->
                        // Update the UI whenever the memo changes
                        updateUI(memo)
                    }
                }
            }
        }
        if (savedInstanceState == null) {
            val id = intent?.data?.getQueryParameter("id")?.toLongOrNull()
                ?: intent.getLongExtra(BUNDLE_MEMO_ID, -1)
            model.loadMemo(id)
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
            if (memo.location != null) {
                chooseLocationText.text = getString(
                    R.string.location_data,
                    memo.location.latitude,
                    memo.location.longitude
                )
                chooseLocationButton.text = getString(R.string.see_location)
                chooseLocationButton.setOnClickListener {
                    locationLauncher.launch(
                        ChooseLocationArgs(
                            location = memo.location,
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
