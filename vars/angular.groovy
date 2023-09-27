def call() {
    node {
        git branch: 'main', url: "https://github.com/jogichennaiah/${COMPONENT}.git"
        common.lintChecks()
        env.ARGS="-Dsonar.java.binaries=target/"
        common.sonarChecks()
        common.testCases()
        common.artifacts()
    }
}