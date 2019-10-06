package com.sft_test.storage

import com.sft_test.WidgetView
import com.sft_test.WidgetDAO
import com.sft_test.WidgetEntity
import org.springframework.beans.factory.annotation.Autowired

class WidgetStorageH2Impl : WidgetStorage {

    @Autowired
    private lateinit var dao: WidgetDAO

    override fun create(widget: WidgetView): WidgetEntity {
        val widgetEntity = WidgetEntity(
            x = widget.x,
            y = widget.y,
            z = widget.z ?: 0,
            width = widget.width,
            height = widget.height
        )
        dao.save(widgetEntity)
        // надо править Z индексы у других компонентов
        return widgetEntity
    }

    override fun getAll(): Collection<WidgetEntity> {
        return dao.findAll().toList()
    }

    override fun getBy(id: Long): WidgetEntity? {
        val widgetOpt = dao.findById(id)
        return if (widgetOpt.isPresent) widgetOpt.get() else null
    }

    override fun remove(id: Long): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
