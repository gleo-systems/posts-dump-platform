package pl.gleosys.postsdump.application

import com.google.inject.AbstractModule
import com.google.inject.Scopes
import com.google.inject.matcher.Matchers
import pl.gleosys.postsdump.application.aop.LogDuration
import pl.gleosys.postsdump.application.aop.LogDurationInterceptor
import pl.gleosys.postsdump.application.process.RunDumpProcess

class ApplicationModule : AbstractModule() {
    override fun configure() {
        bindInterceptor(
            Matchers.any(),
            Matchers.annotatedWith(LogDuration::class.java),
            LogDurationInterceptor(),
        )
        bind(RunDumpProcess::class.java).`in`(Scopes.SINGLETON)
    }
}
