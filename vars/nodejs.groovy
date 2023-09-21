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
        stages {
            stage('Lint Checks') {
                 steps {
                    script {
                        lintChecks()
                    }

                    
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