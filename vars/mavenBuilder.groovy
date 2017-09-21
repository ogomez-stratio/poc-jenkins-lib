def call(String path) {

    def cleanRet= action("mvn -f "+path+"pom.xml clean install")
    return cleanRet
}