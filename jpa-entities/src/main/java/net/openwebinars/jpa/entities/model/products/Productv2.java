package net.openwebinars.jpa.entities.model.products;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class Productv2 {

    @Id
    private Long id;

    private String name;
    private String imageUrl;
    private double price;

    public Productv2(String name, String imageUrl, double price) {
        this(null, name, imageUrl, price);
    }
}
