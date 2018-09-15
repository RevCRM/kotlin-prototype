package org.revcrm.data

import com.fasterxml.jackson.annotation.ObjectIdResolver
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator
import com.fasterxml.jackson.databind.cfg.MapperConfig
import com.fasterxml.jackson.databind.introspect.Annotated
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder
import javax.inject.Inject

class YMLHandlerInstantiator @Inject constructor(_resolver: YMLEntityResolver) : HandlerInstantiator() {
    val resolver: YMLEntityResolver = _resolver

    override fun resolverIdGeneratorInstance(config: MapperConfig<*>, annotated: Annotated, implClass: Class<*>): ObjectIdResolver? {
        return resolver
    }

    // Other abstract functions that had to be overridden
    override fun typeIdResolverInstance(config: MapperConfig<*>?, annotated: Annotated?, resolverClass: Class<*>?): TypeIdResolver? {
        return null
    }
    override fun serializerInstance(config: SerializationConfig?, annotated: Annotated?, serClass: Class<*>?): JsonSerializer<*>? {
        return null
    }
    override fun typeResolverBuilderInstance(config: MapperConfig<*>?, annotated: Annotated?, builderClass: Class<*>?): TypeResolverBuilder<*>? {
        return null
    }
    override fun deserializerInstance(config: DeserializationConfig?, annotated: Annotated?, deserClass: Class<*>?): JsonDeserializer<*>? {
        return null
    }
    override fun keyDeserializerInstance(config: DeserializationConfig?, annotated: Annotated?, keyDeserClass: Class<*>?): KeyDeserializer? {
        return null
    }
}