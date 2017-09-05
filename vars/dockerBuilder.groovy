def call(String repo,String containerName, String user, String password, String buildVersion) {

    def login= action('docker login '+"${repo} "+' -u '+"${user}"+' -p ' +"${password}")
    def tag = action('docker tag '+"${containerName}"+' '+"${repo}"+'/'+"${containerName}"+':'+ buildVersion)
    return login + tag
}
