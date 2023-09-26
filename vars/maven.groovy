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
            stage('Sonar Checks') {
                steps {
                    script {
                            env.ARGS="-Dsonar.java.binaries=target/"
                            common.sonarChecks()
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
            stage('Check The Release') {
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
            stage('Generating Artifacts') {
                steps {
                    sh "echo Artifact Generation Complete"
                    }
                }
            }
        }
    }

        
     
