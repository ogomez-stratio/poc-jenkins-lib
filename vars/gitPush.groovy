def call(String gitRepo, String gitCredentialsId){

    withCredentials([usernamePassword(credentialsId: gitCredentialsId, passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {

        def commit = action('git commit -a -m \"Jenkins Build: '+env.BUILD_NUMBER+' \"')
        def status = action('git status')
        def push = action('git push https://${GIT_USERNAME}:${GIT_PASSWORD}@'+gitRepo)

        return commit + status + push

    }
}
