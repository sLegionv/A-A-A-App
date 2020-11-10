import data.ExitCodes.*
import services.DatabaseWrapper
import services.HandlerCLI
import java.math.BigInteger
import java.security.MessageDigest

class App {
    private val db = DatabaseWrapper()

    /* метод хеширования */
    private fun md5(password: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(password.toByteArray())).toString(16).padStart(32, '0')
    }

    /* метод для проверки формата логина */
    private fun isLoginValid(login: String): Boolean {
        val mathResult = Regex("[^a-zA-Z0-9]").find(login)
        if (mathResult != null) return true
        return false

    }

    //Проверка прошла аутентификация или нет
    private fun hasAuthent2(login: String, pass: String): Boolean {
        when (authentificate(login, pass)) {
            2 -> return false
            3 -> return false
            4 -> return false
        }
        return true
    }

    private fun authentificate(login: String, pass: String): Int {
        val user = db.getUser(login)
        when {
            isLoginValid(login) -> return INVALID_LOGIN_FORM.exitCode
            !user.isInvalidUser() -> return UNKNOWN_LOGIN.exitCode
        }
        return if (user.hashPassword == md5(md5(pass) + user.salt))
            SUCCESS.exitCode
        else
            INVALID_PASSWORD.exitCode
    }

    fun run(args: Array<String>): Int {

        val handlerCLI = HandlerCLI()
        val arguments = handlerCLI.parse(args)

        /* Проверка на пустоту и справку */

        when {
            arguments.hasHelp() -> return HELP.exitCode
            arguments.isEmpty() -> return HELP.exitCode
            !hasAuthent2(arguments.login.toString(), arguments.pass.toString()) -> return authentificate(arguments.login.toString(), arguments.pass.toString())

        }


        return SUCCESS.exitCode
    }


}