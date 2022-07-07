package net.openwebinars.jpa.entities;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import net.openwebinars.jpa.entities.model.Product;
import net.openwebinars.jpa.entities.model.Productv2;
import org.h2.tools.Server;

import java.sql.SQLException;

public class App {

    public static void main(String[] args) {

        try {
            startDatabase();
            showDatabaseMessage();
        } catch (SQLException ex) {
            System.out.println("Error inicializando la base de datos");
            ex.printStackTrace();
        }

        EntityManager entityManager = initEM();

        Product p = new Product(1L, "One product", "http://", 1.25);

        entityManager.getTransaction().begin();
        entityManager.persist(p);
        entityManager.getTransaction().commit();


        entityManager.getTransaction().begin();
        TypedQuery<Product> query = entityManager.createQuery("select p from Product p", Product.class);
        query.getResultStream()
                        .forEach(p1 -> System.out.println(String.format("%d %s %.2f", p1.getId(), p1.getName(), p1.getPrice())));
        entityManager.getTransaction().commit();


        Productv2 p2 = Productv2.builder()
                .id(2L)
                .name("Another product")
                .price(2.25)
                .build();


        entityManager.getTransaction().begin();
        entityManager.persist(p2);
        entityManager.getTransaction().commit();

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

    private static EntityManager initEM() {
        final EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("net.openwebinars.persistence-unit");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager;
    }

}
