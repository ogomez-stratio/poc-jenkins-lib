def call() {
 node {
     def props = readJSON file: 'package.json'
     def buildversion = props.version + '.build-' + env.BUILD_NUMBER
 }
    return buildversion
}