def call() {
    sh "Running Maven Build"
    sh "ls  -lrtha"
    sh "chmod 777 mvnw"
    sh "./mvnw clean install"
    sh "ls  -lrtha ./target"
}
