package pl.gleosys.postsdump.infrastructure

import arrow.core.Either
import arrow.core.Either.Companion.catch
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import pl.gleosys.postsdump.core.Failure
import pl.gleosys.postsdump.core.Failure.ConversionError
import pl.gleosys.postsdump.core.Failure.FailureFactory.newInstance

class JSONParser(private val delegate: Moshi) {

    fun <T> toByteArray(content: T, clazz: Class<T>): Either<Failure, ByteArray> {
        return catch { delegate.adapter(clazz).toJson(content).toByteArray() }
            .mapLeft<ConversionError>(::newInstance)
    }

    fun <T> fromJSON(content: String, clazz: Class<T>): Either<Failure, T> {
        return catch {
            require(content.isNotEmpty())
            delegate.adapter(clazz).fromJson(content)
        }
            .mapLeft<ConversionError>(::newInstance)
            .flatMap {
                it?.right()
                    ?: ConversionError(message = "Error while parsing content '$content' to ${clazz.simpleName} type")
                        .left()
            }
    }

    fun <T> newListAdapter(clazz: Class<T>): JsonAdapter<List<T>> =
        delegate.adapter(Types.newParameterizedType(List::class.java, clazz))
}
