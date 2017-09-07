

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

            /**
             * Checkout from scm to prepare the workspace for the pipeline
             */
            stage('Checkout') {

                checkout scm
            }

            /**
             * Calculate the next version (build or tag) and write it in the package.json
             */

            stage('Version Management'){

             if (!manageNodeVersion()){
                 echo "incorrect version format in package.json"
                 currentBuild.result = 'FAILED'
             }

            }


            /**
             * Install yarn dependencies and build the proyect with the new version.
             */
            stage('build') {
                script {
                    echo yarnBuilder()
                }
            }

            /**
             * TODO TEST PARALLEL STAGE
            */


            /**
             * Build, Tag and publish the Docker Image into Docker Repository (Currently Azure Container Registry)
             */
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

            /**
             * Push the new version to git repository.
             * Example of new build 1.0.1.build-83
             * Example of new TAG (stable version capable promote to productive environments
             */
            stage('Push new version to Git') {


            }

        } catch (err) {
            currentBuild.result = 'FAILED'
            throw err
        }

    }
}
