package com.example.retailer.api.distributor

import javax.persistence.*

/**
 * Описание товара
 */
@Entity
data class Item(
    /**
     * Произвольный идентификатор
     */
    @Id
    val id: Long,

    /**
     * Произвольное название
     */
    @Column
    val name: String,
//    @Version
//    val version: Long? = null
) {
//    override fun equals(other: Any?) = when {
//        this === other -> true
//        javaClass != other?.javaClass -> false
//        id != (other as Item).id -> false
//        else -> true
//    }
//
//    override fun hashCode(): Int = id.hashCode()
}