import groovy.json.JsonSlurperClassic

def call(){

    echo 'Manage version Start'

    def json = readFile(file:'package.json')
    def props = new JsonSlurperClassic().parseText(json)
    def nextVersion

    if (env.TAG_NAME == null || !(env.TAG_NAME ==~ /^v\d+\.\d+\.\d+$/)){

        nextVersion = props.version + + '.build-' + env.BUILD_NUMBER
    } else{
        nextVersion = env.TAG_NAME
    }

    props.setProperty('version', nextVersion)
    props.store(propsFile.newWriter(), null)

    return nextVersion
}
