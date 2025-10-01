package com.kamikadze328.memo.feature.memo.create

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kamikadze328.memo.domain.model.Memo
import com.kamikadze328.memo.feature.choose.location.ChooseLocationArgs
import com.kamikadze328.memo.feature.choose.location.ChooseLocationContract
import com.kamikadze328.memo.feature.memo.create.databinding.ActivityCreateMemoBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Activity that allows a user to create a new Memo.
 */
@AndroidEntryPoint
class CreateMemoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateMemoBinding
    private val viewModel: CreateMemoViewModel by viewModels()

    private val chooseLocationLauncher =
        registerForActivityResult(ChooseLocationContract()) { result ->
            viewModel.onNewMemoLocation(result.location)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.data.collect { uiState ->
                    updateUI(uiState)
                }
            }
        }

        initData(viewModel.data.value)
        setupListeners()
    }

    private fun updateUI(memo: Memo) {
        updateLocationInfo(memo)
    }

    private fun initData(memo: Memo) {
        with(binding.contentCreateMemo) {
            memoTitle.setText(memo.title)
            memoDescription.setText(memo.description)
        }
        updateLocationInfo(memo)
    }

    private fun updateLocationInfo(memo: Memo) {
        with(binding.contentCreateMemo) {
            chooseLocationText.text =
                memo.reminderLocation?.let { "${it.latitude}, ${it.longitude}" }
        }
    }

    private fun setupListeners() {
        with(binding.contentCreateMemo) {
            chooseLocationButton.setOnClickListener {
                chooseLocationLauncher.launch(ChooseLocationArgs(viewModel.data.value.reminderLocation))
            }
            memoTitle.doAfterTextChanged {
                viewModel.onNewTitleValue(it?.toString())
            }
            memoDescription.doAfterTextChanged {
                viewModel.onNewDescriptionValue(it?.toString())
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_create_memo, menu)
        return true
    }

    /**
     * Handles actionbar interactions.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                saveMemo()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Saves the memo if the input is valid; otherwise shows the corresponding error messages.
     */
    private fun saveMemo() {
        with(binding.contentCreateMemo) {
            if (viewModel.isMemoValid()) {
                viewModel.saveMemo()
                setResult(RESULT_OK)
                finish()
            } else {
                memoTitleContainer.error =
                    getErrorMessage(viewModel.hasTitleError(), R.string.memo_title_empty_error)
                memoDescriptionContainer.error =
                    getErrorMessage(viewModel.hasTextError(), R.string.memo_text_empty_error)

                if (viewModel.hasLocationError()) {
                    chooseLocationText.text = getString(R.string.memo_location_empty_error)
                }
            }
        }
    }

    /**
     * Returns the error message if there is an error, or an empty string otherwise.
     *
     * @param hasError          - whether there is an error.
     * @param errorMessageResId - the resource id of the error message to show.
     * @return the error message if there is an error, or an empty string otherwise.
     */
    private fun getErrorMessage(hasError: Boolean, @StringRes errorMessageResId: Int): String {
        return getString(errorMessageResId).takeIf { hasError }.orEmpty()
    }
}
