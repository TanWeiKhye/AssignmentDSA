// Medicine.java - Entity class representing a medicine
package entity;

public class Medicine implements Comparable<Medicine> {
    private String medicineId;
    private String name;
    private String description;
    private double price;
    
    public Medicine(String medicineId, String name, String description, double price) {
        this.medicineId = medicineId;
        this.name = name;
        this.description = description;
        this.price = price;
    }
    
    // Getters and setters
    public String getMedicineId() { return medicineId; }
    public void setMedicineId(String medicineId) { this.medicineId = medicineId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    @Override
    public int compareTo(Medicine other) {
        return this.medicineId.compareTo(other.medicineId);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medicine medicine = (Medicine) o;
        return medicineId.equals(medicine.medicineId);
    }
    
@Override
public int hashCode() {
    return medicineId != null ? medicineId.hashCode() : 0;
}
    @Override
    public String toString() {
        return "Medicine{" +
                "medicineId='" + medicineId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}