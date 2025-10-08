package com.kamikadze328.memo.feature.memo.details.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kamikadze328.memo.core.ui.MemoAppBar
import com.kamikadze328.memo.feature.memo.details.MemoDetailsUiState
import com.kamikadze328.memo.feature.memo.details.MemoDetailsViewModel
import com.kamikadze328.memo.feature.memo.details.R
import com.kamikadze328.memo.navigation.CollectResult
import com.kamikadze328.memo.navigation.choose.location.ChooseLocationArgs
import com.kamikadze328.memo.navigation.choose.location.ChooseLocationResult


@Composable
fun MemoDetailsUi(
    viewModel: MemoDetailsViewModel = hiltViewModel(),
    navController: NavController,
    onChooseLocation: (ChooseLocationArgs) -> Unit,
    onFinish: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    MemoDetailsUi(
        uiState = uiState.value,
        onChooseLocationClick = {
            onChooseLocation(
                ChooseLocationArgs(
                    location = uiState.value.memo.reminderLocation,
                    canChooseLocation = uiState.value.canEdit,
                )
            )
        },
        onSaveClick = viewModel::tryToSaveMemo,
        onNewTitleValue = viewModel::onNewTitleValue,
        onNewDescriptionValue = viewModel::onNewDescriptionValue,
    )

    LaunchedEffect(uiState.value.shouldFinish) {
        if (uiState.value.shouldFinish) {
            onFinish()
        }
    }

    navController.CollectResult<ChooseLocationResult> {
        viewModel.onNewMemoLocation(it.location)
    }
}

@Composable
private fun MemoDetailsUi(
    uiState: MemoDetailsUiState,
    onChooseLocationClick: () -> Unit,
    onSaveClick: () -> Unit,
    onNewTitleValue: (String?) -> Unit = {},
    onNewDescriptionValue: (String?) -> Unit = {},
) {
    Scaffold(
        topBar = {
            MemoAppBar(
                title = if (uiState.canEdit) {
                    stringResource(id = R.string.create_memo_title)
                } else {
                    stringResource(id = R.string.view_memo_title)
                },
                actionTitle = stringResource(id = R.string.action_save).takeIf { uiState.canEdit },
                onActionClick = onSaveClick
            )
        }
    ) { paddingValues ->
        Content(
            uiState = uiState,
            onChooseLocationClick = onChooseLocationClick,
            onNewTitleValue = onNewTitleValue,
            onNewDescriptionValue = onNewDescriptionValue,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun Content(
    uiState: MemoDetailsUiState,
    onChooseLocationClick: () -> Unit,
    onNewTitleValue: (String?) -> Unit,
    onNewDescriptionValue: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .fillMaxSize()
    ) {
        Title(
            uiState = uiState,
            onTextChange = onNewTitleValue,
        )
        Description(
            uiState = uiState,
            onTextChange = onNewDescriptionValue,
        )
        Location(
            uiState = uiState,
            onLocationClick = onChooseLocationClick
        )
    }
}

@Composable
private fun Title(
    uiState: MemoDetailsUiState,
    onTextChange: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val memo = uiState.memo
    val errorText = uiState.titleError
    var title by remember(memo.id) { mutableStateOf(TextFieldValue(memo.title)) }

    Column(
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
                onTextChange(it.text)
            },
            isError = errorText != null,
            enabled = uiState.canEdit,
            label = { Text(stringResource(R.string.memo_title)) },
            modifier = Modifier.fillMaxWidth(),
        )
        if (errorText != null) {
            Text(
                text = errorText,
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.error,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )
        } else {
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun Description(
    uiState: MemoDetailsUiState,
    onTextChange: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val memo = uiState.memo
    val errorText = uiState.textError
    var description by remember(memo.id) { mutableStateOf(TextFieldValue(memo.description)) }

    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = description,
            onValueChange = {
                description = it
                onTextChange(it.text)
            },
            enabled = uiState.canEdit,
            isError = errorText != null,
            label = { Text(stringResource(R.string.memo_text)) },
            modifier = Modifier.fillMaxWidth()
        )
        if (errorText != null) {
            Text(
                text = errorText,
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.error,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )
        } else {
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun Location(
    uiState: MemoDetailsUiState,
    onLocationClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val location = uiState.memo.reminderLocation
    val errorText = uiState.locationError
    val canEdit = uiState.canEdit

    Column(
        modifier = modifier,
    ) {
        Text(
            text = errorText ?: location?.let { "${it.latitude}, ${it.longitude}" }.orEmpty(),
            color = if (errorText != null) MaterialTheme.colors.error else LocalContentColor.current
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = onLocationClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (canEdit) {
                    stringResource(R.string.choose_location)
                } else {
                    stringResource(R.string.see_location)
                }
            )
        }
    }
}