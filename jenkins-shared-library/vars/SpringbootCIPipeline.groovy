def call() {
    pipeline {
        agent any
         
        // environment {
        //     JDK_VERSION = '21'
        //     MAVEN_VERSION = '3.9.6' 
        // }

        tools {
            jdk "AWSCorretto-21"
            maven "Maven-3.9.6"
        }


        

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
        sh "java -version"
        sh "mvn -version"
        // sh "chmod 777 mvnw"
        // sh "mvnw clean install"
        // sh "ls  -lrtha ./target"
    }
}

