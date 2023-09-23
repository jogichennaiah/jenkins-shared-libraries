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
            SONAR_URL = "172.31.21.230"
            SONAR_CRED = credentials('SONAR_CRED')
        } 
        stages {
            stage('Lint Checks') {
                 steps {
                    script {
                        lintChecks()
                    } 
                }
            }
            stage('Sonar Checks') {
                steps {
                    sh "env"
                    sh "sonar-scanner -Dsonar.host.url=http://${SONAR_URL}:9000/ -Dsonar.sources=. -Dsonar.projectKey=${COMPONENT} -Dsonar.login=${SONAR_CRED_USR} -Dsonar.password=${SONAR_CRED_PWD}"  
                }
            }
            stage('Generating Artifacts') {
                steps {
                    sh "echo Generating artifacts...."
                    sh "npm install"
                
                 }
            }
        } 
     }
} 