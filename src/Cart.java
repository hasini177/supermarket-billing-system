import java.util.ArrayList;

public class Cart {
    private ArrayList<CartItem> items;

    public Cart() {
        items = new ArrayList<>();
    }

    public void addItem(CartItem newItem) {
        for (CartItem item : items) {
            if (item.getProduct().getProductId()
                    == newItem.getProduct().getProductId()) {

                item.setQuantity(
                        item.getQuantity() + newItem.getQuantity()
                );
                return;
            }
        }

        items.add(newItem);
    }

    // Method to handle partial or full quantity removal
    public boolean removeQuantity(int productId, int quantityToRemove) {
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            
            if (item.getProduct().getProductId() == productId) {
                int currentQty = item.getQuantity();
                
                if (currentQty <= quantityToRemove) {
                    items.remove(i);
                } else {
                    item.setQuantity(currentQty - quantityToRemove);
                }
                return true;
            }
        }
        return false;
    }

    
    public boolean removeItem(int productId) {
        return removeQuantity(productId, Integer.MAX_VALUE);
    }

    public int getQuantity(int productId) {
        for (CartItem item : items) {
            if (item.getProduct().getProductId() == productId) {
                return item.getQuantity();
            }
        }

        return 0;
    }

    public ArrayList<CartItem> getItems() {
        return items;
    }

    public double getTotal() {
        double total = 0;

        for (CartItem item : items) {
            total += item.getProduct().getPrice()
                    * item.getQuantity();
        }

        return total;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}