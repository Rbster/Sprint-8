package com.example.retailer.adapter


interface RetailerConsumer {
    // update upon receiving
    fun receiveAndUpdate(incomingMsg: String)
}