import data.ExitCodes.*
import db.tableUsers
import services.HandlerCLI
import java.math.BigInteger
import java.security.MessageDigest

class App {
    /* метод для проверки формата логина */
    private fun isLoginValid(login: String): Boolean {
        val mathResult = Regex("[^a-zA-Z0-9]").find(login)
        if (mathResult != null) return true
        return false

    }
    private fun md5(password: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(password.toByteArray())).toString(16).padStart(32, '0')
    }
    /* функция для аутентификации */
    fun authentificate(login: String, pass: String): Int {

        if (isLoginValid(login)) return INVALID_LOGIN_FORM.exitCode

        for (user in tableUsers) {
            if (user.login == login) {
                return if (user.hashPassword == md5(md5(pass) + user.salt))
                    SUCCESS.exitCode
                else
                    INVALID_PASSWORD.exitCode
            }

        }
        return UNKNOWN_LOGIN.exitCode
    }


    fun run(args: Array<String>): Int {

        val handlerCLI = HandlerCLI()
        val arguments = handlerCLI.parse(args)

        /* Проверка на пустоту и справку */
        when {
            arguments.hasHelp() -> return HELP.exitCode
            arguments.isEmpty() -> return HELP.exitCode
            !arguments.hasAuthentification() -> return authentificate(arguments.login.toString(), arguments.pass.toString())

        }


        return SUCCESS.exitCode
    }
}