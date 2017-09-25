

def call(String path) {

    def cleanRet= vars.action("mvn -f "+path+"pom.xml clean install")
    return cleanRet
}