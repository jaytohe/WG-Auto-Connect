package de.marionoll.wgautoconnect.home.features.inversemode

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.marionoll.wgautoconnect.home.Event
import de.marionoll.wgautoconnect.service.NetworkMonitorServiceHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class InverseModeViewModel
@Inject constructor(
    private val inverseModeDataStore: DataStore<Boolean>,
    private val serviceHandler: NetworkMonitorServiceHandler,
) : ViewModel() {

    private var handleInverseModeToggle : Job? = null

    /*
    private val stateFlow = MutableStateFlow(
        InverseModeViewState(
            runOnConnectionEstablished = false
        )
    )
    val viewState = combine(stateFlow, inverseModeDataStore.data) { state, isInverseModeActive ->
        InverseModeViewState(
            runOnConnectionEstablished = isInverseModeActive
        )
    }*/

    // Convert DataStore Flow to a StateFlow
    val viewState: StateFlow<InverseModeViewState> = inverseModeDataStore.data
        .map { isInverseModeActive : Boolean -> InverseModeViewState(runOnConnectionEstablished = isInverseModeActive)}
        .stateIn(viewModelScope, SharingStarted.Lazily, InverseModeViewState(runOnConnectionEstablished = false))

    fun onEvent(event: Event) {
        if (event is Event.InverseModeToggle) {
            handleInverseModeToggle?.cancel()
            handleInverseModeToggle = viewModelScope.launch {
                serviceHandler.stop()
                // Get current toggle value and flip it. currentVal = !currentVal.
                inverseModeDataStore.updateData {currentVal -> !currentVal}
            }
        }
    }

}