import jenkins.model

@Library('jenkinslib') _

pipeline {
    agent any
    // environment {
    // //   ACRCRED = credentials('sania_azure_container_registry')
    //
    // }
    stages {
        stage('build') {
            steps {
                script {
                    echo yarnBuilder()
                }
            }
        }

        stage('publish untagged build') {
            when {
                expression { env.TAG_NAME == null || !(env.TAG_NAME ==~ /^v\d+\.\d+\.\d+$/) }
            }
            steps {
                echo 'Start push image to repo'

                script {
                    // def props = readJSON file: 'package.json'
                    // def buildversion = props.version + '.build-' + env.BUILD_NUMBER
                    echo dockerBuilder('ogomezstratio','test-node-app','ogomezstratio','og1108al','v1.0.0')
                }

                echo 'End push image to repo'
            }
        }

        stage('publish tagged release') {
            when {
                expression { env.TAG_NAME ==~ /^v\d+\.\d+\.\d+$/ }
            }
            steps {
                echo 'Start push image to repo 2'

                script {
                    def buildversion = env.TAG_NAME
                    echo dockerBuilder('ogomezstratio','test-node-app','ogomezstratio','og1108al',buildversion)
                    // sh 'docker login sania.azurecr.io -u $ACRCRED_USR -p $ACRCRED_PSW'
                    // sh 'docker tag sanitas-dental-bot-clinics sania.azurecr.io/sanitas-dental-bot-clinics:' + buildversion
                    // sh 'docker push sania.azurecr.io/sanitas-dental-bot-clinics:' + buildversion
                }

                echo 'End push image to repo 2'
            }
        }
    }

    post {
        success {
            echo("correcto")
            //slackSend (channel: '#datalake-jenkins', color: '#00FF00', message: 'SUCCESSFUL \n\t Build Number: ' + env.BUILD_NUMBER + '\n\t Job: ' + env.JOB_NAME + '\n\t Build Url: ' + env.BUILD_URL)
        }

        failure {
            echo("error")
            //slackSend (channel: '#datalake-jenkins', color: '#FF0000', message: 'FAILURE \n\t Build Number: ' + env.BUILD_NUMBER + '\n\t Job: ' + env.JOB_NAME + '\n\t Build Url: ' + env.BUILD_URL)
        }
    }
}