import groovy.json.*

def call() {

    echo 'Manage version Start'

    def json = readFile(file:'package.json')
    def props = new JsonSlurperClassic().parseText(json)

    def parser = /(\d+\.)(\d+\.)(\d)/
    def match = (props.version =~ parser)
    def cleanVersion

    if(match.find())
        cleanVersion = match.group()
    else{
        echo "error"
        return "error"
    }
    echo "it matches"

    if (env.TAG_NAME == null || !(env.TAG_NAME ==~ /^v\d+\.\d+\.\d+$/)){

        nextVersion = cleanVersion + '.build-' + env.BUILD_NUMBER

        echo "next build:"+nextVersion

    } else{

        nextVersion = cleanVersion
        echo "next tag: "+nextVersion
    }

    props.version = nextVersion

    def jsonOut = JsonOutput.toJson(props)

    echo jsonOut

    writeFile(file:'package.json', text: jsonOut)

    echo "despues del write"

    return nextVersion

}

