public class Product {
    private int productId;
    private String productName;
    private double price;
    private int stockQuantity;

    public Product(int productId, String productName,double price,int stockQuantity){
        this.productId=productId;
        this.productName=productName;
        this.price=price;
        this.stockQuantity=stockQuantity;
    }

    public int getProductId(){
        return productId;
    }

    public String getProductName(){
        return productName;
    }

    public double getPrice(){
        return price;
    }
    
    public int getStockQuantity(){
        return stockQuantity;
    }
    
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}
