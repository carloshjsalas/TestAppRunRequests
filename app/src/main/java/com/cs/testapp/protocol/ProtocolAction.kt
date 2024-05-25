package com.cs.testapp.protocol

sealed class ProtocolAction {
    data object OpenRunRequestScreen : ProtocolAction()
}