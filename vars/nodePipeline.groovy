

def call(body) {
//    stage('Promote to Production repo') {
//        milestone label: 'promote to production'
//        input 'Promote this build to Production?'
//
//        node {
//            Artifactory.server(getArtifactoryServerID()).promote([
//                    'buildName'  : build_name,
//                    'buildNumber': build_number,
//                    'targetRepo' : target_repo,
//                    'sourceRepo' : source_repo,
//                    'copy'       : true,
//            ])
//        }
//    }

//    agent any
//    // environment {
//    // //   ACRCRED = credentials('sania_azure_container_registry')
//    //
//    // }

    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    node {

        currentBuild.result = 'SUCCESS'


        try {

            stage('Checkout') {

                checkout scm
            }

            stage('Version Management'){

              echo manageNodeVersion()
            }

            stage('build') {
                script {
                    echo yarnBuilder()
                }
            }

            stage('publish build') {

                withCredentials([usernamePassword(credentialsId: 'docker-credentials',
                        usernameVariable: 'ACR_USR', passwordVariable: 'ACR_PWD')]) {
                    echo 'Start to push image to repo'
                    script {
                        echo dockerBuilder("${config.dockerRepo}","${config.containerName}", "${env.ACR_USR}",
                                "${env.ACR_PWD}", getNodeVersion())
                    }
                    echo 'End to push image to repo'

                }
            }

        } catch (err) {
            currentBuild.result = 'FAILED'
            throw err
        }

    }
}
