def call() {
    pipeline()
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

            stage('Test') {
                steps {
                    script {
                        echo 'Testing...'
                        // Your test steps here
                    }
                }
            }

            stage('Deploy') {
                steps {
                    script {
                        echo 'Deploying...'
                        // Your deployment steps here
                    }
                }
            }
        }

        post {
            always {
                script {
                    echo 'Cleaning up...'
                    // Your cleanup steps here
                }
            }
        }
    }
}



