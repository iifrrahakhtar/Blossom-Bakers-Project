import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;

public class BakeryGUI extends JFrame {
    private final String url = "jdbc:mysql://localhost:3306/homebakery?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private final String user = "root";
    private final String password = "8zRe2x@@@";

    private final Color coffeeBrown = new Color(93, 64, 55);
    private final Color softPink = new Color(255, 209, 220);
    private final Color deepPink = new Color(255, 143, 171);
    private final Color sidebarColor = new Color(255, 240, 245);

    private JPanel cardPanel;
    private CardLayout cardLayout;

    public BakeryGUI() {
        // Try to load the MySQL Driver explicitly to prevent "No suitable driver found" errors
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "MySQL JDBC Driver missing! Please add it to your project classpath.", "Driver Error", JOptionPane.ERROR_MESSAGE);
        }

        setTitle("Blossom Bakers - All-in-One Management System");
        setSize(1400, 850);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(280, 850));
        sidebar.setBackground(sidebarColor);

        JLabel logo = new JLabel("🧁 Blossom Bakers", JLabel.CENTER);
        logo.setFont(new Font("Serif", Font.BOLD, 26));
        logo.setForeground(coffeeBrown);
        logo.setBorder(new EmptyBorder(40, 10, 40, 10));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(logo);

        sidebar.add(createNavButton("📜 QUICK MENU", "menu"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebar.add(createNavButton("📦 Products", "products"));
        sidebar.add(createNavButton("👥 Customers", "customers"));
        sidebar.add(createNavButton("🛒 Orders", "orders"));
        sidebar.add(createNavButton("📝 Order Details", "order_details"));
        sidebar.add(createNavButton("💳 Payments", "payments"));
        sidebar.add(createNavButton("🚚 Delivery", "delivery"));

        add(sidebar, BorderLayout.WEST);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Management Panels
        cardPanel.add(createDataPanel("products", new String[]{"product_id", "product_name", "category", "price"}), "menu");
        cardPanel.add(createDataPanel("products", new String[]{"product_id", "product_name", "category", "price"}), "products");
        cardPanel.add(createDataPanel("customers", new String[]{"customer_id", "name", "phone", "address"}), "customers");
        cardPanel.add(createDataPanel("orders", new String[]{"order_id", "customer_id", "order_date", "total_amount"}), "orders");
        cardPanel.add(createDataPanel("order_details", new String[]{"detail_id", "order_id", "product_id", "quantity"}), "order_details");
        cardPanel.add(createDataPanel("payments", new String[]{"payment_id", "order_id", "payment_method", "payment_status"}), "payments");
        cardPanel.add(createDataPanel("delivery", new String[]{"delivery_id", "order_id", "delivery_date", "delivery_status"}), "delivery");

        add(cardPanel, BorderLayout.CENTER);
    }

    private JButton createNavButton(String text, String target) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(280, 50));
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBackground(sidebarColor);
        btn.setForeground(coffeeBrown);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.addActionListener(e -> cardLayout.show(cardPanel, target));
        return btn;
    }

    private JPanel createDataPanel(String tableName, String[] columns) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel lbl = new JLabel("Blossom Bakers - " + tableName.toUpperCase());
        lbl.setFont(new Font("Serif", Font.BOLD, 28));
        lbl.setForeground(coffeeBrown);
        p.add(lbl, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setRowHeight(35);
        table.setSelectionBackground(softPink);
        JTableHeader header = table.getTableHeader();
        header.setBackground(coffeeBrown);
        header.setForeground(Color.WHITE);

        p.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        btnPanel.setBackground(Color.WHITE);

        JButton addBtn = createStyledButton("➕ Add", deepPink);
        JButton updBtn = createStyledButton("✏️ Update", coffeeBrown);
        JButton delBtn = createStyledButton("🗑️ Delete", new Color(200, 50, 50));
        JButton refBtn = createStyledButton("🔄 Refresh", new Color(100, 160, 100));

        btnPanel.add(addBtn); btnPanel.add(updBtn); btnPanel.add(delBtn); btnPanel.add(refBtn);
        p.add(btnPanel, BorderLayout.SOUTH);

        refBtn.addActionListener(e -> fetchData(tableName, model));
        delBtn.addActionListener(e -> deleteRecord(tableName, table, model));
        addBtn.addActionListener(e -> showAddDialog(tableName, columns, model));
        updBtn.addActionListener(e -> showUpdateDialog(tableName, table, columns, model));

        if (tableName.equals("products")) {
            insertInitialProducts();
        }

        fetchData(tableName, model);
        return p;
    }

    private void insertInitialProducts() {
        String[][] initialData = {
                {"1", "Chocolate Fudge Cake", "Cakes", "2500"}, {"2", "Red Velvet Cake", "Cakes", "2800"},
                {"3", "Lotus Cheesecake", "Cheesecake", "3500"}, {"4", "Blueberry Muffin", "Pastry", "350"},
                {"5", "Classic Brownie", "Brownies", "250"}, {"6", "Vanilla Cupcake", "Cupcakes", "200"},
                {"7", "Pineapple Pastry", "Pastry", "150"}, {"8", "Almond Biscotti", "Cookies", "800"},
                {"9", "Double Choc Cookie", "Cookies", "150"}, {"10", "Carrot Cake", "Cakes", "2200"},
                {"11", "Coffee Eclair", "Pastry", "400"}, {"12", "Lemon Tart", "Tarts", "300"},
                {"13", "Nutella Croissant", "Pastry", "450"}, {"14", "Strawberry Gateau", "Cakes", "2600"},
                {"15", "Milk Chocolate Bar", "Snacks", "120"}
        };
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            for (String[] row : initialData) {
                String sql = "INSERT IGNORE INTO products VALUES (?,?,?,?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, row[0]); ps.setString(2, row[1]); ps.setString(3, row[2]); ps.setString(4, row[3]);
                    ps.executeUpdate();
                }
            }
        } catch (Exception e) {
            System.out.println("Init skip or failure: " + e.getMessage());
        }
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(120, 40));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setFocusPainted(false);
        return btn;
    }

    private void fetchData(String table, DefaultTableModel model) {
        model.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM " + table)) {
            int cols = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Object[] row = new Object[cols];
                for (int i = 1; i <= cols; i++) row[i-1] = rs.getObject(i);
                model.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching data from table '" + table + "':\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRecord(String table, JTable jt, DefaultTableModel m) {
        int r = jt.getSelectedRow();
        if (r == -1) { JOptionPane.showMessageDialog(this, "Please select a row to delete!"); return; }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement("DELETE FROM " + table + " WHERE " + m.getColumnName(0) + " = ?")) {
            ps.setObject(1, m.getValueAt(r, 0));
            ps.executeUpdate();
            fetchData(table, m);
            JOptionPane.showMessageDialog(this, "Deleted successfully!");
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error deleting: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE); }
    }

    private void showAddDialog(String table, String[] cols, DefaultTableModel m) {
        JPanel p = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField[] fields = new JTextField[cols.length];
        for (int i = 0; i < cols.length; i++) {
            p.add(new JLabel(cols[i] + ":"));
            fields[i] = new JTextField();
            p.add(fields[i]);
        }

        if (JOptionPane.showConfirmDialog(this, p, "Add New Entry", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                StringBuilder sql = new StringBuilder("INSERT INTO " + table + " (");
                for (int i = 0; i < cols.length; i++) sql.append(cols[i]).append(i == cols.length - 1 ? "" : ",");
                sql.append(") VALUES (").append("?,".repeat(cols.length - 1)).append("?)");

                PreparedStatement ps = conn.prepareStatement(sql.toString());
                for (int i = 0; i < fields.length; i++) ps.setString(i + 1, fields[i].getText());
                ps.executeUpdate();
                fetchData(table, m);
                JOptionPane.showMessageDialog(this, "Added successfully!");
            } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error adding: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE); }
        }
    }

    private void showUpdateDialog(String table, JTable jt, String[] cols, DefaultTableModel m) {
        int row = jt.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Please select a row to update!"); return; }

        JPanel p = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField[] fields = new JTextField[cols.length];
        for (int i = 0; i < cols.length; i++) {
            p.add(new JLabel(cols[i] + ":"));
            fields[i] = new JTextField(m.getValueAt(row, i).toString());
            if (i == 0) fields[i].setEditable(false);
            p.add(fields[i]);
        }

        if (JOptionPane.showConfirmDialog(this, p, "Update Entry", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                StringBuilder sql = new StringBuilder("UPDATE " + table + " SET ");
                for (int i = 1; i < cols.length; i++) sql.append(cols[i]).append(" = ? ").append(i == cols.length - 1 ? "" : ", ");
                sql.append(" WHERE ").append(cols[0]).append(" = ?");

                PreparedStatement ps = conn.prepareStatement(sql.toString());
                for (int i = 1; i < fields.length; i++) ps.setString(i, fields[i].getText());
                ps.setObject(cols.length, m.getValueAt(row, 0));

                ps.executeUpdate();
                fetchData(table, m);
                JOptionPane.showMessageDialog(this, "Updated successfully!");
            } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error updating: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE); }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BakeryGUI().setVisible(true));
    }
}