package net.openwebinars.jpa.entities;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import net.openwebinars.jpa.entities.model.book.Book;
import net.openwebinars.jpa.entities.model.book.Library;
import net.openwebinars.jpa.entities.model.products.Product;
import net.openwebinars.jpa.entities.model.products.Productv2;
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

        //testProducts(entityManager);

        //testBooks(entityManager);

        //testBooksV2(entityManager);

        testBooksV3(entityManager);





    }

    public static void testBooks(EntityManager entityManager) {

        Library l = new Library();
        l.setName("Openwebinars Library");
        entityManager.getTransaction().begin();
        entityManager.persist(l);
        entityManager.getTransaction().commit();

        Book book1 = new Book();
        book1.setTitle("El Quijote");

        Book book2 = new Book();
        book2.setTitle("Hibernate for dummies");

        l.getBooks().add(book1);
        l.getBooks().add(book2);

        entityManager.getTransaction().begin();
        entityManager.persist(l);
        entityManager.getTransaction().commit();

    }

    public static void testBooksV2(EntityManager entityManager) {

        Library l = new Library();
        l.setName("Openwebinars Library");
        entityManager.getTransaction().begin();
        entityManager.persist(l);
        entityManager.getTransaction().commit();

        Book book1 = new Book();
        book1.setTitle("El Quijote");

        Book book2 = new Book();
        book2.setTitle("Hibernate for dummies");

        l.getBooks().add(book1);
        l.getBooks().add(book2);

        entityManager.getTransaction().begin();
        //entityManager.persist(book1);
        //entityManager.persist(book2);
        entityManager.persist(l);
        entityManager.getTransaction().commit();

    }
    public static void testBooksV3(EntityManager entityManager) {

        Library l = new Library();
        l.setName("Openwebinars Library");
        entityManager.getTransaction().begin();
        entityManager.persist(l);
        entityManager.getTransaction().commit();

        Book book1 = new Book();
        book1.setTitle("El Quijote");
        book1.setIsbn("978-123456789");

        Book book2 = new Book();
        book2.setTitle("Hibernate for dummies");
        book2.setIsbn("978-987654321");

        l.getBooks().add(book1);
        l.getBooks().add(book2);

        entityManager.getTransaction().begin();
        entityManager.persist(book1);
        entityManager.persist(book2);
        entityManager.persist(l);
        entityManager.getTransaction().commit();

    }


    public static void testProducts(EntityManager entityManager) {
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
