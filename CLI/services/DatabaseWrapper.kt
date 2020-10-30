package services

import data.ExitCodes
import data.RoleResource
import data.User
import db.tableRolesResources
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

    private fun checkAccess(roleResource: RoleResource): Boolean {
        val response = tableRolesResources.filter {
            it["role"] == roleResource.role && it.getValue("idUser").toInt() == roleResource.idUser
                    && checkResourceAccess(roleResource.resource, it.getValue("resource"))
        }
        if (response.isEmpty())
            return false
        return true
    }

    private fun checkResourceAccess(resource: String, realResource: String): Boolean {
        val resourcesSequence = resource.split(".").toSet()
        val realResourcesSequence = realResource.split(".").toSet()
        if (resourcesSequence.intersect(realResourcesSequence).isEmpty())
            return false
        return true
    }
}