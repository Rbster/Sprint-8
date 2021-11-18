package com.example.retailer.adapter

import com.example.retailer.api.distributor.OrderInfo
import com.example.retailer.service.OrderService
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired

class RetailerConsumerImpl : RetailerConsumer {
    @Autowired
    lateinit var orderService: OrderService

    @RabbitListener(queues = ["retailer"])
    override fun receiveAndUpdate(orderInfo: OrderInfo) {
        orderService.updateOrderInfo(orderInfo)

    }

}