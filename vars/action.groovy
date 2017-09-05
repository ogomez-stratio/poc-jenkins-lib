def call(String script) {

        def shStdout = sh returnStdout: true, script: "${script}"
        return shStdout

}