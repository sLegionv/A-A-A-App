fun main(args: Array<String>) {
    val app = App()
    val exitCode = app.run(args)
    println(exitCode)
    System.exit(exitCode)
}