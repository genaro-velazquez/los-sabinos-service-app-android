package com.lossabinos.serviceapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class ChecklistViewModel @Inject constructor() : ViewModel() {

    private val _observations = MutableStateFlow("")
    val observations: StateFlow<String> = _observations.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun updateObservations(text: String) {
        if (text.length <= 300) {
            _observations.value = text
        }
    }

    fun updateTaskProgress(serviceId: String, taskId: String, completed: Boolean) {
        viewModelScope.launch {
            try {
                println("✅ Task $taskId: $completed (serviceId: $serviceId)")
                // TODO: Implementar lógica de guardado en BD
            } catch (e: Exception) {
                println("❌ Error: ${e.message}")
            }
        }
    }

    fun capturePhoto(serviceId: String, taskId: String) {
        viewModelScope.launch {
            println("📷 Capturando foto para task $taskId")
            // TODO: Implementar captura de cámara
        }
    }

    fun selectPhoto(serviceId: String, taskId: String) {
        viewModelScope.launch {
            println("📁 Seleccionando foto para task $taskId")
            // TODO: Implementar selección de galería
        }
    }

    fun onContinueClicked() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                println("✅ Guardando checklist...")

                // TODO: Implementar lógica de guardado
                delay(2000)  // Simular demora

                _isLoading.value = false
                println("✅ Checklist guardado exitosamente")
            } catch (e: Exception) {
                _isLoading.value = false
                println("❌ Error: ${e.message}")
            }
        }
    }

}
