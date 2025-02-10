package de.marionoll.wgautoconnect.home.features.inversemode

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.marionoll.wgautoconnect.home.Event
import de.marionoll.wgautoconnect.service.NetworkMonitorServiceHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class InverseModeViewModel
@Inject constructor(
    private val inverseModeDataStore: DataStore<Boolean?>,
    private val serviceHandler: NetworkMonitorServiceHandler,
) : ViewModel() {

    private var handleInverseModeToggle : Job? = null

    private val stateFlow = MutableStateFlow(
        InverseModeViewState(
            runOnConnectionEstablished = false
        )
    )

    val viewState = combine(stateFlow, inverseModeDataStore.data) { state, isInverseModeActive ->
        InverseModeViewState(
            runOnConnectionEstablished = isInverseModeActive ?: false,
        )
    }

    fun onEvent(event: Event) {
        if (event is Event.InverseModeToggle) {
            handleInverseModeToggle?.cancel()
            handleInverseModeToggle = viewModelScope.launch {
                serviceHandler.stop()
                // Get current toggle value and flip it. currentVal = !currentVal.
                val currentVal: Boolean? = inverseModeDataStore.data.first();
                inverseModeDataStore.updateData { currentVal != true }
            }
        }
    }

}