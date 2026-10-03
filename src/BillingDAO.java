import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BillingDAO {

    public int createBill(double subtotal, double tax, double total) {

        String sql = "INSERT INTO bills (subtotal, tax, total) " +
                     "VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql,
                     PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setDouble(1, subtotal);
            ps.setDouble(2, tax);
            ps.setDouble(3, total);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }

        return -1;
    }

    public boolean addBillItem(
            int billId,
            int productId,
            int quantity,
            double price,
            double amount) {

        String sql = "INSERT INTO bill_items " +
                     "(bill_id, product_id, quantity, price, amount) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, billId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.setDouble(4, price);
            ps.setDouble(5, amount);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }

        return false;
    }
}