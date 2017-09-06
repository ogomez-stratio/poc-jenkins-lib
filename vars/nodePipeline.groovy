

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
                    echo action('pwd&ls -l')
                    echo yarnBuilder()
                }
            }

            stage('publish untagged build') {

                echo env.TAG_NAME

                when {

                    expression { env.TAG_NAME == null || !(env.TAG_NAME ==~ /^v\d+\.\d+\.\d+$/) }
                }
                echo 'Start to push image to repo'
                script {
                    // def props = readJSON file: 'package.json'
                    // def buildversion = props.version + '.build-' + env.BUILD_NUMBER

                    echo dockerBuilder('ogomezstratio', 'test-node-app', 'ogomezstratio', 'og1108al', 'v1.0.0')
                }

                echo 'End push image to repo'
            }

            stage('publish tagged release') {
                when {
                    expression { env.TAG_NAME ==~ /^v\d+\.\d+\.\d+$/ }
                }
                echo 'Start push image to repo 2'

                script {
                    def buildversion = env.TAG_NAME
                    echo dockerBuilder('ogomezstratio', 'test-node-app', 'ogomezstratio', 'og1108al', buildversion)
                    // sh 'docker login sania.azurecr.io -u $ACRCRED_USR -p $ACRCRED_PSW'
                    // sh 'docker tag sanitas-dental-bot-clinics sania.azurecr.io/sanitas-dental-bot-clinics:' + buildversion
                    // sh 'docker push sania.azurecr.io/sanitas-dental-bot-clinics:' + buildversion
                }

                echo 'End push image to repo 2'
            }

        }catch (err){
            currentBuild.result = 'FAILED'
            throw err
        }

    }
}
