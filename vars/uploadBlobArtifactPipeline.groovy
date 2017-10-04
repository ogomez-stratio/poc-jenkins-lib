def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
    node {
        currentBuild.result = 'SUCCESS'
        try {

            /**
             * Upload given artifact to azure blob storage
             */
             stage('upload artifact to azure') {
                  def pompath = "${config.pompath}"
                  def pom = readMavenPom file: pompath+'/pom.xml'
                  def artifactName = pom.artifactId+"-"+pom.version+".jar"

                 echo artifactName

                  azureUpload blobProperties: [cacheControl: '', contentEncoding: '', contentLanguage: '', contentType: '', detectContentType: true],
                              containerName: "${config.containername}",
                              fileShareName: '',
                              filesPath: "${config.localpath}"+"/"+ artifactName,
                              storageCredentialId: "${config.storagecreds}",
                              storageType: 'blobstorage',
                              uploadArtifactsOnlyIfSuccessful: true,
                              virtualPath: env.BRANCH_NAME+"/"+artifactName
                  }
        } catch (err) {
            currentBuild.result = 'FAILED'
            throw err
        }
    }
}
