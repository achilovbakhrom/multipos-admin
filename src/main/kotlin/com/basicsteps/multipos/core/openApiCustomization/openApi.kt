package com.basicsteps.multipos.core.openApiCustomization

import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.v3.parser.ObjectMapperFactory
import io.swagger.v3.parser.OpenAPIResolver
import io.swagger.v3.parser.OpenAPIV3Parser
import io.swagger.v3.parser.core.models.AuthorizationValue
import io.swagger.v3.parser.core.models.ParseOptions
import io.swagger.v3.parser.core.models.SwaggerParseResult
import io.swagger.v3.parser.util.ClasspathHelper
import io.swagger.v3.parser.util.InlineModelResolver
import io.swagger.v3.parser.util.RemoteUrl
import io.swagger.v3.parser.util.ResolverFully
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.ext.web.api.contract.RouterFactoryException
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory
import io.vertx.ext.web.api.contract.openapi3.impl.OpenAPI3RouterFactoryImpl
import io.vertx.ext.web.api.contract.openapi3.impl.OpenApi3Utils
import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import javax.net.ssl.SSLHandshakeException

/**
 * Created by Ikrom Mirzayev on 22-May-18.
 */


fun OpenAPIV3Parser.readContent(content: String, auth: List<AuthorizationValue>, options: ParseOptions): SwaggerParseResult {
    var tempAuth = auth
    var result = SwaggerParseResult()
    try {

        result = readInfo(content = content, auth = tempAuth)

        if (result.openAPI != null) {
            val version = result.openAPI.openapi
            if (auth == null) {
                tempAuth = ArrayList()
            }
            if (version != null && version.startsWith("3.0")) {
                if (options != null) {
                    val resolver = OpenAPIResolver(result.openAPI, tempAuth, null)
                    if (options.isResolve) {
                        result.openAPI = resolver.resolve()
                    }
                    if (options.isResolveFully) {
                        result.openAPI = resolver.resolve()
                        ResolverFully(options.isResolveCombinators).resolveFully(result.openAPI)
                    } else if (options.isFlatten) {
                        val inlineResolver = InlineModelResolver()
                        inlineResolver.flatten(result.openAPI)
                    }
                }
            }
        }

    } catch (e: Exception) {
        result.messages = Arrays.asList<String>(e.message)
    }

    return result
}

fun OpenAPIV3Parser.readInfo(content: String, auth: List<AuthorizationValue>): SwaggerParseResult {
    try {
        val mapper: ObjectMapper
        if (content.trim({ it <= ' ' }).startsWith("{")) {
            mapper = ObjectMapperFactory.createJson()
        } else {
            mapper = ObjectMapperFactory.createYaml()
        }
        val rootNode = mapper.readTree(content)

        return readWithInfo(rootNode)
    } catch (e: SSLHandshakeException) {
        val output = SwaggerParseResult()
        output.messages = Arrays.asList("unable to read location `" + content + "` due to a SSL configuration error.  " +
                "It is possible that the server SSL certificate is invalid, self-signed, or has an untrusted " +
                "Certificate Authority.")
        return output
    } catch (e: Exception) {
        val output = SwaggerParseResult()
        output.messages = Arrays.asList("unable to read location `$content`")
        return output
    }
}

interface CustomOpenApiRouting: OpenAPI3RouterFactory {
    companion object {
        fun create(vertx: Vertx, content: String, handler: Handler<AsyncResult<OpenAPI3RouterFactory>>) {





        }
    }
}

//            vertx.executeBlocking({ future: Handler<Future<OpenAPI3RouterFactory>> ->


//                val swaggerParseResult = OpenAPIV3Parser().readContent(content, ArrayList(), OpenApi3Utils.getParseOptions())
//                if (swaggerParseResult.messages.isEmpty()) {
//                    future.complete(OpenAPI3RouterFactoryImpl(vertx, swaggerParseResult.openAPI))
//                } else {
//                    if (swaggerParseResult.messages.size == 1 && swaggerParseResult.messages[0].matches("unable to read location `?\\Q$content\\E`?".toRegex()))
//                        future.fail(RouterFactoryException.createSpecNotExistsException(url))
//                    else
//                        future.fail(RouterFactoryException.createSpecInvalidException(StringUtils.join(swaggerParseResult.messages, ", ")))
//                }
//            }, handler)