import groovy.json.JsonSlurperClassic

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

    echo nextVersion

    props.version = nextVersion
    props.store(props.newWriter(), null)

    return nextVersion
}
