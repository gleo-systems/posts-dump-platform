package pl.gleosys.postsdump.infrastructure.httpclient

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.response
import com.github.kittinunf.fuel.moshi.moshiDeserializerOf
import com.github.kittinunf.result.Result
import com.squareup.moshi.JsonAdapter
import pl.gleosys.postsdump.domain.Failure
import pl.gleosys.postsdump.infrastructure.InfrastructureError

fun <T> Result<T, FuelError>.toEither(): Either<Failure, T> =
    this.fold(::Right) { Left(InfrastructureError(cause = it)) }

inline fun <reified T : Any> Request.responseObject(adapter: JsonAdapter<T>) = response(moshiDeserializerOf(adapter))
