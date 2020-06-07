package ch.schulealtendorf.sporttagpsa.controller

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

fun MockHttpServletRequestBuilder.formContent(value: Any): MockHttpServletRequestBuilder {
    value::class.memberProperties
        .filter { it.visibility == KVisibility.PUBLIC }
        .forEach {
            this.param(it.name, it.getter.call(value).toString())
        }

    return this
}
