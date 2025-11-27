package com.lossabinos.serviceapp.navigation

sealed class NavigationEvent {
    object NavigateToHome : NavigationEvent()
    object NavigateToForgotPassword : NavigationEvent()
    object ShowLogoutConfirmation : NavigationEvent()  // ✅ NUEVO: Mostrar modal de logout
    object NavigateToLogin : NavigationEvent()         // ✅ NUEVO: Navegar a Login después de logout

}