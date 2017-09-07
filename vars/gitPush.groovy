def call(String gitRepo, String gitCredentialsId){

    withCredentials([usernamePassword(credentialsId: gitCredentialsId, passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {

        action('git add .')
        action('git commit -m \"Jenkins Build: '+env.BUILD_NUMBER+' \"')
        action('git push https://${GIT_USERNAME}:${GIT_PASSWORD}@'+gitRepo+' master')

    }
}
