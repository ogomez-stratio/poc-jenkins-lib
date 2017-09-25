

def call(String containerName) {

    def build = vars.action('docker build -t '+"${containerName }"+' .')

    return build

}
