import groovy.json.*

def call() {

    echo 'Manage version Start'

    def json = readFile(file:'package.json')
    def props = new JsonSlurperClassic().parseText(json)
    def nextVersion

    def parser = /(?<major>\d+).(?<minor>\d+).(?<revision>\d+)-(?<build>\d+)/
    def match = props.version =~ parser


    if(match.matches()) {
        def (major, minor, revision, build) = ['major', 'minor', 'revision', 'build'].collect { match.group(it) }
    }

    echo match

    if (env.TAG_NAME == null || !(env.TAG_NAME ==~ /^v\d+\.\d+\.\d+$/)){

        nextVersion = match + '.build-' + env.BUILD_NUMBER

    } else{

        nextVersion = match
    }

    props.version = nextVersion

    def jsonOut = JsonOutput.toJson(props)

    echo jsonOut

    writeFile(file:'package.json', text: jsonOut)

    return nextVersion
}
