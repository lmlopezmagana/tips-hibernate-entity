package net.openwebinars.hibernate.entities;

import jakarta.persistence.EntityManager;

import jakarta.persistence.TypedQuery;
import net.openwebinars.hibernate.entities.model.Product;
import org.h2.tools.Server;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class App {

    public static void main(String[] args) {

        try {
            startDatabase();
            showDatabaseMessage();
        } catch (SQLException ex) {
            System.out.println("Error inicializando la base de datos");
            ex.printStackTrace();
        }

        Session session = initSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        try {
            Product p = new Product(1L, "One product", "http://", 1.25);
            session.persist(p);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
                throw e;
        }


        session.createQuery("from Product p", Product.class)
                .getResultStream()
                .forEach(p1 -> System.out.println(String.format("%d %s %.2f", p1.getId(), p1.getName(), p1.getPrice())));


        session.close();

    }

    private static void startDatabase() throws SQLException {
        new Server().runTool("-tcp", "-web", "-ifNotExists");
    }

    private static void showDatabaseMessage() {
        System.out.println("Abre el siguiente enlace en tu navegador: http://localhost:8082/");
        System.out.println("URL JDBC: jdbc:h2:mem:database");
        System.out.println("Username: sa");
        System.out.println("Password: ");
    }

    private static SessionFactory initSessionFactory() {
        SessionFactory sessionFactory = null;

        Map<String, Object> settings = new HashMap<>();

        settings.put("hibernate.connection.driver_class", "org.h2.Driver");
        settings.put("hibernate.connection.url", "jdbc:h2:mem:database");
        settings.put("hibernate.connection.username", "sa");
        settings.put("hibernate.connection.password", "");
        settings.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        //settings.put("hibernate.current_session_context_class", "thread");
        settings.put("hibernate.show_sql", "true");
        //settings.put("hibernate.format_sql", "true");
        settings.put("hibernate.hbm2ddl.auto", "create");

        try {
            ServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(settings).build();

            Metadata metadata = new MetadataSources(standardRegistry)
                    .addAnnotatedClass(Product.class)
                    .getMetadataBuilder()
                    .build();

            sessionFactory = metadata.getSessionFactoryBuilder().build();

        } catch (Exception ex) {
            throw new ExceptionInInitializerError(ex);
        }

        return sessionFactory;

    }

}
