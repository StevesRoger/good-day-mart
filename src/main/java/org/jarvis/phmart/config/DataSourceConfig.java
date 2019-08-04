package org.jarvis.phmart.config;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.jarvis.jdbc.factory.DatasourceFactory;
import org.jarvis.jdbc.factory.DatasourceFactoryBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created: chheng
 * Date: 22-Aug-2018 Wed
 * Time: 10:28 PM
 */
@Configuration
@EntityScan(basePackages = {
        "org.jarvis.orm.hibernate.domain",
        "org.jarvis.sercurity.domain.entity",
        "org.jarvis.phmart.model.entity"})
//@EnableTransactionManagement
public class DataSourceConfig {

    // @Bean
    public DataSource dataSource() {
        DatasourceFactory datasourceFactory = new DatasourceFactoryBean();
        datasourceFactory.setDriver(DatasourceFactory.Driver.Postgres);
        datasourceFactory.setUrl("jdbc:postgresql://localhost:5432/testing");
        datasourceFactory.setUsername("postgres");
        datasourceFactory.setPassword("123");
        datasourceFactory.setSchema("public");
        datasourceFactory.setType(DatasourceFactory.Type.Basic);
        return datasourceFactory.getDataSource();
    }

    //@Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("org.jarvis.orm.hibernate.model", "org.jarvis.phmart.model");
        em.setPersistenceUnitName("persistenceUnit");
        em.setSharedCacheMode(SharedCacheMode.ENABLE_SELECTIVE);
        em.setPersistenceProvider(new HibernatePersistenceProvider());
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(additionalProperties());
        return em;
    }


    Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL94Dialect");
        properties.setProperty("hibernate.show_sql", "false");
        properties.setProperty("hibernate.format_sql", "false");
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        //properties.setProperty("hibernate.current_session_context_class", "org.hibernate.context.internal.ThreadLocalSessionContext");
        //properties.setProperty("hibernate.current_session_context_class", "org.springframework.orm.hibernate5.SpringSessionContext");
        //properties.setProperty("hibernate.current_session_context_class", "org.hibernate.context.internal.JTASessionContext");
        //properties.setProperty("hibernate.current_session_context_class", "org.hibernate.context.internal.ManagedSessionContext");
        //properties.setProperty("hibernate.current_session_context_class", "thread");
        properties.setProperty("hibernate.jdbc.lob.non_contextual_creation", "true");
        properties.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false");
        properties.setProperty("hibernate.enable_lazy_load_no_trans", "true");
        properties.setProperty("hibernate.default_schema", "public");

        /*properties.setProperty("org.hibernate.envers.default_schema", "public");
        properties.setProperty("org.hibernate.envers.audit_table_suffix", "_aud");
        //properties.setProperty("org.hibernate.envers.audit_table_prefix", "aud");
        properties.setProperty("org.hibernate.envers.revision_field_name", "rev_audit_id");
        properties.setProperty("org.hibernate.envers.revision_type_field_name", "rev_audit_action");
        properties.setProperty("org.hibernate.envers.audit_strategy", "org.hibernate.envers.strategy.DefaultAuditStrategy");
        properties.setProperty("org.hibernate.envers.use_revision_entity_with_native_id", "false");*/
        return properties;
    }

    /*@Bean
    public HibernateTransactionManager transactionManager(EntityManagerFactory entityManagerFactory){
        HibernateTransactionManager transactionManager=new HibernateTransactionManager();
        transactionManager.setSessionFactory(entityManagerFactory.unwrap(SessionFactory.class));
        return transactionManager;
    }*/

    //@Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        // transactionManager.setPersistenceUnitName("persistenceUnit");
        transactionManager.setDataSource(dataSource());
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
