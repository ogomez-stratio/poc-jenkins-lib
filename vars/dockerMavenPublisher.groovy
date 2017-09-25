

def call(String dockerRepo,String containerName, String dockerUser, String dockerPassword, String version) {

    if (dockerRepo != null || dockerRepo == '') {
        login = vars.action('docker login ' + ' -u ' + "${dockerUser}" + ' -p ' + "${dockerPassword}")
        tag = vars.action('docker tag ' + "${containerName}" + ' ' + "${dockerUser}" + '/' + "${containerName}" + ':' + version)
        push = vars.action('docker push ' + "${dockerUser}" + '/' + "${containerName}" + ':' + version)
    } else{
        login = vars.action('docker login ' + ' -u '+"${dockerRepo}" + "${dockerUser}" + ' -p ' + "${dockerPassword}")
        tag = vars.action('docker tag ' + "${containerName}" + ' ' + "${dockerRepo}" + '/' + "${containerName}" + ':' + version)
        push = vars.action('docker push ' + "${dockerRepo}" + '/' + "${containerName}" + ':' + version)
    }
    return login + tag + push
}