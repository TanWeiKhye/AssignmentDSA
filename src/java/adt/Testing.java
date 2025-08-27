/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adt;

/**
 *
 * @author TanWeiKhye
 */
public class Testing {
    public static void main (String[] args){
     AVLTree<Integer> avl = new AVLTree<>();

        System.out.println("=== AVL Tree Test ===");

        // 1. Empty Tree Check
        System.out.println("1. Empty Tree Check:");
        System.out.println("Is empty? " + avl.isEmpty());
        System.out.println("Insert number: 10");
        avl.insert(10);
        System.out.println("Is empty? " + avl.isEmpty());
        
        // 2. Basic Insertions (check balancing)
        System.out.println("\n2. Basic Insertions and Rotations:");
        System.out.println("Insert ascending order: 10, 20, 30 (should trigger Left Rotation)");
        avl.insert(10);
        avl.insert(20);
        avl.insert(30);
        System.out.println("Min: " + avl.getMin() + " | Max: " + avl.getMax());
        System.out.println();
        
        // Reset tree
        avl = new AVLTree<>();
        System.out.println("Insert descending order: 30, 20, 10 (should trigger Right Rotation)");
        avl.insert(30);
        avl.insert(20);
        avl.insert(10);
        System.out.println("Min: " + avl.getMin() + " | Max: " + avl.getMax());
        System.out.println();
        
        // Reset tree
        avl = new AVLTree<>();
        System.out.println("Insert zig-zag: 10, 30, 20 (should trigger Right-Left Rotation)");
        avl.insert(10);
        avl.insert(30);
        avl.insert(20);
        System.out.println("Min: " + avl.getMin() + " | Max: " + avl.getMax());
        System.out.println();
        
        // 3. Larger Insertions
        avl = new AVLTree<>();
        System.out.println("3. Larger Insertion Test:");
        int[] values = {10, 20, 30, 40, 50, 25};
        for (int v : values) {
            avl.insert(v);
            System.out.println("Inserted: " + v + " | Min: " + avl.getMin() + " | Max: " + avl.getMax());
        }
        System.out.println();
        
        // 4. Deletion Tests
        System.out.println("4. Deletion Tests:");
        System.out.println("Deleting 40...");
        avl.delete(40);
        System.out.println("Min: " + avl.getMin() + " | Max: " + avl.getMax());

        System.out.println("Deleting 10...");
        avl.delete(10);
        System.out.println("Min: " + avl.getMin() + " | Max: " + avl.getMax());

        System.out.println("Deleting root (30)...");
        avl.delete(30);
        System.out.println("Min: " + avl.getMin() + " | Max: " + avl.getMax());
        System.out.println();
        
        // 5. Emptying the Tree
        System.out.println("5. Emptying the Tree:");
        avl.delete(20);
        avl.delete(25);
        avl.delete(50);
        System.out.println("Is empty after all deletions? " + avl.isEmpty());
        System.out.println();
        
        // 6. Stress Test (Optional small scale)
        avl = new AVLTree<>();
        System.out.println("6. Stress Test:");
        for (int i = 1; i <= 15; i++) {
            avl.insert(i);
        }
        System.out.println("Inserted 1 to 15");
        System.out.println("Min: " + avl.getMin() + " | Max: " + avl.getMax());
        System.out.println("Is empty? " + avl.isEmpty());
        
        // 7. Traverse Order
        avl = new AVLTree<>();
        
        avl.insert(10);
        avl.insert(20);
        avl.insert(30);
        avl.insert(40);
        avl.insert(50);
        avl.insert(25);

        System.out.println("\n=== Test Completed ===");
    }
}
