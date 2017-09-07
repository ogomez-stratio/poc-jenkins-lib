import groovy.json.*

def call() {

    echo "test"
    def version = '10.01.03-13'

    def parser = /(?<major>\d+).(?<minor>\d+).(?<revision>\d+)-(?<build>\d+)/

    def match = version =~ parser
    if(match.matches()) {
        def (major, minor, revision, build) = ['major', 'minor', 'revision', 'build'].collect{match.group(version)}
    }

    echo "fin test"

    echo 'Manage version Start'

    def json = readFile(file:'package.json')
    def props = new JsonSlurperClassic().parseText(json)
    def nextVersion = null

    def regExp = /(\d+).(\d+).(\d+)/
    def matcher = props.version =~ parser

    echo match.toString()

    def cleanVersion

    echo "if match"

    if(match.matches()) {

        echo "it matches"

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
