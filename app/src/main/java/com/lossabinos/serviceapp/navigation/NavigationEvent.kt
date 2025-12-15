package com.lossabinos.serviceapp.navigation

sealed class NavigationEvent {
    object NavigateToHome : NavigationEvent()
    object NavigateToForgotPassword : NavigationEvent()
    object ShowLogoutConfirmation : NavigationEvent()
    object NavigateToLogin : NavigationEvent()

    data class NavigateToChecklistProgress(val serviceId: String) : NavigationEvent()
}