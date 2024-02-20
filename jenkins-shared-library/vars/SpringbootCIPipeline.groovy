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
            NEXUS_URL = "nginx/app/nexus"
            NEXUS_RELEASE_REPOSITORY = "maven-releases"
            NEXUS_SNAPSHOT_REPOSITORY = "maven-snapshots"
            NEXUS_CREDENTIAL_ID = "NexusRepo"
            
        }
        
        stages {
            stage('Maven Build') {
                steps {
                    mavenbuild()
                }
            }

            stage('sonar quality check') {
                steps {
                    sonarQualityCheck()                 
                 
                }
            }
            
            stage("Publish to Nexus Repository Manager") {
            steps {
                publishToNexus()        
                }
            }
        }


        post {
            always {
                emailext (
                    attachLog: true,
                    subject: "Pipeline ${env.JOB_NAME} Build ${currentBuild.number}: ${currentBuild.result}",
                    body: "Git repository: ${env.GIT_URL}\nGit branch: ${env.GIT_BRANCH}\nCheck the build at: ${env.BUILD_URL}\nBuild Status: ${currentBuild.result}",
                    to: 'vaishu.dontaraju@gmail.com'
                )
            }
            
        }

    }
}

def mavenbuild(){
    script {
        configFileProvider([configFile(fileId: '6cfed8c1-011d-4578-b5fd-7ad91acd26e7', targetLocation: "${env.WORKSPACE}/.m2/settings.xml")]) {
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

def sonarQualityCheck(){
    script {
        withSonarQubeEnv(credentialsId: 'Sonar-token') {
            sh 'mvn sonar:sonar'
            }
        timeout(time: 5, unit: 'MINUTES') {
            waitForQualityGate abortPipeline: true
        }
    }
}

def publishToNexus(){
    script {
        pom = readMavenPom file: "pom.xml";
        filesByGlob = findFiles(glob: "target/*.${pom.packaging}");
        echo "${filesByGlob[0].name} ${filesByGlob[0].path} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}"
        artifactPath = filesByGlob[0].path;
        artifactExists = fileExists artifactPath;
        if(artifactExists) {
            echo "*** \n File: ${artifactPath},\n group: ${pom.groupId},\n packaging: ${pom.packaging},\n version ${pom.version}";
            
            artifactVersion="${pom.version}"
            if(artifactVersion.toUpperCase().contains("SNAPSHOT")) {
                echo "Uploading artifact to Snapshot repository"
                NEXUS_REPOSITORY = NEXUS_SNAPSHOT_REPOSITORY
            } else {
                echo "Uploading artifact to Release repository"
                NEXUS_REPOSITORY = NEXUS_RELEASE_REPOSITORY
            }

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