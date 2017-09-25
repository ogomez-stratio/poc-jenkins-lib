

def call() {

    def installRet= vars.action('yarn install')
    def buildRet= vars.action('yarn build')
    return installRet + buildRet
}
