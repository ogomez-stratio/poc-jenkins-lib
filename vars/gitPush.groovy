def call(String gitRepo, String gitCredentialsId){

    withCredentials([usernamePassword(credentialsId: gitCredentialsId, passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {

        //def add = action('git add .')
        def commit = action('git commit -m \"Jenkins Build: '+env.BUILD_NUMBER+' \"')
        def status = action('git status')
        def remote = action('git remote -v')

        return add + commit + status + remote
    }
}
