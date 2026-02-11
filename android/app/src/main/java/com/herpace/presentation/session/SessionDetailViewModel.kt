package com.herpace.presentation.session

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herpace.data.remote.ApiResult
import com.herpace.domain.repository.TrainingPlanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionDetailViewModel @Inject constructor(
    private val trainingPlanRepository: TrainingPlanRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val sessionId: String = checkNotNull(savedStateHandle.get<String>("sessionId"))

    private val _uiState = MutableStateFlow(SessionDetailUiState())
    val uiState: StateFlow<SessionDetailUiState> = _uiState.asStateFlow()

    init {
        loadSession()
    }

    fun loadSession() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val session = trainingPlanRepository.getSessionById(sessionId)
            if (session != null) {
                _uiState.update { it.copy(session = session, isLoading = false) }
            } else {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Session not found")
                }
            }
        }
    }

    fun markCompleted() {
        viewModelScope.launch {
            _uiState.update { it.copy(isMarkingComplete = true) }
            when (trainingPlanRepository.markSessionCompleted(sessionId)) {
                is ApiResult.Success -> {
                    loadSession()
                    _uiState.update { it.copy(isMarkingComplete = false) }
                }
                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isMarkingComplete = false,
                            errorMessage = "Failed to mark session as completed"
                        )
                    }
                }
                is ApiResult.NetworkError -> {
                    _uiState.update {
                        it.copy(
                            isMarkingComplete = false,
                            errorMessage = "Network error. Please try again."
                        )
                    }
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
