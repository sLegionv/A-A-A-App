package db

import data.User

class UserDB {

    val Users: Array<User> = arrayOf(
            User(1, "vasya", "dbf8a44e276bc5d8b9bf0c92e03e50a5", "SULT"),

            User(2, "admin", "73ae280de8ae9dbd4bee81cb615e832d", "SULT"))

    //есть ли такой логин
    fun hasLogin(login: String): Boolean {
        if (Users.any { it.login == login }) return false
        return true
    }

    //найти пароль по логину
    fun findPasswordByLogin(login: String): String {
        val pass = ""
        Users.forEach { if (it.login == login) return it.Password }
        return pass
    }

}