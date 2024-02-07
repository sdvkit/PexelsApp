package com.sdv.kit.pexelsapp.data.remote

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes

internal class AnnotationExclusionStrategy : ExclusionStrategy {

    override fun shouldSkipField(attrs: FieldAttributes): Boolean {
        return attrs.getAnnotation(Exclude::class.java) != null
    }

    override fun shouldSkipClass(clazz: Class<*>?): Boolean {
        return false
    }
}

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class Exclude