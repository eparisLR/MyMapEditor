package model
enum class Type {
    TARGET, CHECKPOINT, ATTACK, BONUS
}

data class Point(
    val name: String? = "point",
    val type:Type? =  null,
    val latitude: Double,
    val longitude: Double,
)
