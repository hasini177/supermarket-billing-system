public class Inventory {

    //To check whether the product have enough quantity or not
    public boolean checkStock(CartItem item) {
        Product product = item.getProduct();
        return product.getStockQuantity() >= item.getQuantity();
    }
    //Reducing the stock of the product whenever customer purchases it
    public void reduceStock(CartItem item) {
        Product product = item.getProduct();

        int quantity = item.getQuantity();
        int stock = product.getStockQuantity();

        int remaining = stock - quantity;

        product.setStockQuantity(remaining);
    }

   //Processing the cart if the product has enough stock
    public boolean processCart(Cart cart) {

        for(CartItem item : cart.getItems()) {
            if(!checkStock(item)) {
                return false;
            }
        }

        for(CartItem item : cart.getItems()) {
            reduceStock(item);
        }

        return true;
    }
}