package com.sft_test.storage

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import javax.annotation.Generated
import javax.persistence.*

interface Widget {
	var id: Long
	var x: Long
	var y: Long
	var z: Long
	var width: Double
	var height: Double
	var lastModified: LocalDateTime
}

@Entity
@Table(name = "Widget")
@JsonIgnoreProperties(ignoreUnknown = true)
data class WidgetEntity(
	@get:GeneratedValue(strategy = GenerationType.SEQUENCE)
	@get:Id
	override var id: Long = 0,
	override var x: Long = 0,
	override var y: Long = 0,
	override var z: Long = 0,
	override var width: Double = 0.0,
	override var height: Double = 0.0,
	@get:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	override var lastModified: LocalDateTime = LocalDateTime.now()
) : Widget

@Service
@Repository
interface WidgetDAO : CrudRepository<WidgetEntity, Long>
