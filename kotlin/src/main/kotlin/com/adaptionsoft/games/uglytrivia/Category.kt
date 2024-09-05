package com.adaptionsoft.games.uglytrivia

enum class Category {
    Pop, Science, Sports, Rock;
}

fun categoryByIndex(category: Int): String = when (category) {
    0, 4, 8 -> "Pop"
    1, 5, 9 -> "Science"
    2, 6, 10 -> "Sports"
    else -> "Rock"
}