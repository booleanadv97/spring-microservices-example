package com.example.ecommerce.order.config

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
    basePackages = ["com.example.ecommerce.order.repository"],
    entityManagerFactoryRef = "orderEntityManagerFactory",
    transactionManagerRef = "orderTransactionManager"
)
class OrderDataSourceConfig {
    @Autowired
    private val env: Environment? = null

    @Bean
    @ConfigurationProperties(prefix = "datasource.order")
    fun orderDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    fun orderDataSource(): DataSource {
        val orderDataSourceProperties = orderDataSourceProperties()
        return DataSourceBuilder.create()
            .driverClassName(orderDataSourceProperties.driverClassName)
            .url(orderDataSourceProperties.url)
            .username(orderDataSourceProperties.username)
            .password(orderDataSourceProperties.password)
            .build()
    }

    @Bean
    fun orderTransactionManager(): PlatformTransactionManager {
        val factory = orderEntityManagerFactory().getObject()
        return JpaTransactionManager(factory!!)
    }

    @Bean
    fun orderEntityManagerFactory(): LocalContainerEntityManagerFactoryBean {
        val factory = LocalContainerEntityManagerFactoryBean()
        factory.dataSource = orderDataSource()
        factory.setPackagesToScan(*arrayOf("com.example.ecommerce.order.model"))
        factory.jpaVendorAdapter = HibernateJpaVendorAdapter()

        val jpaProperties = Properties()
        jpaProperties["hibernate.hbm2ddl.auto"] = env!!.getProperty("spring.jpa.hibernate.ddl-auto")
        jpaProperties["hibernate.generate-ddl"] = env.getProperty("spring.jpa.generate-ddl")
        jpaProperties["hibernate.show-sql"] = env.getProperty("spring.jpa.show-sql")
        factory.setJpaProperties(jpaProperties)

        return factory
    }
}