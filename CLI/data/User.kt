package data

data class User(
        val id: Int,
        val login: String,
        val Password: String,
        val salt: String
)