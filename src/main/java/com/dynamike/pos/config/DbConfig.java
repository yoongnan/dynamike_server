package com.dynamike.pos.config;


import java.util.HashMap;

import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
//@PropertySource({ "classpath:database-${spring.profiles.active}.properties" })
@PropertySource({ "classpath:database.properties" })
@EnableJpaRepositories(basePackages = "com.dynamike.pos.model.entities", entityManagerFactoryRef = "posEntityManagerFactory", transactionManagerRef = "posTransactionManager")
@EnableTransactionManagement
public class DbConfig {
    public static final String VALIDATION_QUERY = "SELECT 1";

    private static final String BASE_PACKAGE = "com.dynamike.pos.model.entities";

    @Bean
    @Primary
    @ConfigurationProperties("pos.datasource")
    public DataSource posDataSource() {
        DataSource datasource = posDataSourceProperties().initializeDataSourceBuilder().build();
        return datasource;
    }

    @Bean
    @Primary
    @ConfigurationProperties("pos.datasource")
    public DataSourceProperties posDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "posTransactionManager")
    @Primary
    public PlatformTransactionManager posTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(posEntityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean(name = "posEntityManagerFactory")
    @PersistenceContext(unitName = "posdatabase")
    @Primary
    public LocalContainerEntityManagerFactoryBean posEntityManagerFactory() {        
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
//        jpaVendorAdapter.setGenerateDdl(false);
//        jpaVendorAdapter.setShowSql(true);
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        factoryBean.setDataSource(posDataSource());
        factoryBean.setPersistenceUnitName("posdatabase");
        factoryBean.setPackagesToScan(new String[] { BASE_PACKAGE });
        return factoryBean;
    }

}
