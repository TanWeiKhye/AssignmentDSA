package entity;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Kat Tan
 */
public class Product implements Serializable {
  private String number;
  private String name;
  private int quantity;

  public Product() {
  }

  public Product(String number, String name, int quantity) {
    this.number = number;
    this.name = name;
    this.quantity = quantity;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  @Override
  public int hashCode() {
    int hash = 3;
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
    if (!Objects.equals(this.number, other.number)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return String.format("%-10s %-40s %10d", number, name, quantity);
  }

}
