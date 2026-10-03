import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BillingSystemGUI extends JFrame {

    private JTextField productIdField;
    private JTextField quantityField;
    private JTable cartTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton removeButton;
    private JButton checkoutButton;
    private JLabel totalLabel;

    // Backend models and DAOs
    private Cart cart;
    private ProductDAO productDAO;
    private BillingDAO billingDAO;

    public BillingSystemGUI() {
        cart = new Cart();
        productDAO = new ProductDAO();
        billingDAO = new BillingDAO();

        setTitle("Supermarket Billing System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // 1. Top Panel: Inputs
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.add(new JLabel("Product ID:"));
        productIdField = new JTextField(10);
        inputPanel.add(productIdField);

        inputPanel.add(new JLabel("Qty:"));
        quantityField = new JTextField("1", 5);
        inputPanel.add(quantityField);

        addButton = new JButton("Add to Cart");
        inputPanel.add(addButton);
        
        add(inputPanel, BorderLayout.NORTH);

        // 2. Center Panel: Cart Table
        String[] columns = {"Product ID", "Name", "Price", "Qty", "Amount"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Prevent manual table editing
            }
        };
        cartTable = new JTable(tableModel);
        add(new JScrollPane(cartTable), BorderLayout.CENTER);

        // 3. Bottom Panel: Controls and Totals
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        removeButton = new JButton("Remove Selected");
        checkoutButton = new JButton("Checkout");
        buttonPanel.add(removeButton);
        buttonPanel.add(checkoutButton);
        
        totalLabel = new JLabel("Total: ₹0.00  ");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));

        bottomPanel.add(buttonPanel, BorderLayout.WEST);
        bottomPanel.add(totalLabel, BorderLayout.EAST);
        
        add(bottomPanel, BorderLayout.SOUTH);


        // Add Item Action
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int productId = Integer.parseInt(productIdField.getText().trim());
                    int quantity = Integer.parseInt(quantityField.getText().trim());

                    if (quantity <= 0) {
                        JOptionPane.showMessageDialog(null, "Quantity must be greater than 0.");
                        return;
                    }

                    Product selectedProduct = productDAO.getProductById(productId);

                    if (selectedProduct == null) {
                        JOptionPane.showMessageDialog(null, "Product not found!");
                        return;
                    }

                    int alreadyInCart = cart.getQuantity(productId);
                    if (quantity + alreadyInCart > selectedProduct.getStockQuantity()) {
                        JOptionPane.showMessageDialog(null, "Out of Stock! Available: " + 
                            (selectedProduct.getStockQuantity() - alreadyInCart));
                        return;
                    }

                    cart.addItem(new CartItem(selectedProduct, quantity));
                    refreshTable();
                    
                    productIdField.setText("");
                    quantityField.setText("1");
                    productIdField.requestFocus();

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter valid numbers.");
                }
            }
        });

        // Remove Item Action
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = cartTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select an item from the table to remove.");
                    return;
                }

                // Get Product ID from the selected row (Column 0)
                int productId = (int) tableModel.getValueAt(selectedRow, 0);
                
                // Ask user how many to remove
                String input = JOptionPane.showInputDialog("Enter quantity to remove (Leave blank to remove all):");
                
                if (input == null) return; // User canceled

                if (input.trim().isEmpty()) {
                    cart.removeItem(productId); // Remove all
                } else {
                    try {
                        int qtyToRemove = Integer.parseInt(input.trim());
                        cart.removeQuantity(productId, qtyToRemove);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid quantity.");
                        return;
                    }
                }
                
                refreshTable();
            }
        });

        // Checkout Action
        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cart.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Cart is empty!");
                    return;
                }

                Bill bill = new Bill(cart.getTotal());
                int billId = billingDAO.createBill(bill.getSubtotal(), bill.getTax(), bill.getTotal());

                if (billId == -1) {
                    JOptionPane.showMessageDialog(null, "Database error: Failed to generate bill.");
                    return;
                }

                StringBuilder receipt = new StringBuilder();
                receipt.append("========== SUPERMARKET RECEIPT ==========\n");
                receipt.append("Bill ID: ").append(billId).append("\n\n");
                
                for (CartItem item : cart.getItems()) {
                    Product p = item.getProduct();
                    double amount = p.getPrice() * item.getQuantity();
                    
                    receipt.append(String.format("%-15s Qty: %-5d $%.2f\n", 
                            p.getProductName(), item.getQuantity(), amount));
                    
                    billingDAO.addBillItem(billId, p.getProductId(), item.getQuantity(), p.getPrice(), amount);
                    productDAO.reduceStock(p.getProductId(), item.getQuantity());
                }
                
                receipt.append("\n-----------------------------------------\n");
                receipt.append(String.format("Subtotal: $%.2f\n", bill.getSubtotal()));
                receipt.append(String.format("Tax (5%%): $%.2f\n", bill.getTax()));
                receipt.append(String.format("Total   : $%.2f\n", bill.getTotal()));
                receipt.append("=========================================");

                JOptionPane.showMessageDialog(null, receipt.toString(), "Transaction Complete", JOptionPane.INFORMATION_MESSAGE);
                
                cart = new Cart();
                refreshTable();
            }
        });
    }

    // Helper method to sync the JTable with the Cart object
    private void refreshTable() {
        tableModel.setRowCount(0); // Clear table
        for (CartItem item : cart.getItems()) {
            double amount = item.getProduct().getPrice() * item.getQuantity();
            tableModel.addRow(new Object[]{
                    item.getProduct().getProductId(),
                    item.getProduct().getProductName(),
                    item.getProduct().getPrice(),
                    item.getQuantity(),
                    amount
            });
        }
        totalLabel.setText(String.format("Total: $%.2f  ", cart.getTotal()));
    }

    public static void main(String[] args) {
       
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            new BillingSystemGUI().setVisible(true);
        });
    }
}