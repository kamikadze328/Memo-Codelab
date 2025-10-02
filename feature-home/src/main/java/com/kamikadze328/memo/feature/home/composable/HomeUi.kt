package com.kamikadze328.memo.feature.home.composable

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kamikadze328.memo.background.location.LocationService
import com.kamikadze328.memo.core.android.permissions.isAllPermissionsGranted
import com.kamikadze328.memo.core.ui.MemoAppBar
import com.kamikadze328.memo.core.ui.theme.AppTheme
import com.kamikadze328.memo.domain.model.Memo
import com.kamikadze328.memo.feature.home.HomeUiState
import com.kamikadze328.memo.feature.home.HomeViewModel
import com.kamikadze328.memo.feature.home.R
import com.kamikadze328.memo.feature.memo.create.CreateMemoActivity
import com.kamikadze328.memo.feature.memo.details.ViewMemoActivity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun HomeUi(
    viewModel: HomeViewModel = viewModel(),
) {
    val context = LocalContext.current
    val createMemoLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            viewModel.refreshMemos()
        }
    }
    val viewMemoLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {}

    val permissionsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            context.startLocationService()
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.no_location_permissions_warning),
                Toast.LENGTH_SHORT
            ).show()
            context.openAppSettings()
        }
    }

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    HomeUi(
        uiState = uiState.value,
        onClickMemo = {
            viewMemoLauncher.launch(
                Intent(context, ViewMemoActivity::class.java).apply {
                    putExtra(ViewMemoActivity.BUNDLE_MEMO_ID, it.id)
                }
            )
        },
        onAddMemo = {
            createMemoLauncher.launch(
                Intent(context, CreateMemoActivity::class.java)
            )
        },
        onShowAllClick = viewModel::onShowAllClick,
        onShowOpenClick = viewModel::onShowOpenClick,
        onUpdateMemo = viewModel::onUpdateMemo
    )

    LaunchedEffect(Unit) {
        if (!context.isAllPermissionsGranted()) {
            val permissions = buildList {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    add(Manifest.permission.POST_NOTIFICATIONS)
                }
                add(Manifest.permission.ACCESS_FINE_LOCATION)
                add(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
            permissionsLauncher.launch(permissions.toTypedArray())
        } else {
            context.startLocationService()
        }
    }
}

private fun Context.startLocationService() {
    LocationService.startService(this)
}

private fun Context.openAppSettings() {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    )
    startActivity(intent)
}

@Composable
private fun HomeUi(
    uiState: HomeUiState,
    onClickMemo: (Memo) -> Unit,
    onAddMemo: () -> Unit,
    onUpdateMemo: (Memo, Boolean) -> Unit,
    onShowAllClick: () -> Unit,
    onShowOpenClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            MemoAppBar(
                title = stringResource(id = R.string.home_title),
                actionTitle = if (uiState.shouldShowAll) {
                    stringResource(R.string.action_show_open)
                } else {
                    stringResource(R.string.action_show_all)
                },
                onActionClick = if (uiState.shouldShowAll) onShowOpenClick else onShowAllClick,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddMemo,
                backgroundColor = MaterialTheme.colors.secondary
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                )
            }
        }
    ) { paddingValues ->
        Content(
            uiState = uiState,
            onClickMemo = onClickMemo,
            onUpdateMemo = onUpdateMemo,
            modifier = Modifier.padding(paddingValues),
        )
    }
}

@Composable
private fun Content(
    uiState: HomeUiState,
    onClickMemo: (Memo) -> Unit,
    onUpdateMemo: (Memo, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    MemoList(
        memos = uiState.memos.toImmutableList(),
        onClickMemo = onClickMemo,
        onToggleDone = onUpdateMemo,
        modifier = modifier
            .fillMaxSize()
    )
}

@Composable
private fun MemoList(
    memos: ImmutableList<Memo>,
    onClickMemo: (Memo) -> Unit,
    onToggleDone: (Memo, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        items(items = memos, key = { it.id }) { memo ->
            MemoRow(
                memo = memo,
                onClick = { onClickMemo(memo) },
                onCheckedChange = { checked -> onToggleDone(memo, checked) }
            )
            Divider()
        }
    }
}

@Composable
private fun MemoRow(
    memo: Memo,
    onClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text(
                text = memo.title,
                style = MaterialTheme.typography.h6,
                overflow = TextOverflow.Ellipsis,
                maxLines = 3,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = memo.description,
                style = MaterialTheme.typography.body1,
                overflow = TextOverflow.Ellipsis,
                maxLines = 3,
            )

        }

        Checkbox(
            checked = memo.isDone,
            onCheckedChange = onCheckedChange,
            enabled = !memo.isDone,
        )
    }
}

@Composable
@Preview
private fun Preview() {
    AppTheme {
        HomeUi(
            uiState = HomeUiState(
                memos = listOf(
                    Memo(
                        id = 0,
                        title = "Title",
                        description = "Description",
                        reminderDate = 0,
                        reminderLocation = null,
                        isDone = true,
                    ),
                    Memo(
                        id = 1,
                        title = "Title123",
                        description = "Description123",
                        reminderDate = 0,
                        reminderLocation = null,
                        isDone = false,
                    ),
                ),
                shouldShowAll = false,
            ),
            onClickMemo = {},
            onAddMemo = {},
            onUpdateMemo = { _, _ -> },
            onShowAllClick = {},
            onShowOpenClick = {},
        )
    }
}