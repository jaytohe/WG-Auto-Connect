package de.marionoll.wgautoconnect.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.marionoll.wgautoconnect.home.Event
import de.marionoll.wgautoconnect.home.HomeViewState

@Composable
fun InverseMode(
    viewState: HomeViewState.Content,
    onEvent: (Event) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Switch(
            checked = viewState.inverseModeViewState.runOnConnectionEstablished,
            onCheckedChange = { onEvent(Event.InverseModeToggle)}
        )

        Spacer(modifier = Modifier.size(8.dp))

        Text(text = "Run on connection established: ${if (viewState.inverseModeViewState.runOnConnectionEstablished) "ON" else "OFF"}")
    }
}