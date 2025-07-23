package dao;

import adt.*;
import entity.Product;
import java.io.*;

/**
 *
 * @author Kat Tan
 */
public class ProductDAO {
  private String fileName = "products.dat"; // For security and maintainability, should not have filename hardcoded here.
  
  public void saveToFile(ListInterface<Product> productList) {
    File file = new File(fileName);
    try {
      ObjectOutputStream ooStream = new ObjectOutputStream(new FileOutputStream(file));
      ooStream.writeObject(productList);
      ooStream.close();
    } catch (FileNotFoundException ex) {
      System.out.println("\nFile not found");
    } catch (IOException ex) {
      System.out.println("\nCannot save to file");
    }
  }

  public ListInterface<Product> retrieveFromFile() {
    File file = new File(fileName);
    ListInterface<Product> productList = new ArrayList<>();
    try {
      ObjectInputStream oiStream = new ObjectInputStream(new FileInputStream(file));
      productList = (ArrayList<Product>) (oiStream.readObject());
      oiStream.close();
    } catch (FileNotFoundException ex) {
      System.out.println("\nNo such file.");
    } catch (IOException ex) {
      System.out.println("\nCannot read from file.");
    } catch (ClassNotFoundException ex) {
      System.out.println("\nClass not found.");
    } finally {
      return productList;
    }
  }
}
