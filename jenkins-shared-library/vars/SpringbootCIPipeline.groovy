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
                    def settingsXmlPath = "${env.WORKSPACE}/.m2/settings.xml"

                // Copy the managed settings.xml to the workspace
                    configFileProvider([configFile(fileId: '24ed4219-1ebb-4416-9f64-420f25a144e4', targetLocation: settingsXmlPath)]) {
                        echo "Copied settings.xml to ${settingsXmlPath}"
                    }
                    mavenbuild()
                      
                }
            }
            stage('Upload to Nexus Artifactory'){
                steps{

                }
            }
            
        }
    }
}

def mavenbuild(){
    script {
        def settingsXmlPath = "${env.WORKSPACE}/.m2/settings.xml"
        echo 'Building...'
        sh "echo Running Maven Build"
        sh "java -version"
        sh "mvn -version"
        sh "mvn -s ${settingsXmlPath} clean install"
        sh "ls  -lrtha ./target"
    }
}

