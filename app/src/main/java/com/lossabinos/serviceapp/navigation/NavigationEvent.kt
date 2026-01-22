package com.lossabinos.serviceapp.navigation

sealed class NavigationEvent {
    object NavigateToHome : NavigationEvent()
    object NavigateToForgotPassword : NavigationEvent()
    object ShowLogoutConfirmation : NavigationEvent()
    object NavigateToLogin : NavigationEvent()
    object NavigateToNotifications : NavigationEvent()
    data class NavigateToChecklistProgress(val serviceId: String) : NavigationEvent()
    data class NavigateToVehicleRegistration(val serviceId: String) : NavigationEvent()
}