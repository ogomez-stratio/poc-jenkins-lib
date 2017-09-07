import groovy.json.*

def call() {

    echo 'Manage version Start'

    def json = readFile(file:'package.json')
    def props = new JsonSlurperClassic().parseText(json)
    def nextVersion

    echo 'antes del if'

    if (env.TAG_NAME == null || !(env.TAG_NAME ==~ /^v\d+\.\d+\.\d+$/)){

        cleanVersion =(props.version =~ /^\d+\.\d+\.\d+$/)
        echo cleanVersion
        nextVersion = cleanVersion + '.build-' + env.BUILD_NUMBER
    } else{
        nextVersion = env.TAG_NAME
    }

    props.version = nextVersion

    def jsonOut = JsonOutput.toJson(props)

    echo jsonOut

    writeFile(file:'package.json', text: jsonOut)

    return nextVersion
}
