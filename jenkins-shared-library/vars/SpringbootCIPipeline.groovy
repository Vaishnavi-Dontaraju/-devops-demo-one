def call(){
    pipeline()
}
def pipeline(){
    pipeline {
        agent any
        stages {
            stage('Maven Build') {
                steps {
                    script {
                        sh "echo Running Maven Build"
                        sh "ls  -lrtha"
                        sh "chmod 777 mvnw"
                        sh "./mvnw clean install"
                        sh "ls  -lrtha ./target"
                    }
                }
            }          
        }
    }

}



