def call() {
    pipeline {
        agent any
         
        tools {
            jdk 'AWSCorretto-21'
            maven 'Maven-3.9.6'
        }

        // environment {
        //     JDK_TOOL = 'AWSCorretto-21'
        //     //MAVEN_VERSION = '3.9.6' 
        // }
        
        stages {
            stage('Maven Build') {
                steps {
                    mavenbuild()
                }
            }
                       
        }
    }
}

def mavenbuild(){
    script {
        configFileProvider([configFile(fileId: '24ed4219-1ebb-4416-9f64-420f25a144e4', targetLocation: "${env.WORKSPACE}/.m2/settings.xml")]) {
            echo "Copied settings.xml to ${env.WORKSPACE}/.m2/settings.xml"
        }
        echo 'Building...'
        sh "echo Running Maven Build"
        sh "java -version"
        sh "mvn -version"
        sh "echo env.WORKSPACE=${env.WORKSPACE}"
        sh "mvn -s ${env.WORKSPACE}/.m2/settings.xml -Dmaven.repo.local=${env.WORKSPACE}/.m2/repository clean install"
        sh "ls  -lrtha ./target"
    }
}

