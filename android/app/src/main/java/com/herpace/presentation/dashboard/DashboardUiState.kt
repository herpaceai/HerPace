package com.herpace.presentation.dashboard

import com.herpace.domain.model.TrainingPlan
import com.herpace.domain.model.TrainingSession

data class DashboardUiState(
    val todaySession: TrainingSession? = null,
    val activePlan: TrainingPlan? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
