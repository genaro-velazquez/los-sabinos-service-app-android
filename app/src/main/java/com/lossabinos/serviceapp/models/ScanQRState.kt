package com.lossabinos.serviceapp.models

enum class ScanQRState {
    INITIAL,    // Solo botón
    VALID,      // Botón + formulario
    INVALID     // Botón + Error
}