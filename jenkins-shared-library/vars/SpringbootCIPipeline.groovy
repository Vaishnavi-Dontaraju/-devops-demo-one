def call() {
    pipeline {
        // Ensure that 'agent any' is specified correctly
        agent any

        stages {
            stage('Build') {
                steps {
                    mavenbuild()
                      
                }
            }
        }
    }
}

def mavenbuild(){
    script {
        echo 'Building...'
        sh "echo Running Maven Build"
        sh "ls  -lrtha"
        sh "chmod 777 mvnw"
        sh "./mvnw clean install"
        sh "ls  -lrtha ./target"
    }
}

