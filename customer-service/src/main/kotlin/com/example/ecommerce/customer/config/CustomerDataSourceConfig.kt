package com.example.ecommerce.customer.config

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
    basePackages = ["com.example.ecommerce.customer.repository"],
    entityManagerFactoryRef = "customerEntityManagerFactory",
    transactionManagerRef = "customerTransactionManager"
)
class CustomerDataSourceConfig {
    @Autowired
    private val env: Environment? = null

    @Bean
    @ConfigurationProperties(prefix = "datasource.customer")
    fun customerDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    fun customerDataSource(): DataSource {
        val customerDataSourceProperties = customerDataSourceProperties()
        return DataSourceBuilder.create()
            .driverClassName(customerDataSourceProperties.driverClassName)
            .url(customerDataSourceProperties.url)
            .username(customerDataSourceProperties.username)
            .password(customerDataSourceProperties.password)
            .build()
    }

    @Bean
    fun customerTransactionManager(): PlatformTransactionManager {
        val factory = customerEntityManagerFactory().getObject()
        return JpaTransactionManager(factory!!)
    }

    @Bean
    fun customerEntityManagerFactory(): LocalContainerEntityManagerFactoryBean {
        val factory = LocalContainerEntityManagerFactoryBean()
        factory.dataSource = customerDataSource()
        factory.setPackagesToScan(*arrayOf("com.example.ecommerce.customer.model"))
        factory.jpaVendorAdapter = HibernateJpaVendorAdapter()

        val jpaProperties = Properties()
        jpaProperties["hibernate.hbm2ddl.auto"] = env!!.getProperty("spring.jpa.hibernate.ddl-auto")
        jpaProperties["hibernate.show-sql"] = env.getProperty("spring.jpa.show-sql")
        jpaProperties["hibernate.dialect"] = "org.hibernate.dialect.MySQLDialect"
        factory.setJpaProperties(jpaProperties)

        return factory
    }
}