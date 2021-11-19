package com.example.retailer.storage.repository

import com.example.retailer.api.distributor.Order
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface OrderRepository : CrudRepository<Order, String> {}