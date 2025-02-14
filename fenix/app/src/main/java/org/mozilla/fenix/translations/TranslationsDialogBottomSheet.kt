/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.translations

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import org.mozilla.fenix.R
import org.mozilla.fenix.compose.BetaLabel
import org.mozilla.fenix.compose.LinkText
import org.mozilla.fenix.compose.LinkTextState
import org.mozilla.fenix.compose.annotation.LightDarkPreview
import org.mozilla.fenix.compose.button.PrimaryButton
import org.mozilla.fenix.compose.button.TextButton
import org.mozilla.fenix.theme.FirefoxTheme

/**
 * Firefox Translations bottom sheet dialog.
 */
@Composable
fun TranslationsDialogBottomSheet(
    learnMoreUrl: String,
    showFirstTimeTranslation: Boolean,
    onSettingClicked: () -> Unit,
    onLearnMoreClicked: () -> Unit,
    onTranslateButtonClick: () -> Unit,
) {
    var orientation by remember { mutableIntStateOf(Configuration.ORIENTATION_PORTRAIT) }

    val configuration = LocalConfiguration.current

    LaunchedEffect(configuration) {
        snapshotFlow { configuration.orientation }.collect { orientation = it }
    }

    Column(
        modifier = Modifier.padding(16.dp),
    ) {
        BetaLabel(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .clearAndSetSemantics {},
        )

        TranslationsDialogHeader(onSettingClicked, showFirstTimeTranslation)

        Spacer(modifier = Modifier.height(8.dp))

        if (showFirstTimeTranslation) {
            TranslationsDialogInfoMessage(onLearnMoreClicked, learnMoreUrl)
        }

        Spacer(modifier = Modifier.height(14.dp))

        when (orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                TranslationsDialogContentInLandscapeMode(onTranslateButtonClick)
            }

            else -> {
                TranslationsDialogContentInPortraitMode(onTranslateButtonClick)
            }
        }
    }
}

@Composable
private fun TranslationsDialogContentInPortraitMode(onTranslateButtonClick: () -> Unit) {
    Column {
        TranslationsDropdown(
            header = stringResource(id = R.string.translations_bottom_sheet_translate_from),
        )

        Spacer(modifier = Modifier.height(16.dp))

        TranslationsDropdown(
            header = stringResource(id = R.string.translations_bottom_sheet_translate_to),
        )

        Spacer(modifier = Modifier.height(16.dp))

        TranslationsDialogActionButtons(onTranslateButtonClick)
    }
}

@Composable
private fun TranslationsDialogContentInLandscapeMode(onTranslateButtonClick: () -> Unit) {
    Column {
        Row {
            TranslationsDropdown(
                modifier = Modifier.weight(1f),
                header = stringResource(id = R.string.translations_bottom_sheet_translate_from),
            )
            Spacer(modifier = Modifier.width(16.dp))

            TranslationsDropdown(
                modifier = Modifier.weight(1f),
                header = stringResource(id = R.string.translations_bottom_sheet_translate_to),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TranslationsDialogActionButtons(onTranslateButtonClick)
    }
}

@Composable
private fun TranslationsDialogHeader(
    onSettingClicked: () -> Unit,
    showFirstTimeTranslation: Boolean,
) {
    val title: String = if (showFirstTimeTranslation) {
        stringResource(
            id = R.string.translations_bottom_sheet_title_first_time,
            stringResource(id = R.string.firefox),
        )
    } else {
        stringResource(id = R.string.translations_bottom_sheet_title)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            modifier = Modifier
                .weight(1f)
                .semantics { heading() },
            color = FirefoxTheme.colors.textPrimary,
            style = FirefoxTheme.typography.headline7,
        )

        Spacer(modifier = Modifier.width(4.dp))

        IconButton(
            onClick = { onSettingClicked() },
            modifier = Modifier.size(24.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.mozac_ic_settings_24),
                contentDescription = stringResource(id = R.string.translation_option_bottom_sheet_title),
                tint = FirefoxTheme.colors.iconPrimary,
            )
        }
    }
}

@Composable
private fun TranslationsDialogInfoMessage(
    onLearnMoreClicked: () -> Unit,
    learnMoreUrl: String,
) {
    val learnMoreText =
        stringResource(id = R.string.translations_bottom_sheet_info_message_learn_more)
    val learnMoreState = LinkTextState(
        text = learnMoreText,
        url = learnMoreUrl,
        onClick = { onLearnMoreClicked() },
    )

    Box {
        LinkText(
            text = stringResource(
                R.string.translations_bottom_sheet_info_message,
                learnMoreText,
            ),
            linkTextStates = listOf(learnMoreState),
            style = FirefoxTheme.typography.subtitle1.copy(
                color = FirefoxTheme.colors.textPrimary,
            ),
            linkTextDecoration = TextDecoration.Underline,
        )
    }
}

@Composable
private fun TranslationsDropdown(
    header: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = header,
            color = FirefoxTheme.colors.textPrimary,
            style = FirefoxTheme.typography.caption,
        )

        Row {
            Text(
                text = "English",
                modifier = Modifier.weight(1f),
                color = FirefoxTheme.colors.textPrimary,
                style = FirefoxTheme.typography.subtitle1,
            )

            Spacer(modifier = Modifier.width(10.dp))

            Icon(
                painter = painterResource(id = R.drawable.mozac_ic_dropdown_arrow),
                contentDescription = null,
                tint = FirefoxTheme.colors.iconPrimary,
            )
        }

        Divider(color = FirefoxTheme.colors.formDefault)
    }
}

@Composable
private fun TranslationsDialogActionButtons(onTranslateButtonClick: () -> Unit) {
    val isTranslationInProgress = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextButton(
            text = stringResource(id = R.string.translations_bottom_sheet_negative_button),
            modifier = Modifier,
            onClick = {},
        )
        Spacer(modifier = Modifier.width(10.dp))

        if (isTranslationInProgress.value) {
            DownloadIndicator(
                text = stringResource(id = R.string.translations_bottom_sheet_translating_in_progress),
                contentDescription = stringResource(
                    id = R.string.translations_bottom_sheet_translating_in_progress_content_description,
                ),
                icon = painterResource(id = R.drawable.mozac_ic_sync_24),
            )
        } else {
            PrimaryButton(
                text = stringResource(id = R.string.translations_bottom_sheet_positive_button),
                modifier = Modifier.wrapContentSize(),
            ) {
                isTranslationInProgress.value = true
                onTranslateButtonClick()
            }
        }
    }
}

@Composable
@LightDarkPreview
private fun TranslationsDialogBottomSheetPreview() {
    FirefoxTheme {
        TranslationsDialogBottomSheet(
            learnMoreUrl = "",
            showFirstTimeTranslation = true,
            onSettingClicked = {},
            onLearnMoreClicked = {},
            onTranslateButtonClick = {},
        )
    }
}
