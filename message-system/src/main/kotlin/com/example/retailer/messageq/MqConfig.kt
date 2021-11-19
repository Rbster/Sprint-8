package com.example.retailer.messageq

import com.example.retailer.adapter.DistributorPublisher
import com.example.retailer.adapter.DistributorPublisherImpl
import com.example.retailer.adapter.RetailerConsumer
import com.example.retailer.adapter.RetailerConsumerImpl
import org.springframework.amqp.core.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class MqConfig {
    @Bean
    fun topic(): TopicExchange {
        return TopicExchange("distributor_exchange", true, false)
    }
    @Bean
    fun autoDeleteRetailerQueue(): Queue {
        return Queue("retailer", false, false, true)
    }
    @Bean
    fun bindingRetailer(
        topic: TopicExchange,
        autoDeleteRetailerQueue: Queue
    ): Binding {
        return BindingBuilder.bind(autoDeleteRetailerQueue)
            .to(topic)
            .with("retailer.Rbster.#")
    }
    @Bean
    fun consumer(): RetailerConsumer {
        return RetailerConsumerImpl()
    }
    @Bean
    fun publisher(): DistributorPublisher {
        return DistributorPublisherImpl()
    }
}


