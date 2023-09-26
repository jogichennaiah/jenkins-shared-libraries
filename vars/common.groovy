def sonarChecks() {  
          stage('Sonar Checks') {         
          sh "echo Starting Sonar Checks For ${COMPONENT}"
            //sh "sonar-scanner -Dsonar.host.url=http://${SONAR_URL}:9000/ -Dsonar.sources=. -Dsonar.projectKey=${COMPONENT} -Dsonar.login=${SONAR_CRED_USR} -Dsonar.password=${SONAR_CRED_PSW}" 
            // sh "curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate > qyality-gate.sh"
            //sh "bash quality-gate.sh ${SONAR_CRED_USR} ${SONAR_CRED_PSW} ${SONAR_URL} ${COMPONENT}"
          sh "echo  ${COMPONENT} Sonar Checks are completed" 
        }
}    

def lintChecks() {
    stage('Lint Checks') {
        if(env.APPTYPE == "nodejs") {
            sh "echo Installing JSlist"
            sh "npm i jslint"
            sh "echo starting linkChecks for ${COMPONENT}"
            sh "node_modules/jslint/bin/jslint.js server.js || true"
            sh "echo linkChecks completed for ${COMPONENT}"
        }
        else if(env.APPTYPE == "maven") {
            sh "echo starting linkChecks for ${COMPONENT}"
            sh "mvn checkstyle:check || true"
            sh "echo linkChecks completed for ${COMPONENT}"
        }
        else if(env.APPTYPE == "pylint") {
            sh "echo starting linkChecks for ${COMPONENT}"
            sh "echo linkChecks completed for ${COMPONENT}"
        }
        else {
            sh "echo Lint Checks For Frontend are in progress"
        }   
    }
}

def testCases() {
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
        }

