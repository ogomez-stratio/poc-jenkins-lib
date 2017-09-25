

def call(String path) {

    def build = vars.action("mvn -Dmaven.test.skip -f "+path+"pom.xml clean package docker:build")
    return build
}