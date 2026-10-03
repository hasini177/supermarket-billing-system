import java.util.InputMismatchException;
import java.util.Scanner;

public class BillingSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Cart cart = new Cart();
        ProductDAO productDAO = new ProductDAO();
        BillingDAO billingDAO = new BillingDAO();

        while (true) {

            System.out.print("Scan Product ID / Barcode: ");
           
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("checkout")) {
                break;
            }

            // remove <ID> OR remove <ID> <Quantity>
            if (input.toLowerCase().startsWith("remove")) {

                String[] parts = input.split("\\s+");

                if (parts.length < 2) {
                    System.out.println("Use format: remove <ProductID> [quantity]");
                    continue;
                }

                try {
                    int productId = Integer.parseInt(parts[1]);
                    int qtyToRemove = Integer.MAX_VALUE; // Default to removing all [ if you dont specify the qunatity]

                    if (parts.length >= 3) {
                        qtyToRemove = Integer.parseInt(parts[2]);
                    }

                    boolean removed = cart.removeQuantity(productId, qtyToRemove);

                    if (removed) {
                        System.out.println("Cart updated successfully.");
                    } else {
                        System.out.println("Item not found in cart.");
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Invalid Product ID or Quantity. Use format: remove <ProductID> [quantity]");
                }

                continue;
            }

            int productId;

            try {
                productId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid Product ID.");
                continue;
            }

            Product selectedProduct =
                    productDAO.getProductById(productId);

            if (selectedProduct == null) {
                System.out.println("Product not found.");
                continue;
            }

            int quantity;

            while (true) {

                try {
                    System.out.print("Enter Quantity: ");
                    quantity = sc.nextInt();
                    sc.nextLine();

                    if (quantity <= 0) {
                        System.out.println(
                                "Quantity must be greater than 0."
                        );
                        continue;
                    }

                    int alreadyInCart =
                            cart.getQuantity(productId);

                    if (quantity + alreadyInCart
                            > selectedProduct.getStockQuantity()) {

                        System.out.println(
                                "Out of Stock! Available stock: "
                                + (selectedProduct.getStockQuantity()
                                - alreadyInCart)
                        );

                        continue;
                    }

                    break;

                } catch (InputMismatchException e) {
                    System.out.println(
                            "Please enter numbers only."
                    );
                    sc.nextLine();
                }
            }

            CartItem item =
                    new CartItem(selectedProduct, quantity);

            cart.addItem(item);

        }

        if (cart.isEmpty()) {
            System.out.println("No items purchased.");
            sc.close();
            return;
        }

        /*
         * Final stock validation before checkout
         */
        for (CartItem item : cart.getItems()) {

            Product product =
                    productDAO.getProductById(
                            item.getProduct().getProductId()
                    );

            if (product == null ||
                    product.getStockQuantity()
                    < item.getQuantity()) {

                System.out.println(
                        "Checkout failed. Some items are out of stock."
                );

                sc.close();
                return;
            }
        }

        /*
         * Calculate bill
         */
        Bill bill = new Bill(cart.getTotal());

        /*
         * Create bill in database
         */
        int billId = billingDAO.createBill(
                bill.getSubtotal(),
                bill.getTax(),
                bill.getTotal()
        );

        if (billId == -1) {
            System.out.println("Failed to create bill.");
            sc.close();
            return;
        }

        /*
         * Save items and reduce stock
         */
        for (CartItem item : cart.getItems()) {

            Product product = item.getProduct();

            double amount =
                    product.getPrice() * item.getQuantity();

            boolean itemSaved =
                    billingDAO.addBillItem(
                            billId,
                            product.getProductId(),
                            item.getQuantity(),
                            product.getPrice(),
                            amount
                    );

            boolean stockUpdated =
                    productDAO.reduceStock(
                            product.getProductId(),
                            item.getQuantity()
                    );

            if (!itemSaved || !stockUpdated) {
                System.out.println(
                        "Error while processing checkout."
                );

                sc.close();
                return;
            }
        }

        /*
         * Display final bill
         */
        System.out.println();
        System.out.println("========== BILL ==========");

        System.out.printf(
                "%-15s %-5s %s%n",
                "Product",
                "Qty",
                "Amount"
        );

        for (CartItem item : cart.getItems()) {

            double amount =
                    item.getProduct().getPrice()
                    * item.getQuantity();

            System.out.printf(
                    "%-15s %-5d %.2f%n",
                    item.getProduct().getProductName(),
                    item.getQuantity(),
                    amount
            );
        }

        System.out.println("--------------------------");

        System.out.printf(
                "Bill ID  : %d%n",
                billId
        );

        System.out.printf(
                "Subtotal : %.2f%n",
                bill.getSubtotal()
        );

        System.out.printf(
                "Tax      : %.2f%n",
                bill.getTax()
        );

        System.out.printf(
                "Total    : %.2f%n",
                bill.getTotal()
        );

        System.out.println("==========================");
        System.out.println("Thank you for shopping!");

        sc.close();
    }
}