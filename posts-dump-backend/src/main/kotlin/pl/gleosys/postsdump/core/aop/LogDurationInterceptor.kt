package pl.gleosys.postsdump.core.aop

import io.github.oshai.kotlinlogging.KotlinLogging
import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import kotlin.time.measureTime

class LogDurationInterceptor : MethodInterceptor {
    override fun invoke(invocation: MethodInvocation): Any {
        var result: Any
        val duration = measureTime { result = invocation.proceed() }
        KotlinLogging.logger(invocation.method.declaringClass.canonicalName)
            .info { "#${invocation.method.name} invocation duration=${duration.inWholeMilliseconds}ms" }
        return result
    }
}
