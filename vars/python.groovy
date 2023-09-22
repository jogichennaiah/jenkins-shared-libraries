def lintchecks() {
    sh "echo starting linkChecks for ${COMPONENT}"
    sh "pylint *.py || true"
    sh "echo linkChecks completed for ${COMPONENT}"
}

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
        }
    }
}