public class Bill {

    private double subtotal;
    private double tax;
    private double total;

    //Bill calculation.
    public Bill(double subtotal) {
    this.subtotal = subtotal;
    this.tax = subtotal * 0.05;
    this.total = subtotal + tax;
}
    public double getSubtotal() {
        return subtotal;
    }

    public double getTax() {
        return tax;
    }

    public double getTotal() {

        return total;
    }

}