import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List; // Ensure we import the correct List

class Food {
    String category;
    String description;
    String nutrientDataBankNumber;
    String[] nutrients; // Array to hold nutrient information

    public Food(String category, String description, String nutrientDataBankNumber, String[] nutrients) {
        this.category = category;
        this.description = description;
        this.nutrientDataBankNumber = nutrientDataBankNumber;
        this.nutrients = nutrients; // Storing nutrients in an array
    }

    public String getDescription() {
        return description;
    }

    public String displayInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Category: ").append(category).append("\n")
            .append("Description: ").append(description).append("\n")
            .append("Nutrient Data Bank Number: ").append(nutrientDataBankNumber).append("\n")
            .append("Nutritional Information:\n");

        // Display nutrients (assuming nutrients start from index 3)
        String[] nutrientNames = {
            "Alpha Carotene", "Beta Carotene", "Beta Cryptoxanthin", "Carbohydrate",
            "Cholesterol", "Choline", "Fiber", "Kilocalories", "Lutein and Zeaxanthin",
            "Lycopene", "Manganese", "Niacin", "Pantothenic Acid", "Protein",
            "Refuse Percentage", "Retinol", "Riboflavin", "Selenium", "Sugar Total",
            "Thiamin", "Water", "Monosaturated Fat", "Polysaturated Fat", "Saturated Fat",
            "Total Lipid", "Calcium", "Copper", "Iron", "Magnesium", "Phosphorus",
            "Potassium", "Sodium", "Zinc", "Vitamin A - IU", "Vitamin A - RAE",
            "Vitamin B12", "Vitamin B6", "Vitamin C", "Vitamin E", "Vitamin K"
        };

        for (int i = 0; i < nutrientNames.length; i++) {
            if (i < nutrients.length) {
                info.append(nutrientNames[i]).append(": ").append(nutrients[i]).append("\n");
            }
        }

        return info.toString();
    }
}

public class FoodAppGUI {
    private List<Food> foodList;
    private JComboBox<String> categoryComboBox;
    private JComboBox<String> foodComboBox;
    private JTextArea infoTextArea;
    private JTextField searchField;

    public FoodAppGUI() {
        foodList = new ArrayList<>();
        loadFoodData("food.csv");
        loadFoodData("food1.csv");
        createAndShowGUI();
    }

    private void loadFoodData(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String[] nutrients = Arrays.copyOfRange(data, 3, data.length); // Capture all nutrients
                Food food = new Food(data[0], data[1], data[2], nutrients);
                foodList.add(food);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Food Information");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        categoryComboBox = new JComboBox<>();
        foodComboBox = new JComboBox<>();
        infoTextArea = new JTextArea(10, 30);
        infoTextArea.setEditable(false);
        searchField = new JTextField(15);
        searchField.addActionListener(e -> searchFood());

        // Populate categories
        Set<String> categories = new HashSet<>();
        for (Food food : foodList) {
            categories.add(food.category);
        }
        for (String category : categories) {
            categoryComboBox.addItem(category);
        }

        categoryComboBox.addActionListener(e -> updateFoodComboBox());

        foodComboBox.addActionListener(e -> displayFoodInfo());

        topPanel.add(new JLabel("Select Category:"));
        topPanel.add(categoryComboBox);
        topPanel.add(new JLabel("Select Food:"));
        topPanel.add(foodComboBox);
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(infoTextArea), BorderLayout.CENTER);

        frame.setSize(500, 400);
        frame.setVisible(true);
    }

    private void updateFoodComboBox() {
        String selectedCategory = (String) categoryComboBox.getSelectedItem();
        foodComboBox.removeAllItems();
        for (Food food : foodList) {
            if (food.category.equals(selectedCategory)) {
                foodComboBox.addItem(food.getDescription());
            }
        }
    }

    private void displayFoodInfo() {
        String selectedFoodDesc = (String) foodComboBox.getSelectedItem();
        for (Food food : foodList) {
            if (food.getDescription().equals(selectedFoodDesc)) {
                infoTextArea.setText(food.displayInfo());
                return;
            }
        }
    }

    private void searchFood() {
        String query = searchField.getText().toLowerCase();
        foodComboBox.removeAllItems();
        for (Food food : foodList) {
            if (food.getDescription().toLowerCase().contains(query)) {
                foodComboBox.addItem(food.getDescription());
            }
        }
        if (foodComboBox.getItemCount() == 0) {
            infoTextArea.setText("No matching food found.");
        } else {
            foodComboBox.setSelectedIndex(0);
            displayFoodInfo();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FoodAppGUI::new);
    }
}
