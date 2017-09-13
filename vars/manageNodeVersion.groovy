import groovy.json.*

def call() {

    def json = readFile(file:'package.json')
    def props = new JsonSlurperClassic().parseText(json)

    def nextVersion

    def cleanVersion = getCleanVersion(props.version)

    echo env.TAG_NAME

    if (cleanVersion == 'error') return 'error'

    if (env.TAG_NAME == null || !(env.TAG_NAME ==~ /^v\d+\.\d+\.\d+$/))

        nextVersion = cleanVersion + '.build-' + env.BUILD_NUMBER

    else
        nextVersion ='v'+cleanVersion

    props.version = nextVersion

    def jsonOut = JsonOutput.toJson(props)

    def prettyOut  = JsonOutput.prettyPrint(jsonOut)

    writeFile(file:'package.json', text: prettyOut)

    return "Version to build: " + nextVersion

}

@NonCPS
def getCleanVersion(String version){

    def parser = /(\d+\.)(\d+\.)(\d)/
    def match = (version =~ parser)

    if(match.find()) {
        return match.group()
    }
    else return "error"
}
