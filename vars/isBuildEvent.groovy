def Boolean call(String script) {

    def gitCommitMessage = action('git log -1 --pretty=%B')

    if (gitCommitMessage =~ 'Jenkins Build: ') {
        echo('commit true')
        return true
    }
    else {
        echo('commit false')
        return false
    }

}