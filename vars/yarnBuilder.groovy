def call() {

    def installRet= action('yarn install')
    def buildRet= action('yarn build')
    return installRet + buildRet
}
