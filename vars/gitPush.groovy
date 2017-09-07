def call(String gitRepo, String gitCredentialsId){

    withCredentials([usernamePassword(credentialsId: gitCredentialsId, passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {

        if (env.TAG_NAME == null || !(env.TAG_NAME ==~ /^v\d+\.\d+\.\d+$/)) {

            action('git push https://${GIT_USERNAME}:${GIT_PASSWORD}@'+gitRepo+' master')

        }else {

            action("git tag -a some_tag -m 'Jenkins'")
            action('git push https://${GIT_USERNAME}:${GIT_PASSWORD}@'+gitRepo+'--tags')

        }
    }
}
