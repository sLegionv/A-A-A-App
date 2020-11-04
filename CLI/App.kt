import data.ExitCodes.*
import db.UserDB
import services.HandlerCLI
import java.math.BigInteger
import java.security.MessageDigest

class App {
    fun run(args: Array<String>): Int {

        val handlerCLI = HandlerCLI()
        val arguments = handlerCLI.parse(args)
        val userDB = UserDB()

        //метод хеширования
        fun md5(password: String): String {
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(1, md.digest(password.toByteArray())).toString(16).padStart(32, '0')
        }


        //метод для проверки формата логина
        fun isLoginValid(login: String): Boolean {
            val mathResult = Regex("[^a-zA-Z0-9]").find(login)
            if (mathResult != null) return true
            return false

        }

        //метод для проверки пароля
        fun validatePassword(passArg: String, passDB: String): Boolean {
            if (md5(md5(passArg) + "SULT") != passDB) return false
            return true
        }


        //функция для аутентификации
        fun authentificate(login: String, pass: String): Int {
            when {
                isLoginValid(login) -> return InvalidLoginForm.exitCode
                userDB.hasLogin(login) -> return UnknownLogin.exitCode
            }
            val passDB = userDB.findPasswordByLogin(login)
            if (validatePassword(pass, passDB) == false) return InvalidPassword.exitCode
            return Success.exitCode
        }

        fun hasAuthentification(): Boolean {
            val code = authentificate(arguments.login.toString(), (arguments.pass.toString()))
            if (code == 2 || code == 3 || code == 4) return false
            return true

        }

//        Проверка на пустоту и справку
        when {
            arguments.hasHelp() -> return Help.exitCode
            arguments.isEmpty() -> return Help.exitCode

        }

        if (hasAuthentification() == false) return (authentificate(arguments.login.toString(), (arguments.pass.toString())))

        return Success.exitCode
    }


}