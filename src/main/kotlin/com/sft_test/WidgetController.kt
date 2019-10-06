package com.sft_test

import com.sft_test.storage.WidgetStorage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class WidgetController @Autowired constructor(
	val storage: WidgetStorage
) {

	@PostMapping(path = ["/widgets"])
	fun create(@RequestBody widget: WidgetView): ResponseEntity<Widget> {
		//ToDo: лучше сделать приём 1ой строкой и инстанцирования в зависимости от хранилища
		return ResponseEntity(storage.create(widget), HttpStatus.OK)
	}

	@GetMapping(path = ["/widgets"])
	fun all(): ResponseEntity<Collection<Widget>> {
		return ResponseEntity(storage.getAll(), HttpStatus.OK)
	}

	@GetMapping(path = ["/widgets/{id}"])
	fun one(@PathVariable id: Long): ResponseEntity<Any> {
		val widget = storage.getBy(id)
		return if (widget == null) {
			notFoundWidgetResponse(id)
		} else {
			ResponseEntity(widget, HttpStatus.OK)
		}
	}

	private fun notFoundWidgetResponse(id: Long): ResponseEntity<Any> =
			ResponseEntity(ApiResponse(HttpStatus.NOT_FOUND, "Widget with id=$id not found"), HttpStatus.NOT_FOUND)

	@DeleteMapping(path = ["/widgets/{id}"])
	fun remove(@PathVariable id: Long): ResponseEntity<Any> {
		return if (storage.remove(id)) {
			ResponseEntity(ApiResponse(HttpStatus.OK, "widget id=$id removed"), HttpStatus.OK)
		} else {
			notFoundWidgetResponse(id)
		}
	}

    @PatchMapping(path = ["/widgets/{id}"])
    fun update(@PathVariable id: Long, @RequestBody widget: WidgetView): ResponseEntity<Any> {
        val updatedWidget = storage.update(id, widget)
        return if (updatedWidget == null) {
            notFoundWidgetResponse(id)
        } else {
            ResponseEntity(updatedWidget, HttpStatus.OK)
        }
    }
}