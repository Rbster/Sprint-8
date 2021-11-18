package com.example.retailer.adapter

import com.example.retailer.api.distributor.Order
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired


class DistributorPublisherImpl : DistributorPublisher {
    @Autowired
    private lateinit var template: RabbitTemplate

    @Autowired
    private lateinit var topic: TopicExchange


    override fun placeOrder(order: Order): Boolean {
        if (order.id != null) {
            template.convertAndSend(topic.name, "distributor.placeOrder.Rbster.${order.id}", order) {
                it.messageProperties.headers["Notify-Exchange"] = "distributor_exchange"
                it.messageProperties.headers["Notify-RoutingKey"] = "retailer.Rbster"
                it
            }
            return true
        } else {
            throw Exception("order id == null !")
        }
    }
}