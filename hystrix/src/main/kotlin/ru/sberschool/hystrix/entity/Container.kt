package ru.sberschool.hystrix.entity

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
)

data class Ability(
    val ability: Element,
    val isHidden: Boolean,
    val slot: Long
)

data class Pokemon(
    val abilities: List<Ability>,
    val baseExperience: Long,
    val forms: List<Element>
)