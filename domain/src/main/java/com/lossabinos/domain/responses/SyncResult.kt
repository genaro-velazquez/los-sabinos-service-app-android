package com.lossabinos.domain.responses

sealed class SyncResult {
    object Success : SyncResult()
    object AlreadySynced : SyncResult()
    data class Error(val message: String) : SyncResult()
}