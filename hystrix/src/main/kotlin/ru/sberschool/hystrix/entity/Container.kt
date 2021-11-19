package ru.sberschool.hystrix.entity

import com.fasterxml.jackson.annotation.JsonProperty

//data class Container(
//    val count: Long,
//    val next: String,
//    val previous: String,
//    val results: List<Element>
//)
//
data class Element(
    val name: String,
    val url: String
) {
    constructor() : this("", "")
}

data class Ability(
//    @JsonProperty("")
    val ability: Element,
//    @JsonProperty("is_hidden")
    val hidden: Boolean,
//    @JsonProperty("slot")
    val slot: Long
) {
    constructor() : this(Element(), true, 0)
}

data class Pokemon(
    val abilities: List<Ability>,
    val baseExperience: Long,
    val forms: List<Element>
) {
    constructor() : this(listOf(), 1, listOf())
}