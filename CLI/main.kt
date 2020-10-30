import data.ExitCodes.Success
val ROLES = arrayOf("READ", "WRITE", "EXECUTE")

fun main(args: Array<String>) {
    val app = App()
    app.run(args)
    System.exit(Success.exitCode)
}