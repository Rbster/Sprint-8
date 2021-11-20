package ru.sberschool.hystrix.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class Element(
    val name: String,
    val url: String
) {
    constructor() : this("", "")
}

data class Ability(
    @JsonProperty("ability")
    val ability: Element,
    @JsonProperty("is_hidden")
    val isHidden: Boolean,
    @JsonProperty("slot")
    val slot: Long
) {
    constructor() : this(Element(), true, 0)
}

data class Pokemon(
    @JsonProperty("abilities")
    val abilities: List<Ability>,
    @JsonProperty("base_experience")
    val baseExperience: Long,
    @JsonProperty("forms")
    val forms: List<Element>
) {
    constructor() : this(listOf(), 1, listOf())
}