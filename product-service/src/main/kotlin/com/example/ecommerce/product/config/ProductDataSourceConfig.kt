package com.example.ecommerce.product.config

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
    basePackages = ["com.example.ecommerce.product.repository"],
    entityManagerFactoryRef = "productEntityManagerFactory",
    transactionManagerRef = "productTransactionManager"
)
class ProductDataSourceConfig {
    @Autowired
    private val env: Environment? = null

    @Bean
    @ConfigurationProperties(prefix = "datasource.product")
    fun productDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    fun productDataSource(): DataSource {
        val productDataSourceProperties = productDataSourceProperties()
        return DataSourceBuilder.create()
            .driverClassName(productDataSourceProperties.driverClassName)
            .url(productDataSourceProperties.url)
            .username(productDataSourceProperties.username)
            .password(productDataSourceProperties.password)
            .build()
    }

    @Bean
    fun productTransactionManager(): PlatformTransactionManager {
        val factory = productEntityManagerFactory().getObject()
        return JpaTransactionManager(factory!!)
    }

    @Bean
    fun productEntityManagerFactory(): LocalContainerEntityManagerFactoryBean {
        val factory = LocalContainerEntityManagerFactoryBean()
        factory.dataSource = productDataSource()
        factory.setPackagesToScan(*arrayOf("com.example.ecommerce.product.model"))
        factory.jpaVendorAdapter = HibernateJpaVendorAdapter()

        val jpaProperties = Properties()
        jpaProperties["hibernate.hbm2ddl.auto"] = env!!.getProperty("spring.jpa.hibernate.ddl-auto")
        jpaProperties["hibernate.show-sql"] = env.getProperty("spring.jpa.show-sql")
        jpaProperties["hibernate.dialect"] = "org.hibernate.dialect.MySQLDialect"

        factory.setJpaProperties(jpaProperties)

        return factory
    }
}