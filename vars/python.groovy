def call() {
    node {
        git branch: 'main', url: "https://github.com/b55-clouddevops/${COMPONENT}.git"
        common.lintChecks()
        env.ARGS="-Dsonar.sources=."
        common.sonarChecks()
        common.testCases()
        common.artifacts()
    }
}

def call() {
    node {
        common.lintChecks()

    }
}


/* Declarative Pipeline

def call() {
    pipeline {
        agentany
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
                    script {
                            env.ARGS="-Dsonar.sources=."
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
*/    