package com.voloaccendo.androiddemo.ui.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.voloaccendo.androiddemo.data.models.ThemeLighting

@Composable
fun ThemeLightingRadioButton(themeLighting: ThemeLighting, onThemeChanged: (themeLighting: ThemeLighting) -> Unit) {
    val themeOptions = enumValues<ThemeLighting>()
    var selectedTheme by remember { mutableStateOf(themeLighting) }

    Column(modifier = Modifier.fillMaxWidth()) {
        themeOptions.forEach { theme ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (theme == selectedTheme),
                        onClick = { selectedTheme = theme
                                   onThemeChanged(theme)},
                        role = Role.RadioButton
                    )
                    .padding(8.dp)
            ) {
                RadioButton(selected = (theme == selectedTheme), onClick = null)
                Text(text = theme.name.lowercase().replaceFirstChar { it.uppercase() })
            }
        }
    }
}
