package com.sft_test

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.SpringApplication



@SpringBootApplication
class Application

fun main(args: Array<String>) {
	SpringApplication.run(Application::class.java, *args)
}
