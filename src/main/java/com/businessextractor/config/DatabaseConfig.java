//package com.businessextractor.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//
//import javax.sql.DataSource;
//import org.apache.commons.dbcp2.BasicDataSource;
//import java.util.Properties;
//
//@Configuration
//public class DatabaseConfig {
//
//    @Bean
//    @Primary
//    public DataSource dataSource() {
//        BasicDataSource dataSource = new BasicDataSource();
//
//        // Set database connection properties
//        dataSource.setUrl("jdbc:mysql://localhost:3306/businessextractor?characterEncoding=UTF-8&jdbcCompliantTruncation=true&useSSL=false");
//        dataSource.setUsername("root");
//        dataSource.setPassword("root@123");
//        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//
//        // Connection pool settings
//        dataSource.setInitialSize(4);
//        dataSource.setMaxTotal(4);
//        dataSource.setMaxIdle(4);
//        dataSource.setMinIdle(3);
//        dataSource.setMaxWaitMillis(3000);
//
//        return dataSource;
//    }
//
//    @Bean
//    @Primary
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
//        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
//        factoryBean.setDataSource(dataSource());
//        factoryBean.setPackagesToScan("com.businessextractor.entity");
//
//        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        factoryBean.setJpaVendorAdapter(vendorAdapter);
//
//        Properties properties = new Properties();
//        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
//        properties.setProperty("hibernate.show_sql", "true");
//        properties.setProperty("hibernate.format_sql", "true");
//        properties.setProperty("hibernate.hbm2ddl.auto", "update");
//        properties.setProperty("hibernate.bytecode.use_reflection_optimizer", "false");
//        properties.setProperty("hibernate.search.default.directory_provider", "org.hibernate.search.store.filesystem");
//        properties.setProperty("hibernate.search.default.indexBase", "/Users/sohani/Index");
//        properties.setProperty("hibernate.search.reader.strategy", "shared");
//
//        factoryBean.setJpaProperties(properties);
//
//        return factoryBean;
//    }
//
//    @Bean
//    @Primary
//    public JpaTransactionManager transactionManager() {
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
//        return transactionManager;
//    }
//}
//
