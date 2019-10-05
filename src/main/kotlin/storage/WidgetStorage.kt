package com.sft_test.storage

import com.sft_test.CreatingWidgetView

interface WidgetStorage {
	//ToDo: тут предполагается create&save нужно разделить
	fun save(widget: CreatingWidgetView): Widget

	fun getAll(): Collection<Widget>

	fun getBy(id: Long): Widget?

}
