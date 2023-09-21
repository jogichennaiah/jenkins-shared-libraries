def call() {


    pipeline {
        agent any 
        stages {
            stage('Lint Checks') {
                 steps {

                    sh "echo Installing JSlist"
                    sh "npm i jslint"
                    sh "echo Starting linkChecks...."
                    sh "node_modules/jslint/bin/jslint.js server.js || true"
                    sh "echo linkCheck completed"
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