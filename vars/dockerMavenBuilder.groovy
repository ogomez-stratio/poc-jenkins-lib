def call(String dockerRepo,String containerName, String dockerUser, String dockerPassword, String version, String path) {

    def login
    def tag
    def push
    def build = action("mvn -f "+path+"pom.xml package docker:build")
    if (dockerRepo != null || dockerRepo == '') {
        login = action('docker login ' + ' -u ' + "${dockerUser}" + ' -p ' + "${dockerPassword}")
        tag = action('docker tag ' + "${containerName}" + ' ' + "${dockerUser}" + '/' + "${containerName}" + ':' + version)
        push = action('docker push ' + "${dockerUser}" + '/' + "${containerName}" + ':' + version)
    } else{
        login = action('docker login ' + ' -u '+"${dockerRepo}" + "${dockerUser}" + ' -p ' + "${dockerPassword}")
        tag = action('docker tag ' + "${containerName}" + ' ' + "${dockerRepo}" + '/' + "${containerName}" + ':' + version)
        push = action('docker push ' + "${dockerRepo}" + '/' + "${containerName}" + ':' + version)
    }
    return build + login + tag + push
}