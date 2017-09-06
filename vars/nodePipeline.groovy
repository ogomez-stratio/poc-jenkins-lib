

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

                stage('build') {
                    script {
                        echo yarnBuilder()
                    }
                }

                stage('publish build') {

                    withCredentials([usernamePassword(credentialsId: 'docker-credentials',
                            usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                        //available as an env variable, but will be masked if you try to print it out any which way
                        sh 'echo $PASSWORD'
                        echo "${env.USERNAME}"
                    }

                    echo 'Start to push untagged build image to repo'

                    if (env.TAG_NAME == null || !(env.TAG_NAME ==~ /^v\d+\.\d+\.\d+$/)) {

                        script {
                            echo dockerBuilder("${config.dockerRepo}", "${env.USERNAME}","${env.PASSWORD}", getNodeVersion())
                        }

                        echo 'End push untagged build image to repo'
                    } else {

                        echo 'Start push tagged build image to repo'

                        script {
                            def buildversion = env.TAG_NAME
                            echo dockerBuilder('ogomezstratio', 'test-node-app', 'ogomezstratio', 'og1108al')
                        }

                        echo 'End push tagged build image to repo 2'
                    }
                }

            } catch (err) {
                currentBuild.result = 'FAILED'
                throw err
            }

        }
}
