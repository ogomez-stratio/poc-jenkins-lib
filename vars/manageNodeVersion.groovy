import groovy.json.*

def call() {

    echo 'Manage version Start'

    def json = readFile(file:'package.json')
    def props = new JsonSlurperClassic().parseText(json)
    def nextVersion

    echo 'antes del if'

    if (env.TAG_NAME == null || !(env.TAG_NAME ==~ /^v\d+\.\d+\.\d+$/)){

        nextVersion = props.version + '.build-' + env.BUILD_NUMBER
    } else{
        nextVersion = env.TAG_NAME
    }
    new File("package.json").write(props.toPrettyString())

    return nextVersion
}