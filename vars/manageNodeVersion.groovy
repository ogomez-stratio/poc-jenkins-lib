import groovy.json.*

def call(String type) {

    def json = readFile(file:'package.json')
    def props = new JsonSlurperClassic().parseText(json)

    def nextVersion

    def cleanVersion = getCleanVersion(props.version)

    if (cleanVersion == 'error') return 'error'

    switch (type){
        case 'build': nextVersion = cleanVersion + '.build-' + env.BUILD_NUMBER
            break
        case 'RC' :   nextVersion = cleanVersion + '.RC-' + env.BUILD_NUMBER
            break
        case 'version': nextVersion ='v'+cleanVersion
            break
    }




    props.version = nextVersion

    def jsonOut = JsonOutput.toJson(props)

    def prettyOut  = JsonOutput.prettyPrint(jsonOut)

    writeFile(file:'package.json', text: prettyOut)

    return "Version to build: " + nextVersion

}

@NonCPS
def getCleanVersion(String version){

    def parser = /(\d+\.)(\d+\.)(\d+)/
    def match = (version =~ parser)

    if(match.find()) {
        return match.group()
    }
    else return "error"
}
