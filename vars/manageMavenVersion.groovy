import groovy.json.*

def call(String type,String path) {

    def pom = readMavenPom file: path+'pom.xml'
    env.POM_VERSION = pom.version

    def nextVersion

    def cleanVersion = getCleanVersion(env.POM_VERSION)

    if (cleanVersion == 'error') return 'error'

    switch (type){
        case 'build': nextVersion = cleanVersion + '.build-' + env.BUILD_NUMBER
            break
        case 'RC' :   nextVersion = cleanVersion + '.RC-' + env.BUILD_NUMBER
            break
        case 'version': nextVersion ='v'+cleanVersion
            break
    }

    return nextVersion

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