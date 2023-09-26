def lintChecks() {
     sh "echo Installing JSlist"
     sh "npm i jslint"
     sh "echo Starting linkChecks ${COMPONENT}"
     sh "node_modules/jslint/bin/jslint.js server.js || true"
     sh "echo linkCheck completed for ${COMPONENT}"
}

def call() {


    pipeline {
        agent any
        environment {
            SONAR_URL = "172.31.61.127"
            NEXUS_URL = "172.31.17.146"
            SONAR_CRED = credentials('SONAR_CRED')
            NEXUS_CRED = credentials('NEXUS_CRED')
        } 
        stages {
            stage('Lint Checks') {
                 steps {
                    script {
                        lintChecks()
                    } 
                }
            }
            stage('Test Cases') {
                parallel {
                    stage('Unit Testing') {
                        steps {
                            sh "echo Starting Unit Testing"
                            sh "echo Unit Testing Completed"
                        }
                    }
                    stage('Integration Testing') {
                        steps {
                            sh "echo Starting Integration Testing"
                            sh "echo Integration Testing Completed"
                        }
                    }
                    stage('Functional Testing') {
                        steps {
                            sh "echo Starting Functional Testing"
                            sh "echo Functional Testing Completed"
                        }
                    }
                }
            }
            stage('Checks The Release') {
                when {
                    expression { env.TAG_NAME != null }
                }
                steps {
                    script {
                    env.UPLOAD_STATUS=sh(returnStdout: true, script: "curl -L -s http://${NEXUS_URL}:8081/service/rest/repository/browse/${COMPONENT} | grep ${COMPONENT}-${TAG_NAME}.zip || true")
                    print UPLOAD_STATUS
                    }
                }
            }
 //           stage('Sonar Checks') {
//                steps {
//                    sh "echo Starting Sonar Checks For ${COMPONENT}"
 //                   sh "sonar-scanner -Dsonar.host.url=http://${SONAR_URL}:9000/ -Dsonar.sources=. -Dsonar.projectKey=${COMPONENT} -Dsonar.login=${SONAR_CRED_USR} -Dsonar.password=${SONAR_CRED_PSW}" 
//                    sh "curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate > qyality-gate.sh"
 //                   sh "bash quality-gate.sh ${SONAR_CRED_USR} ${SONAR_CRED_PSW} ${SONAR_URL} ${COMPONENT}"
 //                   sh "echo  ${COMPONENT} Sonar Checks are completed" 
 //               }
 //           }
            stage('Generating Artifacts') { 
                when {
                    expression { env.TAG_NAME != null }
                }
                steps {
                    sh "echo Generating artifacts...."
                    sh "npm install"
                    sh "zip ${COMPONENT}-${TAG_NAME}.zip node_modules server.js"
                    sh "ls -ltr"
                
                 }
            }
            stage('Uploading Artifacts') {
                when {
                    expression { env.TAG_NAME != null }
             }
                steps {
                    sh '''
                         echo Uploading ${COMPONENT} Artifacts to nexus
                         curl -v -u ${NEXUS_USR}:${NEXUS_PSW} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://${NEXUS_URL}:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip
                         echo Uploading ${COMPONENT} artifact to nexus is completed
                       '''  

                 }
            }
        } 
     }
} 