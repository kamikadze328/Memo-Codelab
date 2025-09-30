package com.kamikadze328.memo.feature.memo.create

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.kamikadze328.memo.feature.choose.location.ChooseLocationContract
import com.kamikadze328.memo.feature.memo.create.databinding.ActivityCreateMemoBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity that allows a user to create a new Memo.
 */
@AndroidEntryPoint
class CreateMemoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateMemoBinding
    private val viewModel: CreateMemoViewModel by viewModels()

    private val chooseLocationLauncher =
        registerForActivityResult(ChooseLocationContract()) { location ->
            viewModel.updateMemoLocation(location)
            updateLocationInfo()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setupClickListeners()
        initData()
    }

    private fun initData() {
        updateLocationInfo()
    }

    private fun updateLocationInfo() {
        with(binding.contentCreateMemo) {
            chooseLocationText.text =
                viewModel.getMemoLocation()?.let { "${it.latitude}, ${it.longitude}" }
        }
    }

    private fun setupClickListeners() {
        binding.contentCreateMemo.chooseLocationButton.setOnClickListener {
            chooseLocationLauncher.launch(viewModel.getMemoLocation())
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
        binding.contentCreateMemo.run {
            viewModel.updateMemo(memoTitle.text.toString(), memoDescription.text.toString())
            if (viewModel.isMemoValid()) {
                viewModel.saveMemo()
                setResult(RESULT_OK)
                finish()
            } else {
                memoTitleContainer.error =
                    getErrorMessage(viewModel.hasTitleError(), R.string.memo_title_empty_error)
                memoDescription.error =
                    getErrorMessage(viewModel.hasTextError(), R.string.memo_text_empty_error)
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
