import groovy.json.*

def call() {

    echo 'Manage version Start'

    def json = readFile(file:'package.json')
    def props = new JsonSlurperClassic().parseText(json)
    def nextVersion = null

    def parser = /(\d+).(\d+).(\d+)/
    def match = props.version =~ parser

    def cleanVersion

    echo "if match"
    if(match.matches()) {

        echo "it matches"

        echo match[0]

        cleanVersion = match[0]


        if (env.TAG_NAME == null || !(env.TAG_NAME ==~ /^v\d+\.\d+\.\d+$/)){

            nextVersion = cleanVersion + '.build-' + env.BUILD_NUMBER

        } else{

            nextVersion = cleanVersion
        }

        echo nextVersion

        props.version = nextVersion

        def jsonOut = JsonOutput.toJson(props)

        echo jsonOut

        writeFile(file:'package.json', text: jsonOut)

        return true

    } else {

        return false
    }


}
