def call() {
    pipeline {
        agent any
         
        tools {
            jdk 'AWSCorretto-21'
            maven 'Maven-3.9.6'
        }

        environment {
            NEXUS_VERSION = "nexus3"
            NEXUS_PROTOCOL = "http"
            NEXUS_URL = "nexus:8081"
            NEXUS_REPOSITORY = "cicd-demo-mixed"
            NEXUS_CREDENTIAL_ID = "NexusRepo"
            
        }
        
        stages {
            stage('Maven Build') {
                steps {
                    mavenbuild()
                }
            }

            stage("Publish to Nexus Repository Manager") {
            steps {
                script {
                    pom = readMavenPom file: "pom.xml";
                    filesByGlob = findFiles(glob: "target/*.${pom.packaging}");
                    echo "${filesByGlob[0].name} ${filesByGlob[0].path} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}"
                    artifactPath = filesByGlob[0].path;
                    artifactExists = fileExists artifactPath;
                    if(artifactExists) {
                        echo "*** File: ${artifactPath}, group: ${pom.groupId}, packaging: ${pom.packaging}, version ${pom.version}";
                        nexusArtifactUploader(
                            nexusVersion: NEXUS_VERSION,
                            protocol: NEXUS_PROTOCOL,
                            nexusUrl: NEXUS_URL,
                            groupId: pom.groupId,
                            version: pom.version,
                            repository: NEXUS_REPOSITORY,
                            credentialsId: NEXUS_CREDENTIAL_ID,
                            artifacts: [
                                [artifactId: pom.artifactId,
                                classifier: '',
                                file: artifactPath,
                                type: pom.packaging],
                                [artifactId: pom.artifactId,
                                classifier: '',
                                file: "pom.xml",
                                type: "pom"]
                            ]
                        );
                    } else {
                        error "*** File: ${artifactPath}, could not be found";
                        }
                    }
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
        sh "cat ${env.WORKSPACE}/.m2/settings.xml"
        sh "mvn -s ${env.WORKSPACE}/.m2/settings.xml -Dmaven.repo.local=${env.WORKSPACE}/.m2/repository clean install -U"
        sh "ls  -lrtha ./target"
    }
}

