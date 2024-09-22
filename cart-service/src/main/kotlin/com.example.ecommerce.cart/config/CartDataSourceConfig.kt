package com.example.ecommerce.cart.config

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
    basePackages = ["com.example.ecommerce.cart.repository"],
    entityManagerFactoryRef = "cartEntityManagerFactory",
    transactionManagerRef = "cartTransactionManager"
)
class CartDataSourceConfig {
    @Autowired
    private val env: Environment? = null

    @Bean
    @ConfigurationProperties(prefix = "datasource.cart")
    fun cartDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    fun cartDataSource(): DataSource {
        val cartDataSourceProperties = cartDataSourceProperties()
        return DataSourceBuilder.create()
            .driverClassName(cartDataSourceProperties.driverClassName)
            .url(cartDataSourceProperties.url)
            .username(cartDataSourceProperties.username)
            .password(cartDataSourceProperties.password)
            .build()
    }

    @Bean
    fun cartTransactionManager(): PlatformTransactionManager {
        val factory = cartEntityManagerFactory().getObject()
        return JpaTransactionManager(factory!!)
    }

    @Bean
    fun cartEntityManagerFactory(): LocalContainerEntityManagerFactoryBean {
        val factory = LocalContainerEntityManagerFactoryBean()
        factory.dataSource = cartDataSource()
        factory.setPackagesToScan(*arrayOf("com.example.ecommerce.cart.model"))
        factory.jpaVendorAdapter = HibernateJpaVendorAdapter()

        val jpaProperties = Properties()
        jpaProperties["hibernate.hbm2ddl.auto"] = env!!.getProperty("spring.jpa.hibernate.ddl-auto")
        jpaProperties["hibernate.generate-ddl"] = env.getProperty("spring.jpa.generate-ddl")
        jpaProperties["hibernate.show-sql"] = env.getProperty("spring.jpa.show-sql")
        factory.setJpaProperties(jpaProperties)

        return factory
    }
}