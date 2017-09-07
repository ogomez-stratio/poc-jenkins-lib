def call(String gitRepo, String gitCredentialsId){

    withCredentials([usernamePassword(credentialsId: gitCredentialsId, passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {


        action('git remote add https://'+gitRepo)
        action('git commit https://${GIT_USERNAME}:${GIT_PASSWORD}@'+gitRepo+' -m \"Jenkins Build: '+env.BUILD_NUMBER+' \"')
        action('git push https://${GIT_USERNAME}:${GIT_PASSWORD}@'+gitRepo+' master')


        if (env.TAG_NAME == null || !(env.TAG_NAME ==~ /^v\d+\.\d+\.\d+$/)) {

            action("git tag -a some_tag -m 'Jenkins'")
            action('git push https://${GIT_USERNAME}:${GIT_PASSWORD}@' + gitRepo + '--tags')
        }

    }
}
