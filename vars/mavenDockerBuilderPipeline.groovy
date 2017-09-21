def call(body) {

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

            stage('Version Management') {

                def result = manageMavenVersion("${config.buildType}", "${config.pomPath}")

                if (result == 'error') {

                    echo "incorrect version format in package.json"
                    currentBuild.result = 'FAILED'
                    throw new Exception("Invalid version format in package.json")

                } else {

                    env.NEXT_VERSION = result
                    echo result
                }

            }

            /**
             * Install yarn dependencies and build the proyect with the new version.
             */
            stage('build & unit test') {
                script {
                    echo mavenBuilder("${config.pomPath}")
                }
            }

            /**
             * TODO TEST PARALLEL STAGE
             */

            /**
             * Build, Tag and publish the Docker Image into Docker Repository (Currently Azure Container Registry)
             */
            stage('publish build') {

                withCredentials([usernamePassword(credentialsId: "${config.registryCredentialsId}",
                        usernameVariable: 'ACR_USR', passwordVariable: 'ACR_PWD')]) {
                    echo 'Start to push image to repo'
                    script {
                        echo dockerMavenBuilder("${config.dockerRepo}", "${config.containerName}", "${env.ACR_USR}",
                                "${env.ACR_PWD}", env.NEXT_VERSION,"${config.pomPath}")
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