def runShellCommand(command) {
    return sh(script: command, returnStatus: true)
}