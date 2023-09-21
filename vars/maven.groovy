def lintChecks() {
     sh "echo Starting linkChecks ${COMPONENT}"
     sh "mvn checkstyle:check || true"
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
            stage('Code Compile') {
                steps {
                    sh "echo Generating artifacts for $COMPONENT"
                    sh "mvn clean compile"
                
                 }
            }

        } 
     }
} 