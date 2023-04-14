import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.NodeJSBuildStep
import jetbrains.buildServer.configs.kotlin.buildSteps.nodeJS
import jetbrains.buildServer.configs.kotlin.projectFeatures.githubConnection
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2022.10"

project {

    vcsRoot(HttpsGithubComTomasz64playwrightPlaygroundRefsHeadsMaster)

    buildType(Build)

    features {
        githubConnection {
            id = "PROJECT_EXT_4"
            displayName = "GitHub.com"
            clientId = "28d4a3b5cf0c10c0748e"
            clientSecret = "credentialsJSON:7a6ac660-1a37-4936-9124-5250873b808b"
        }
    }
}

object Build : BuildType({
    name = "Build"

    artifactRules = "playwright-report => playwright-report"

    vcs {
        root(HttpsGithubComTomasz64playwrightPlaygroundRefsHeadsMaster)
    }

    steps {
        nodeJS {
            name = "Install Dependencies"
            shellScript = "npm install"
            dockerImage = "mcr.microsoft.com/playwright:v1.30.0-focal"
            dockerImagePlatform = NodeJSBuildStep.ImagePlatform.Any
        }
        nodeJS {
            name = "Install Browsers and run tests"
            shellScript = """
                npx playwright install
                npx playwright test
            """.trimIndent()
            dockerImage = "mcr.microsoft.com/playwright:v1.30.0-focal"
            dockerImagePlatform = NodeJSBuildStep.ImagePlatform.Any
        }
    }

    triggers {
        vcs {
        }
    }

    features {
        perfmon {
        }
    }
})

object HttpsGithubComTomasz64playwrightPlaygroundRefsHeadsMaster : GitVcsRoot({
    name = "https://github.com/Tomasz64/playwright-playground#refs/heads/master"
    url = "https://github.com/Tomasz64/playwright-playground"
    branch = "master"
    branchSpec = "refs/heads/*"
})
