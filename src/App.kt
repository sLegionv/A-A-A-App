import data.ExitCodes.*
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


    fun run(args: Array<String>): Int {

        val handlerCLI = HandlerCLI()
        val arguments = handlerCLI.parse(args)

        /* Проверка на пустоту и справку */
        when {
            arguments.hasHelp() -> return HELP.exitCode
            arguments.isEmpty() -> return HELP.exitCode

        }


        return SUCCESS.exitCode
    }
}