public class CartItem {
    private  Product product;
    private int qunatity;

    public CartItem(Product product, int qunatity){
        this.product=product;
        this.qunatity=qunatity;
    }
    public void setQuantity(int qunatity){
        this.qunatity=qunatity;
    }

    public Product getProduct(){
        return product;
    }

    public int getQuantity(){
        return qunatity;
    }
}
