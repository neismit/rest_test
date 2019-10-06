package com.sft_test.storage

import com.sft_test.WidgetView
import com.sft_test.Widget

interface WidgetStorage {
    // create & save
	fun create(widget: WidgetView): Widget

    fun update(id: Long, widget: WidgetView): Widget?

	fun getAll(): Collection<Widget>

	fun getBy(id: Long): Widget?

	// false - не был найден
	fun remove(id: Long): Boolean

}
