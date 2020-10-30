package services

import data.ExitCodes
import data.User
import db.tableUsers

class DatabaseWrapper {

    fun getUser(login: String): User {
        val user = tableUsers.find { it["login"] == login }
        if (user == null) {
            terminate(exitCode = ExitCodes.UnknownLogin.exitCode, printHelp = false)
        }
        val id: Int = user!!.getValue("id").toInt()
        val hashPassword: String = user.getValue("hashPassword")
        val salt: String = user.getValue("salt")
        return User(id, login, hashPassword, salt)
    }

}