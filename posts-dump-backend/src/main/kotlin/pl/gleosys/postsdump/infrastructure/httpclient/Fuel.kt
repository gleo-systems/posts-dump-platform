package pl.gleosys.postsdump.infrastructure.httpclient

import arrow.core.Either
import arrow.core.Either.Right
import arrow.core.left
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.response
import com.github.kittinunf.fuel.moshi.moshiDeserializerOf
import com.github.kittinunf.result.Result
import com.squareup.moshi.JsonAdapter
import pl.gleosys.postsdump.core.Failure
import pl.gleosys.postsdump.core.Failure.InfrastructureError

fun <T> Result<T, FuelError>.toEither(): Either<Failure, T> =
    this.fold(::Right) { InfrastructureError(cause = it).left() }

inline fun <reified T : Any> Request.responseObject(adapter: JsonAdapter<T>) =
    response(moshiDeserializerOf(adapter))
