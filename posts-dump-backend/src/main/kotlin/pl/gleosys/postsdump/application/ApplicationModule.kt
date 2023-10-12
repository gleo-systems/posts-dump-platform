package pl.gleosys.postsdump.application

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Scopes
import com.google.inject.matcher.Matchers
import jakarta.inject.Singleton
import pl.gleosys.postsdump.application.process.RunDumpProcess
import pl.gleosys.postsdump.application.process.RunDumpProcessCommand
import pl.gleosys.postsdump.core.aop.LogDuration
import pl.gleosys.postsdump.core.aop.LogDurationInterceptor

class ApplicationModule : AbstractModule() {
    override fun configure() {
        bindInterceptor(
            Matchers.any(),
            Matchers.annotatedWith(LogDuration::class.java),
            LogDurationInterceptor()
        )
        bind(RunDumpProcess::class.java).`in`(Scopes.SINGLETON)
    }

    @Provides
    @Singleton
    fun runDumpProcessCommand(process: RunDumpProcess) = RunDumpProcessCommand(process)
}
