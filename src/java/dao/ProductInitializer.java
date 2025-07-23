package dao;

import adt.*;
import entity.Product;

/**
 *
 * @author Kat Tan
 */

public class ProductInitializer {

//  Method to return a collection of with hard-coded entity values
  public ListInterface<Product> initializeProducts() {
    ListInterface<Product> pList = new ArrayList<>();
    pList.add(new Product("A1001", "Book", 10));
    pList.add(new Product("A1002", "Pen", 50));
    pList.add(new Product("A1003", "Pencil", 30));
    pList.add(new Product("A1004", "Notepad", 40));
    pList.add(new Product("A1005", "Ruler", 15));
    pList.add(new Product("A1006", "Eraser", 60));
    return pList;
  }

  public static void main(String[] args) {
    // To illustrate how to use the initializeProducts() method
    ProductInitializer p = new ProductInitializer();
    ListInterface<Product> productList = p.initializeProducts();
    System.out.println("\nProducts:\n" + productList);
  }

}
