package com.example.retailer.adapter

import com.example.retailer.api.distributor.OrderInfo

interface RetailerConsumer {
    // update upon receiving
    fun receiveAndUpdate(orderInfo: OrderInfo)
}