package entity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Product implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private double price;
    private int quantity;
    @Lob
    private byte[] cover;

    public Product() {
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public byte[] getCover() {
        return cover;
    }

    public void setCover(byte[] cover) {
        this.cover = cover;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.name);
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.price) ^ (Double.doubleToLongBits(this.price) >>> 32));
        hash = 97 * hash + this.quantity;
        hash = 97 * hash + Arrays.hashCode(this.cover);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Product other = (Product) obj;
        if (Double.doubleToLongBits(this.price) != Double.doubleToLongBits(other.price)) {
            return false;
        }
        if (this.quantity != other.quantity) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Arrays.equals(this.cover, other.cover)) {
            return false;
        }
        return true;
    }
    
    public InputStream getCoverAsStream(){
        return new ByteArrayInputStream(this.cover);
    }    

    public StringProperty idProperty(){
        return new SimpleStringProperty(String.valueOf(id));
    }
    
    public StringProperty nameProperty() {
        return new SimpleStringProperty(name);
    }   
    
    public StringProperty quantityProperty() {
        return new SimpleStringProperty(String.valueOf(quantity));
    }
    
    public StringProperty priceProperty() {
        return new SimpleStringProperty(String.valueOf(price));
    }
    
    
    // Уменьшаем количество продукта
    public void decreaseQuantity(int amount) {
        if (quantity >= amount) {
            quantity -= amount;
        } else {
            // Если запрошенное количество больше, чем доступное, можно сделать что-то еще, например, выкинуть ошибку
            throw new IllegalArgumentException("Requested quantity exceeds available quantity");
        }
    }    

}
