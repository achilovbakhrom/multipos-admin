package com.basicsteps.multipos.utils

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.VertxOptions

import java.io.File
import java.io.IOException
import java.util.function.Consumer

/**
 * Created at 20.01.2018
 *
 * @author Achilov Bakhrom
 *
 * Singleton Helper for simple/clustered deploying verticles
 * written in various languages (java, kotlin, ruby, javascript ...)
 */
object Runner {

    private val WEB_EXAMPLES_DIR = "multipos-admin"
    private val WEB_EXAMPLES_JAVA_DIR = WEB_EXAMPLES_DIR + "/src/main/java/"
    private val WEB_EXAMPLES_JS_DIR = WEB_EXAMPLES_DIR + "/src/main/js/"
    private val WEB_EXAMPLES_GROOVY_DIR = WEB_EXAMPLES_DIR + "/src/main/groovy/"
    private val WEB_EXAMPLES_RUBY_DIR = WEB_EXAMPLES_DIR + "/src/main/ruby/"
    private val WEB_EXAMPLES_KOTLIN_DIR = WEB_EXAMPLES_DIR + "/src/main/kotlin/"


    fun runClusteredJavaVerticle(clazz: Class<*>) {
        runVerticle(WEB_EXAMPLES_JAVA_DIR, clazz, VertxOptions().setClustered(true), null)
    }

    fun runJavaVerticle(clazz: Class<*>) {
        runVerticle(WEB_EXAMPLES_JAVA_DIR, clazz, VertxOptions().setClustered(false), null)
    }

    fun runJavaVerticle(clazz: Class<*>, options: DeploymentOptions) {
        runVerticle(WEB_EXAMPLES_JAVA_DIR, clazz, VertxOptions().setClustered(false), options)
    }

    // JavaScript examples

    fun runJSVerticle(scriptName: String) {
        runScriptVerticle(WEB_EXAMPLES_JS_DIR, scriptName, VertxOptions().setClustered(false))
    }

    fun runJSClusteredVerticle(scriptName: String) {
        runScriptVerticle(WEB_EXAMPLES_JS_DIR, scriptName, VertxOptions().setClustered(true))
    }

//    internal object JSAuthRunner {
//        @JvmStatic
//        fun main(args: Array<String>) {
//            Runner.runJSExample("io/vertx/example/web/auth/server.js")
//        }
//    }
//
//    internal object JSAuthJDBC {
//        @JvmStatic
//        fun main(args: Array<String>) {
//            Runner.runJSExample("io/vertx/example/web/authjdbc/server.js")
//        }
//    }
//
//    internal object JSHelloWorldRunner {
//        @JvmStatic
//        fun main(args: Array<String>) {
//            Runner.runJSExample("io/vertx/example/web/helloworld/server.js")
//        }
//    }
//
//    internal object JSRealtimeRunner {
//        @JvmStatic
//        fun main(args: Array<String>) {
//            Runner.runJSExample("io/vertx/example/web/realtime/server.js")
//        }
//    }
//
//    internal object JSChatRunner {
//        @JvmStatic
//        fun main(args: Array<String>) {
//            Runner.runJSExample("io/vertx/example/web/chat/server.js")
//        }
//    }
//
//    internal object JSSessionsRunner {
//        @JvmStatic
//        fun main(args: Array<String>) {
//            Runner.runJSExample("io/vertx/example/web/sessions/server.js")
//        }
//    }
//
//    internal object JSTemplatingRunner {
//        @JvmStatic
//        fun main(args: Array<String>) {
//            Runner.runJSExample("io/vertx/example/web/templating/mvel/server.js")
//        }
//    }

    // Groovy examples

    fun runGroovyVerticle(scriptName: String) {
        runScriptVerticle(WEB_EXAMPLES_GROOVY_DIR, scriptName, VertxOptions().setClustered(false))
    }

    fun runGroovyClusteredVerticle(scriptName: String) {
        runScriptVerticle(WEB_EXAMPLES_GROOVY_DIR, scriptName, VertxOptions().setClustered(true))
    }
//
//    internal object GroovyAuthRunner {
//        @JvmStatic
//        fun main(args: Array<String>) {
//            Runner.runGroovyExample("io/vertx/example/web/auth/server.groovy")
//        }
//    }
//
//    internal object GroovyAuthJDBC {
//        @JvmStatic
//        fun main(args: Array<String>) {
//            Runner.runGroovyExample("io/vertx/example/web/authjdbc/server.groovy")
//        }
//    }
//
//    internal object GroovyHelloWorldRunner {
//        @JvmStatic
//        fun main(args: Array<String>) {
//            Runner.runGroovyExample("io/vertx/example/web/helloworld/server.groovy")
//        }
//    }
//
//    internal object GroovyChatRunner {
//        @JvmStatic
//        fun main(args: Array<String>) {
//            Runner.runGroovyExample("io/vertx/example/web/chat/server.groovy")
//        }
//    }
//
//    internal object GroovyRealtimeRunner {
//        @JvmStatic
//        fun main(args: Array<String>) {
//            Runner.runGroovyExample("io/vertx/example/web/realtime/server.groovy")
//        }
//    }
//
//    internal object GroovySessionsRunner {
//        @JvmStatic
//        fun main(args: Array<String>) {
//            Runner.runGroovyExample("io/vertx/example/web/sessions/server.groovy")
//        }
//    }
//
//    internal object GroovyTemplatingRunner {
//        @JvmStatic
//        fun main(args: Array<String>) {
//            Runner.runGroovyExample("io/vertx/example/web/templating/mvel/server.groovy")
//        }
//    }
//
//    internal object GroovyRestRunner {
//        @JvmStatic
//        fun main(args: Array<String>) {
//            Runner.runGroovyExample("io/vertx/example/web/rest/simple_rest.groovy")
//        }
//    }

