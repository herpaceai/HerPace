package com.herpace.presentation.session

import com.herpace.domain.model.TrainingSession

data class SessionDetailUiState(
    val session: TrainingSession? = null,
    val isLoading: Boolean = false,
    val isMarkingComplete: Boolean = false,
    val errorMessage: String? = null
)
