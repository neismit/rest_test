package com.sft_test.storage

import com.sft_test.WidgetView
import com.sft_test.Widget
import com.sft_test.WidgetEntity
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class WidgetStorageInMemoryImpl : WidgetStorage {

    private val indexGenerator = AtomicLong(0L)

    private val store = ConcurrentHashMap<Long, WidgetEntity>()

    // ? можно заменить на лок по коллекции
    private val zIndexChangeLock = ReentrantLock()

    override fun create(widget: WidgetView): Widget {
        zIndexChangeLock.withLock {
            val zIndex = calculateZIndex(widget.z)
            val widgetEntity = createWidgetWithNewZ(widget, zIndex)
            store[widgetEntity.id] = widgetEntity
            return widgetEntity
        }
    }

    private fun createWidgetWithNewZ(widget: WidgetView, zIndex: Long): WidgetEntity {
        return WidgetEntity(
                id = indexGenerator.getAndIncrement(),
                x = widget.x ?: 0,
                y = widget.y ?: 0,
                z = zIndex,
                width = widget.width ?: 0.0,
                height = widget.height ?: 0.0
        )
    }

    private fun calculateZIndex(newZ: Long?): Long {
        // на передний план
        if (newZ == null) {
            if (store.isEmpty()) return 0
            val maxZ = store.values.asSequence().map { it.z }.max() ?: 0
            return maxZ + 1
        }
        val widgets = store.values.filter { w -> w.z >= newZ }

        // что бы при запросе коллекции не получилось что она имеет 2 элемента с одинаковыми Z индексами
        // сортируем и инкрементим каждый индекс в порядке убывания индексов
        // новый во время операции добавлен не будет т.к. мы в локе на создание элементов
        val sortedByZWidgets = widgets.sortedByDescending { it.z }
        sortedByZWidgets.forEach { it.z += 1 }
        return newZ
    }

    override fun getAll(): Collection<Widget> {
        return store.values
    }

    override fun getBy(id: Long): Widget? {
        return store[id]
    }

    override fun remove(id: Long): Boolean {
        return store.remove(id) != null
    }

    override fun update(id: Long, widget: WidgetView): Widget? {
        val widgetEntity = store[id] ?: return null
        val updatedWidget = merge(widgetEntity, widget)
        zIndexChangeLock.withLock {
            if (widgetEntity.z != updatedWidget.z) {
                updatedWidget.z = calculateZIndex(updatedWidget.z)
            }
            store[updatedWidget.id] = updatedWidget
            return updatedWidget
        }
    }

    private fun merge(old: WidgetEntity, new: WidgetView): WidgetEntity {
        return WidgetEntity(
                id = old.id,
                x = new.x ?: old.x,
                y = new.y ?: old.y,
                z = new.z ?: old.z,
                width = new.width ?: old.width,
                height = new.height ?: old.height
        )
    }
}