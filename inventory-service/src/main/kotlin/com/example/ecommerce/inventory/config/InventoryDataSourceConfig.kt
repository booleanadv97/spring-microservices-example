package com.example.ecommerce.inventory.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import java.util.*
import javax.sql.DataSource

@Configuration
@EnableJpaRepositories(
    basePackages = ["com.example.ecommerce.inventory.repository"],
    entityManagerFactoryRef = "inventoryEntityManagerFactory",
    transactionManagerRef = "inventoryTransactionManager"
)
class InventoryDataSourceConfig {
    @Autowired
    private val env: Environment? = null

    @Bean
    @ConfigurationProperties(prefix = "datasource.inventory")
    fun inventoryDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    fun inventoryDataSource(): DataSource {
        val inventoryDataSourceProperties = inventoryDataSourceProperties()
        return DataSourceBuilder.create()
            .driverClassName(inventoryDataSourceProperties.driverClassName)
            .url(inventoryDataSourceProperties.url)
            .username(inventoryDataSourceProperties.username)
            .password(inventoryDataSourceProperties.password)
            .build()
    }

    @Bean
    fun inventoryTransactionManager(): PlatformTransactionManager {
        val factory = inventoryEntityManagerFactory().getObject()
        return JpaTransactionManager(factory!!)
    }

    @Bean
    fun inventoryEntityManagerFactory(): LocalContainerEntityManagerFactoryBean {
        val factory = LocalContainerEntityManagerFactoryBean()
        factory.dataSource = inventoryDataSource()
        factory.setPackagesToScan(*arrayOf("com.example.ecommerce.inventory.model"))
        factory.jpaVendorAdapter = HibernateJpaVendorAdapter()

        val jpaProperties = Properties()
        jpaProperties["hibernate.hbm2ddl.auto"] = env!!.getProperty("spring.jpa.hibernate.ddl-auto")
        jpaProperties["hibernate.generate-ddl"] = env.getProperty("spring.jpa.generate-ddl")
        jpaProperties["hibernate.show-sql"] = env.getProperty("spring.jpa.show-sql")
        factory.setJpaProperties(jpaProperties)

        return factory
    }
}