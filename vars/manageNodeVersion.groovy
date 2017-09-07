import groovy.json.*

def call() {

    echo 'Manage version Start'

    def json = readFile(file:'package.json')
    def props = new JsonSlurperClassic().parseText(json)
    def nextVersion = null

    def parser = /(\d+\.)(\d+\.)(\d)/
    def match = props.version =~ parser

    if(match.matches()) {


        echo "it matches"

        def v = match.group()

        if (env.TAG_NAME == null || !(env.TAG_NAME ==~ /^v\d+\.\d+\.\d+$/)){

            nextVersion = v + '.build-' + env.BUILD_NUMBER

            echo "next build:"+nextVersion

        } else{

            nextVersion = v
            echo "next tag: "+nextVersion
        }

        props.version = nextVersion

        def jsonOut = JsonOutput.toJson(props)

        echo jsonOut

        writeFile(file:'package.json', text: jsonOut)

        echo "despues del write"

        return nextVersion

    } else {

        echo "error"

        return "error"
    }


}
