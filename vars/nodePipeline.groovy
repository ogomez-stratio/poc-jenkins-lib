

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

            stage('Checkout'){

                checkout scm
            }

            stage('build') {
                script {
                    echo yarnBuilder()
                }
            }

            stage('publish build') {

                echo 'Start to push untagged build image to repo'

                withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'docker-credentials',
                                  usernameVariable: 'ACRCRED_USR', passwordVariable: 'ACRCRED_PWD']])

                if (env.TAG_NAME == null || !(env.TAG_NAME ==~ /^v\d+\.\d+\.\d+$/)) {

                    script {
                        echo dockerBuilder("${config.dockerRepo}", env.ACRCRED_USR, env.ACRCRED_PWD,getNodeVersion())
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

        }catch (err){
            currentBuild.result = 'FAILED'
            throw err
        }

    }
}
