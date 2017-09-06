def call(String dockerRepo,String containerName, String dockerUser, String dockerPassword, String version) {

    def build = action('docker build -t '+"${containerName }"+' .')
    if (dockerRepo != null || dockerRepo == '') {
        def login = action('docker login ' + ' -u ' + "${dockerUser}" + ' -p ' + "${dockerPassword}")
        def tag = action('docker tag ' + "${containerName}" + ' ' + "${dockerRepo}" + '/' + "${containerName}" + ':' + version)
        def push = action('docker push ' + "${dockerRepo}" + '/' + "${containerName}" + ':' + version)
    } else{
        def login = action('docker login ' + ' -u '+"${dockerRepo}" + "${dockerUser}" + ' -p ' + "${dockerPassword}")
        def tag = action('docker tag ' + "${containerName}" + ' ' + "${dockerRepo}" + '/' + "${containerName}" + ':' + version)
        def push = action('docker push ' + "${dockerRepo}" + '/' + "${containerName}" + ':' + version)
    }
    return build + login + tag + push
}
