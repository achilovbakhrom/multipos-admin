package com.basicsteps.multipos

import com.basicsteps.multipos.utils.Runner
import com.basicsteps.multipos.worker.MainVerticle


fun main(args: Array<String>) {
    Runner.runJavaVerticle(MainVerticle::class.java)
}


