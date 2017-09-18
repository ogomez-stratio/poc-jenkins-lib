def Boolean call(String script) {

    def gitCommitMessage = action('git log -1 --pretty=%B')

    if (gitCommitMessage =~ 'Jenkins Build: ')
        return true
    else
        return false

}