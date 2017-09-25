

def call(String containerName) {

    def build = action('docker build -t '+"${containerName }"+' .')

    return build

}
