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
	fun create(@RequestBody widget: CreatingWidgetView): ResponseEntity<Widget> {
		return ResponseEntity(storage.save(widget), HttpStatus.OK)
	}

	@GetMapping(path = ["/widgets"])
	fun all(): ResponseEntity<Collection<Widget>> {
		return ResponseEntity(storage.getAll(), HttpStatus.OK)
	}

	@GetMapping(path = ["/widgets/{id}"])
	fun one(@PathVariable id: Long): ResponseEntity<Widget> {
		//ToDo: логика на эксепшенах это потеря производительности, но тут надо много чего сделать для правильного пайплайна
		val widget = storage.getBy(id) ?: throw WidgetNotFoundException(id)
		return ResponseEntity(widget, HttpStatus.OK)
	}
}

class WidgetNotFoundException(id: Long) : RuntimeException("Widget with id $id not found")