    // Ruby examples

    fun runRubyVerticle(scriptName: String) {
        runScriptVerticle(WEB_EXAMPLES_RUBY_DIR, scriptName, VertxOptions().setClustered(false))
    }

    fun runRubyClusteredVerticle(scriptName: String) {
        runScriptVerticle(WEB_EXAMPLES_RUBY_DIR, scriptName, VertxOptions().setClustered(true))
    }

//    internal object RubyAuthRunner {
//        @JvmStatic
//        fun main(args: Array<String>) {
//            Runner.runRubyExample("io/vertx/example/web/auth/server.rb")
//        }
//    }
//
//    internal object RubyAuthJDBC {
//        @JvmStatic
//        fun main(args: Array<String>) {
//            Runner.runRubyExample("io/vertx/example/web/authjdbc/server.rb")
//        }
//    }
//
//    internal object RubyHelloWorldRunner {
//        @JvmStatic
//        fun main(args: Array<String>) {
//            Runner.runRubyExample("io/vertx/example/web/helloworld/server.rb")
//        }
//    }
//
//    internal object RubyChatRunner {
//        @JvmStatic
//        fun main(args: Array<String>) {
//            Runner.runRubyExample("io/vertx/example/web/chat/server.rb")
//        }
//    }
//
//    internal object RubyRealtimeRunner {
//        @JvmStatic
//        fun main(args: Array<String>) {
//            Runner.runRubyExample("io/vertx/example/web/realtime/server.rb")
//        }
//    }
//
//    internal object RubySessionsRunner {
//        @JvmStatic
//        fun main(args: Array<String>) {
//            Runner.runRubyExample("io/vertx/example/web/sessions/server.rb")
//        }
//    }
//
//    internal object RubyTemplatingRunner {
//        @JvmStatic
//        fun main(args: Array<String>) {
//            Runner.runRubyExample("io/vertx/example/web/templating/mvel/server.rb")
//        }
//    }

    fun runVerticle(exampleDir: String, clazz: Class<*>, options: VertxOptions, deploymentOptions: DeploymentOptions?) {
        runVerticle(exampleDir + clazz.`package`.name.replace(".", "/"), clazz.name, options, deploymentOptions)
    }


    fun runScriptVerticle(prefix: String, scriptName: String, options: VertxOptions) {
        val file = File(scriptName)
        val dirPart = file.parent
        val scriptDir = prefix + dirPart
        runVerticle(scriptDir, scriptDir + "/" + file.name, options, null)
    }

    fun runVerticle(exampleDir: String, verticleID: String, options: VertxOptions?, deploymentOptions: DeploymentOptions?) {
        var exampleDir = exampleDir
        var options = options
        if (options == null) {
            // Default parameter
            options = VertxOptions()
        }
        // Smart cwd detection

        // Based on the current directory (.) and the desired directory (exampleDir), we try to compute the vertx.cwd
        // directory:
        try {
            // We need to use the canonical file. Without the file name is .
            val current = File(".").canonicalFile
            if (exampleDir.startsWith(current.name) && exampleDir != current.name) {
                exampleDir = exampleDir.substring(current.name.length + 1)
            }
        } catch (e: IOException) {
            // Ignore it.
        }

        System.setProperty("vertx.cwd", exampleDir)
        val runner: Consumer<Vertx> = Consumer { vertx ->
            try {
                if (deploymentOptions != null) {
                    vertx.deployVerticle(verticleID, deploymentOptions)
                } else {
                    vertx.deployVerticle(verticleID)
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
        if (options.isClustered) {
            Vertx.clusteredVertx(options) { res ->
                if (res.succeeded()) {
                    val vertx = res.result()
                    runner.accept(vertx)
                } else {
                    res.cause().printStackTrace()
                }
            }
        } else {
            val vertx = Vertx.vertx(options)
            runner.accept(vertx)
        }
    }
}
