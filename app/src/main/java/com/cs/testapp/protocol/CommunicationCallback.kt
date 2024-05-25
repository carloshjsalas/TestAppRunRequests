package com.cs.testapp.protocol

interface CommunicationCallback {
    fun onFragmentEvent(action: ProtocolAction)
}