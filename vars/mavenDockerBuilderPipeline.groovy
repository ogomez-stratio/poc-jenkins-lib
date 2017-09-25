

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

                def result = vars.manageMavenVersion("build", "${config.pomPath}")

                if (result == 'error') {

                    echo "incorrect version format in package.json"
                    currentBuild.result = 'FAILED'
                    throw new Exception("Invalid version format in package.xml")

                } else {

                    env.NEXT_VERSION = result
                    echo "Next Build Version: "+ result
                }

            }

            /**
             * Maven Test Stage
             */
            stage('Maven Test') {

                echo vars.mavenBuilder("${config.pomPath}")
            }

            /**
             * Build, Tag and publish the Docker Image into Docker Repository (Currently Azure Container Registry)
             */
            stage('publish build to develop') {

                withCredentials([usernamePassword(credentialsId: "${config.registryCredentialsId}",
                        usernameVariable: 'ACR_USR', passwordVariable: 'ACR_PWD')]) {
                    echo 'Start to push image to repo'
                    script {
                        echo vars.dockerMavenPublisher("${config.pomPath}")
                        echo vars.dockerMavenBuilder("${config.dockerBuildRepo}", "${config.containerName}", "${env.ACR_USR}",
                                "${env.ACR_PWD}", env.NEXT_VERSION)
                    }
                    echo 'End to push image to repo'

                }
            }

            /**
             * Build, Tag and publish the Docker Image into Docker Repository (Currently Azure Container Registry)
             */
            stage('publish RC Build') {

                env.NEXT_VERSION = vars.manageMavenVersion("RC", "${config.pomPath}")

                withCredentials([usernamePassword(credentialsId: "${config.registryCredentialsId}",
                        usernameVariable: 'ACR_USR', passwordVariable: 'ACR_PWD')]) {
                    echo 'Start to push image to repo'
                    script {
                        milestone 1
                        timeout(time: 10, unit: 'MINUTES') {
                            input message: "Does Pre-Production look good?"
                        }
                        // this will kill any job which is still in the input step
                        echo vars.dockerMavenPublisher("${config.dockerRcRepo}", "${config.containerName}", "${env.ACR_USR}",
                                "${env.ACR_PWD}", env.NEXT_VERSION,"${config.pomPath}")

                        milestone 2
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