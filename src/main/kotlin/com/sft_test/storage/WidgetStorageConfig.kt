package com.sft_test.storage

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

const val STORAGE_TYPE_PROP = "storage.type"

@Configuration
class WidgetStorageConfig {
    @Bean
    @ConditionalOnProperty(name = [STORAGE_TYPE_PROP], havingValue = "memory", matchIfMissing = true)
    fun getInMemoryStorage(): WidgetStorage = WidgetStorageInMemoryImpl()

    @Bean
    @ConditionalOnProperty(name = [STORAGE_TYPE_PROP], havingValue = "rdb")
    fun getRdbStorage(): WidgetStorage = WidgetStorageH2Impl()
}