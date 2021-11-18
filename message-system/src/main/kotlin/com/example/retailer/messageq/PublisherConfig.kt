package com.example.retailer.messageq

import com.example.retailer.adapter.DistributorPublisher
import com.example.retailer.adapter.DistributorPublisherImpl
import com.example.retailer.adapter.RetailerConsumer
import com.example.retailer.adapter.RetailerConsumerImpl
import org.springframework.amqp.core.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class PublisherConfig {
    @Bean
    fun topic(): TopicExchange {
        return TopicExchange("distributor_exchange", false, true)
    }

    @Bean
    fun autoDeleteRetailerQueue(): Queue {
        return Queue("retailer", false, false, true)
    }

//    @Bean
//    fun autoDeleteDistributorQueue(): Queue {
//        return Queue("distributor", false, false, true)
//    }

    @Bean
    fun bindingRetailer(
        topic: TopicExchange,
        autoDeleteRetailerQueue: Queue
    ): Binding {
        return BindingBuilder.bind(autoDeleteRetailerQueue)
            .to(topic)
            .with("retailer.Rbster.#")
    }

//    @Bean
//    fun bindingDistributor(
//        topic: TopicExchange,
//        autoDeleteDistributorQueue: Queue
//    ): Binding {
//        return BindingBuilder.bind(autoDeleteDistributorQueue)
//            .to(topic)
//            .with("distributor.placeOrder.Rbster.#")
//    }
    @Bean
    fun consumer(): RetailerConsumer {
        return RetailerConsumerImpl()
    }

    @Bean
    fun publisher(): DistributorPublisher {
        return DistributorPublisherImpl()
    }
    
}


