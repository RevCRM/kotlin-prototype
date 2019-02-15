import com.moowork.gradle.node.npm.NpmTask

description = "RevCRM Client"

// Orig
// task buildClient(type: NpmTask, dependsOn: npmInstall) {
//    args = ["run", "build"]
//    inputs.dir file("src")
//    outputs.dirs file("${buildDir}/dist")
// }

task("buildClient", NpmTask::class) {
    dependsOn("npmInstall")
    setArgs(listOf("run", "build"))
    inputs.dir(File("src"))
    outputs.dir(File("$buildDir/dist"))
}
