package com.sft_test

import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.isA
import com.nhaarman.mockitokotlin2.verify
import com.sft_test.storage.WidgetStorage
import org.junit.Test

import org.junit.Assert.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [Application::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WidgetControllerTest {

    @Autowired
    private lateinit var controller: WidgetController

    @MockBean
    private lateinit var storage: WidgetStorage


    @Before
    fun setUp() {

    }

    @Test
    fun create_ok() {
        val view = WidgetView(x = 2, y = 4, width = 23423.234)
        given(storage.create(eq(view))).willReturn(WidgetEntity(
                x = view.x!!,
                y = view.y!!,
                width = view.width!!
        ))
        val createResponse = controller.create(view)
        verify(storage).create(eq(view))

        assertThat(createResponse.statusCode).isEqualTo(HttpStatus.OK)
        val createdWidget = createResponse.body
        assertThat(createdWidget).isNotNull
        assertThat(createdWidget.x).isEqualTo(view.x!!)
        assertThat(createdWidget.y).isEqualTo(view.y!!)
        assertThat(createdWidget.width).isEqualTo(view.width!!)
    }

    @Test
    fun one_widgetFount() {
        val id = 4L
        given(storage.getBy(eq(id))).willReturn(WidgetEntity(id = 4L))

        val response = controller.one(id)
        verify(storage).getBy(eq(id))

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isNotNull
        val widget = response.body as WidgetEntity
        assertThat(widget.id).isEqualTo(id)
    }

    @Test
    fun one_widgetNotFound() {
        val id = 4L
        val response = controller.one(id)

        assertApiResponse(response, id, HttpStatus.NOT_FOUND)
    }

    private fun assertApiResponse(response: ResponseEntity<Any>, id: Long, status: HttpStatus) {
        assertThat(response.statusCode).isEqualTo(status)
        assertThat(response.body).isNotNull
        val apiResponse = response.body!! as ApiResponse
        assertThat(apiResponse.status).isEqualTo(status)
        assertThat(apiResponse.message).contains(id.toString())
    }

    @Test
    fun remove_ok() {
        val id = 4L
        given(storage.remove(eq(id))).willReturn(true)
        val response = controller.remove(id)

        assertApiResponse(response, id, HttpStatus.OK)
    }

    @Test
    fun remove_notFound() {
        val id = 4L
        given(storage.remove(eq(id))).willReturn(false)
        val response = controller.remove(id)

        assertApiResponse(response, id, HttpStatus.NOT_FOUND)
    }

    @Test
    fun update_ok() {
        val id = 4L
        val view = WidgetView(x = 2, y = 4, width = 23423.234, z = 4)
        given(storage.update(eq(id), eq(view))).willReturn(WidgetEntity(
                id = id,
                x = view.x!!,
                y = view.y!!,
                width = view.width!!,
                z = view.z!!
        ))
        val createResponse = controller.update(id, view)
        verify(storage).update(eq(id), eq(view))

        assertThat(createResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(createResponse.body).isNotNull
        val createdWidget = createResponse.body as WidgetEntity
        assertThat(createdWidget.id).isEqualTo(id)
        assertThat(createdWidget.x).isEqualTo(view.x!!)
        assertThat(createdWidget.y).isEqualTo(view.y!!)
        assertThat(createdWidget.width).isEqualTo(view.width!!)
        assertThat(createdWidget.z).isEqualTo(view.z!!)
    }

    @Test
    fun update_notFound() {
        val id = 4L
        val view = WidgetView(x = 2, y = 4, width = 23423.234, z = 4)
        given(storage.update(eq(id), eq(view))).willReturn(null)
        val response = controller.update(id, view)

        assertApiResponse(response, id, HttpStatus.NOT_FOUND)

    }
}