package com.sft_test

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class ApiError(
	val status: HttpStatus,
	val message: String,
	@get:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	val timestamp: LocalDateTime = LocalDateTime.now()
)
