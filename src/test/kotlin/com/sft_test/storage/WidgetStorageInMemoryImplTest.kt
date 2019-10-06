package com.sft_test.storage

import com.sft_test.WidgetEntity
import com.sft_test.WidgetView
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.assertj.core.api.Assertions.assertThat

class WidgetStorageInMemoryImplTest {

    private lateinit var memStorage: WidgetStorageInMemoryImpl

    @Before
    fun setUp() {
        memStorage = WidgetStorageInMemoryImpl()

    }

    @Test
    fun createOnEmptyStorage_Ok() {
        val x: Long = 2
        val y: Long = 4
        val createdWidget = memStorage.create(WidgetView(x = x, y = y))

        assertThat(memStorage.getAll().size).isEqualTo(1)
        assertThat(memStorage.getBy(createdWidget.id)).isNotNull
        assertThat(createdWidget.id).isZero()
        assertThat(createdWidget.z).isZero()
        assertThat(createdWidget.x).isEqualTo(x)
        assertThat(createdWidget.y).isEqualTo(y)
        assertThat(createdWidget.width).isZero()
        assertThat(createdWidget.width).isZero()
        //ToDo: брать время через Clock, что бы проверить lastModified. Добавить проверку во все тесты
//        assertThat(createdWidget.lastModified).isEqualTo()
    }

    @Test
    fun create_ZFight() {
        val z = 1L
        for(i in 0..2L) {
            memStorage.create(WidgetView(z = i))
        }
        val conflictedByZWidget = memStorage.getAll().find { it.z == z }!!
        val createdWidget = memStorage.create(WidgetView(z = z))

        assertThat(memStorage.getAll().size).isEqualTo(4)
        assertThat(memStorage.getBy(createdWidget.id)).isNotNull
        assertThat(createdWidget.z).isEqualTo(z)
        assertThat(conflictedByZWidget.z).isEqualTo(z + 1)
    }

    @Test
    fun create_ZisNull() {
        val maxZ = 2L
        for(i in 0..maxZ) {
            memStorage.create(WidgetView(z = i))
        }
        val widgets = memStorage.getAll()
        val createdWidget = memStorage.create(WidgetView())

        assertThat(memStorage.getBy(createdWidget.id)).isNotNull
        assertThat(createdWidget.z).isEqualTo(maxZ + 1)
        assertThat(widgets.all {
            val w = memStorage.getBy(it.id)!!
            it.z == w.z
        }).isTrue()
    }

    @Test
    fun create_ZBetweenIndexes() {
        //ToDo: тест когда заданный индекс между
    }

    @Test
    fun update_widgetNotFound() {
        assertThat(memStorage.update(0L, WidgetView(x = 234))).isNull()
    }

    @Test
    fun update_mergeWidgetsWithoutZ() {
        val createdWidget = memStorage.create(WidgetView(x = 1, y = 2, width = 3.0, height = 4.0))
        val startedWidget = (createdWidget as WidgetEntity).copy()

        val sourceForUpdate = WidgetView(x = 99, y = 23, width = 1.0, height = 4.5)
        val updatedWidget = memStorage.update(createdWidget.id, sourceForUpdate)!!

        assertThat(memStorage.getAll().size).isEqualTo(1)
        assertThat(memStorage.getBy(startedWidget.id)).isNotNull

        assertThat(updatedWidget.id).isEqualTo(startedWidget.id)
        assertThat(updatedWidget.x).isEqualTo(sourceForUpdate.x)
        assertThat(updatedWidget.y).isEqualTo(sourceForUpdate.y)
        assertThat(updatedWidget.width).isEqualTo(sourceForUpdate.width)
        assertThat(updatedWidget.height).isEqualTo(sourceForUpdate.height)
        assertThat(updatedWidget.z).isEqualTo(0)
    }

    @Test
    fun update_mergeWithNullFields() {
        val createdWidget = memStorage.create(WidgetView(x = 1, y = 2, width = 3.0, height = 4.0))
        val startedWidget = (createdWidget as WidgetEntity).copy()

        val sourceForUpdate = WidgetView(x = 99)
        val updatedWidget = memStorage.update(createdWidget.id, sourceForUpdate)!!

        assertThat(memStorage.getAll().size).isEqualTo(1)
        assertThat(memStorage.getBy(startedWidget.id)).isNotNull

        assertThat(updatedWidget.id).isEqualTo(startedWidget.id)
        assertThat(updatedWidget.x).isEqualTo(sourceForUpdate.x)
        assertThat(updatedWidget.y).isEqualTo(startedWidget.y)
        assertThat(updatedWidget.width).isEqualTo(startedWidget.width)
        assertThat(updatedWidget.height).isEqualTo(startedWidget.height)
    }

    @Test
    fun update_mergeZ() {
        val checkingZ = 0L
        val widgetWithSameZ = memStorage.create(WidgetView(z = 0))
        val createdWidget = memStorage.create(WidgetView(z = 1))
        val startedWidget = (createdWidget as WidgetEntity).copy()

        val sourceForUpdate = WidgetView(x = 99, z = checkingZ)
        val updatedWidget = memStorage.update(createdWidget.id, sourceForUpdate)!!

        assertThat(memStorage.getAll().size).isEqualTo(2)
        assertThat(memStorage.getBy(startedWidget.id)).isNotNull

        assertThat(updatedWidget.z).isEqualTo(checkingZ)
        assertThat(updatedWidget.id).isEqualTo(startedWidget.id)
        assertThat(updatedWidget.x).isEqualTo(sourceForUpdate.x)

        assertThat(widgetWithSameZ.z).isEqualTo(checkingZ + 1)
    }

}