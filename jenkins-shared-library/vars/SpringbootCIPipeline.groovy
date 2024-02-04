def call() {
    sh "echo Running Maven Build"
    sh "ls  -lrtha"
    sh "chmod 777 mvnw"
    sh "./mvnw clean install"
    sh "ls  -lrtha ./target"
}

def pipeline() {
    pipeline {
        // Ensure that 'agent any' is specified correctly
        agent any

        stages {
            stage('Build') {
                steps {
                    script {
                        echo 'Building...'
                        // Your build steps here
                    }
                }
            }
        }
    }
}



