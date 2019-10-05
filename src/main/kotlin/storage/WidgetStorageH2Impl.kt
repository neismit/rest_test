package com.sft_test.storage

import com.sft_test.CreatingWidgetView
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class WidgetStorageH2Impl @Autowired constructor(
	val dao: WidgetDAO
) : WidgetStorage {

	override fun save(widget: CreatingWidgetView): WidgetEntity {
		val widget = WidgetEntity(
			x = widget.x,
			y = widget.y,
			z = widget.z,
			width = widget.width,
			height = widget.height
		)
		dao.save(widget)
		// надо править Z индексы у других
		return widget
	}

	override fun getAll(): Collection<WidgetEntity> {
		return dao.findAll().toList()
	}

	override fun getBy(id: Long): WidgetEntity? {
		val widgetOpt = dao.findById(id)
		return if (widgetOpt.isPresent) widgetOpt.get() else null
	}
}
