package com.unlam.tupartidito.data.model.sport

class SportProvider {
    companion object {
        fun getSports(): List<Sport> {
            return sports
        }
        private val sports = listOf<Sport>(
            Sport(1,"url1"),
            Sport(2,"url2"),
            Sport(3,"url3"),
        )
    }
}