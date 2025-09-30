package com.kamikadze328.memo.feature.home

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kamikadze328.memo.background.location.LocationService
import com.kamikadze328.memo.core.android.permissions.isAllPermissionsGranted
import com.kamikadze328.memo.domain.model.Memo
import com.kamikadze328.memo.feature.home.databinding.ActivityHomeBinding
import com.kamikadze328.memo.feature.memo.create.CreateMemoActivity
import com.kamikadze328.memo.feature.memo.details.ViewMemoActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * The main activity of the app. Shows a list of recorded memos and lets the user add new memos.
 */
@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private var binding: ActivityHomeBinding? = null
    private val viewModel: HomeViewModel by viewModels()
    private var menuItemShowAll: MenuItem? = null
    private var menuItemShowOpen: MenuItem? = null
    private val createMemoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.refreshMemos()
            }
        }

    private val locationPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.all { it.value }) {
                startLocationService()
            } else {
                toast(getString(R.string.no_location_permissions_warning))
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbar)

        requestPermissions()

        // Setup the adapter and the recycler view
        setupRecyclerView(initializeAdapter())

        binding?.fab?.setOnClickListener {
            // Handles clicks on the FAB button > creates a new Memo
            createMemoLauncher.launch(Intent(this@HomeActivity, CreateMemoActivity::class.java))
        }
        viewModel.loadOpenMemos()
    }

    private fun startLocationService() {
        Intent(applicationContext, LocationService::class.java).apply {
            startService(this)
        }
    }

    private fun requestPermissions() {
        if (!isAllPermissionsGranted()) {
            val permissions = buildList {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    add(Manifest.permission.POST_NOTIFICATIONS)
                }
                add(Manifest.permission.ACCESS_FINE_LOCATION)
                add(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
            locationPermissionsLauncher.launch(permissions.toTypedArray())
            return
        }
        startLocationService()
    }

    /**
     * Initializes the adapter and sets the needed callbacks.
     */
    private fun initializeAdapter(): MemoAdapter {
        val adapter = MemoAdapter(mutableListOf(), { view ->
            // Implementation for when the user selects a row to show the detail view
            showMemo((view.tag as Memo).id)
        }, { checkbox, isChecked ->
            // Implementation for when the user marks a memo as completed
            viewModel.updateMemo(checkbox.tag as Memo, isChecked)
            viewModel.refreshMemos()
        })
        lifecycle.coroutineScope.launch {
            viewModel.memos.collect { memos ->
                adapter.setItems(memos)
            }
        }
        return adapter
    }

    /**
     * Opens the Memo detail view for the given memoId.
     *
     * @param memoId    - the id of the memo to be shown.
     */
    private fun showMemo(memoId: Long) {
        val intent = Intent(this@HomeActivity, ViewMemoActivity::class.java)
        intent.putExtra(ViewMemoActivity.BUNDLE_MEMO_ID, memoId)
        startActivity(intent)
    }

    /**
     * Initializes the recycler view to display the list of memos.
     */
    private fun setupRecyclerView(adapter: MemoAdapter) {
        with(binding?.contentHome?.recyclerView ?: return) {
            layoutManager =
                LinearLayoutManager(this@HomeActivity, LinearLayoutManager.VERTICAL, false)
            this.adapter = adapter
            addItemDecoration(
                DividerItemDecoration(
                    this@HomeActivity,
                    (layoutManager as LinearLayoutManager).orientation
                )
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        menuItemShowAll = menu.findItem(R.id.action_show_all)
        menuItemShowOpen = menu.findItem(R.id.action_show_open)
        return true
    }

    /**
     * Handles actionbar interactions.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_show_all -> {
                viewModel.loadAllMemos()
                //Switch available menu options
                menuItemShowAll?.isVisible = false
                menuItemShowOpen?.isVisible = true
                true
            }

            R.id.action_show_open -> {
                viewModel.loadOpenMemos()
                //Switch available menu options
                menuItemShowOpen?.isVisible = false
                menuItemShowAll?.isVisible = true
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}
