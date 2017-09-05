def call(String repo,String containerName, String user, String password, String buildVersion) {

    def build = action('docker build -t '+"${containerName }"+' .')
    def login= action('docker login '+' -u '+"${user}"+' -p ' +"${password}")
    def tag = action('docker tag '+"${containerName}"+' '+"${repo}"+'/'+"${containerName}"+':'+ getNodeVersion())
    def push = action('docker push '+"${repo}"+'/'+"${containerName}"+':'+getNodeVersion())
    return build + login + tag + push
}
