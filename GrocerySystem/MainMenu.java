package GrocerySystem;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

class DatabaseConnection {
    private static final String URL      = "jdbc:mysql://localhost:3306/grocerydb";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Connection Failed: " + e.getMessage());
            return null;
        }
    }
}

class Product {
    private int    productId;
    private String name;
    private double price;
    private int    stock;
    private String expiryDate;
    private int    supplierId;

    public Product(int productId, String name, double price,
                   int stock, String expiryDate, int supplierId) {
        this.productId  = productId;
        this.name       = name;
        this.price      = price;
        this.stock      = stock;
        this.expiryDate = expiryDate;
        this.supplierId = supplierId;
    }

    public int    getProductId()  { return productId;  }
    public String getName()       { return name;        }
    public double getPrice()      { return price;       }
    public int    getStock()      { return stock;       }
    public String getExpiryDate() { return expiryDate;  }
    public int    getSupplierId() { return supplierId;  }

    public void setName(String name)             { this.name       = name;       }
    public void setPrice(double price)           { this.price      = price;      }
    public void setStock(int stock)              { this.stock      = stock;      }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
    public void setSupplierId(int supplierId)    { this.supplierId = supplierId; }

    @Override
    public String toString() {
        return "Product{id=" + productId + ", name=" + name +
               ", price=" + price + ", stock=" + stock + "}";
    }
}

class Supplier {
    private int    supplierId;
    private String name;
    private String contact;

    public Supplier(int supplierId, String name, String contact) {
        this.supplierId = supplierId;
        this.name       = name;
        this.contact    = contact;
    }

    public int    getSupplierId() { return supplierId; }
    public String getName()       { return name;       }
    public String getContact()    { return contact;    }

    public void setName(String name)       { this.name    = name;    }
    public void setContact(String contact) { this.contact = contact; }
}

class Customer {
    private int    customerId;
    private String name;
    private String phone;

    public Customer(int customerId, String name, String phone) {
        this.customerId = customerId;
        this.name       = name;
        this.phone      = phone;
    }

    public int    getCustomerId() { return customerId; }
    public String getName()       { return name;       }
    public String getPhone()      { return phone;      }

    public void setName(String name)   { this.name  = name;  }
    public void setPhone(String phone) { this.phone = phone; }
}

class Employee {
    private int    employeeId;
    private String name;
    private String role;

    public Employee(int employeeId, String name, String role) {
        this.employeeId = employeeId;
        this.name       = name;
        this.role       = role;
    }

    public int    getEmployeeId() { return employeeId; }
    public String getName()       { return name;       }
    public String getRole()       { return role;       }

    public void setName(String name) { this.name = name; }
    public void setRole(String role) { this.role = role; }
}

class Sale {
    private int    saleId;
    private int    customerId;
    private int    employeeId;
    private String saleDate;

    public Sale(int saleId, int customerId, int employeeId, String saleDate) {
        this.saleId     = saleId;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.saleDate   = saleDate;
    }

    public int    getSaleId()     { return saleId;     }
    public int    getCustomerId() { return customerId; }
    public int    getEmployeeId() { return employeeId; }
    public String getSaleDate()   { return saleDate;   }
}

class SaleItem {
    private int    saleItemId;
    private int    saleId;
    private int    productId;
    private int    quantity;
    private double price;

    public SaleItem(int saleItemId, int saleId, int productId,
                    int quantity, double price) {
        this.saleItemId = saleItemId;
        this.saleId     = saleId;
        this.productId  = productId;
        this.quantity   = quantity;
        this.price      = price;
    }

    public int    getSaleItemId() { return saleItemId; }
    public int    getSaleId()     { return saleId;     }
    public int    getProductId()  { return productId;  }
    public int    getQuantity()   { return quantity;   }
    public double getPrice()      { return price;      }
}

class SupplyOrder {
    private int    orderId;
    private int    supplierId;
    private int    productId;
    private int    quantity;
    private String orderDate;

    public SupplyOrder(int orderId, int supplierId, int productId,
                       int quantity, String orderDate) {
        this.orderId    = orderId;
        this.supplierId = supplierId;
        this.productId  = productId;
        this.quantity   = quantity;
        this.orderDate  = orderDate;
    }

    public int    getOrderId()    { return orderId;    }
    public int    getSupplierId() { return supplierId; }
    public int    getProductId()  { return productId;  }
    public int    getQuantity()   { return quantity;   }
    public String getOrderDate()  { return orderDate;  }
}

interface CrudOperations {
    void loadData();
    void clearForm();
}

abstract class BasePanel extends JFrame implements CrudOperations {

    protected JTextField addField(JPanel panel, String label) {
        panel.add(new JLabel(label));
        JTextField tf = new JTextField();
        panel.add(tf);
        return tf;
    }

    protected boolean validateNotEmpty(JTextField... fields) {
        for (JTextField f : fields) {
            if (f.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Please fill in all fields.", "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }

    protected boolean validateInt(JTextField field, String fieldName) {
        try {
            Integer.parseInt(field.getText().trim());
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                fieldName + " must be a whole number.", "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    protected boolean validateDouble(JTextField field, String fieldName) {
        try {
            Double.parseDouble(field.getText().trim());
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                fieldName + " must be a number (e.g. 12.50).", "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    protected JButton styledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        return btn;
    }
}

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Grocery Store Management System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Grocery Store Management System", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setOpaque(true);
        header.setBackground(new Color(30, 100, 180));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(header, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new GridLayout(4, 2, 15, 15));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        btnPanel.setBackground(new Color(240, 245, 255));

        String[] labels = {"Products","Suppliers","Supply Orders","Customers",
                           "Sales","Sale Items","Employees","Reports"};
        Color[] colors  = {
            new Color(52,152,219), new Color(46,204,113),
            new Color(155,89,182), new Color(241,196,15),
            new Color(231,76,60),  new Color(26,188,156),
            new Color(243,156,18), new Color(52,73,94)
        };

        for (int i = 0; i < labels.length; i++) {
            JButton btn = new JButton(labels[i]);
            btn.setBackground(colors[i]);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
            final String label = labels[i];
            btn.addActionListener(e -> openPanel(label));
            btnPanel.add(btn);
        }

        add(btnPanel, BorderLayout.CENTER);

        JLabel footer = new JLabel("Faryal Channa & Mehak Sajjad", SwingConstants.CENTER);
        footer.setFont(new Font("Arial", Font.ITALIC, 12));
        footer.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(footer, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void openPanel(String name) {
        switch (name) {
            case "Products"      -> new ProductsPanel();
            case "Suppliers"     -> new SuppliersPanel();
            case "Supply Orders" -> new SupplyOrdersPanel();
            case "Customers"     -> new CustomersPanel();
            case "Sales"         -> new SalesPanel();
            case "Sale Items"    -> new SaleItemsPanel();
            case "Employees"     -> new EmployeesPanel();
            case "Reports"       -> new ReportsPanel();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainMenu::new);
    }
}

class ProductsPanel extends BasePanel {

    private JTable            table;
    private DefaultTableModel model;
    private JTextField tfProductId, tfName, tfPrice, tfStock, tfExpiry, tfSupplierId;

    public ProductsPanel() {
        setTitle("Products Management");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] cols = {"Product ID","Name","Price","Stock","Expiry Date","Supplier ID"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        table.getSelectionModel().addListSelectionListener(e -> fillForm());
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createTitledBorder("Product Details"));
        formPanel.setPreferredSize(new Dimension(300, 260));

        tfProductId  = addField(formPanel, "Product ID (update/delete):");
        tfName       = addField(formPanel, "Name:");
        tfPrice      = addField(formPanel, "Price:");
        tfStock      = addField(formPanel, "Stock:");
        tfExpiry     = addField(formPanel, "Expiry Date (YYYY-MM-DD):");
        tfSupplierId = addField(formPanel, "Supplier ID:");

        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton btnAdd    = styledButton("Add",    new Color(46,204,113));
        JButton btnUpdate = styledButton("Update", new Color(52,152,219));
        JButton btnDelete = styledButton("Delete", new Color(231,76,60));
        JButton btnClear  = styledButton("Clear",  Color.GRAY);

        btnAdd.addActionListener(e    -> addProduct());
        btnUpdate.addActionListener(e -> updateProduct());
        btnDelete.addActionListener(e -> deleteProduct());
        btnClear.addActionListener(e  -> clearForm());

        btnPanel.add(btnAdd); btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete); btnPanel.add(btnClear);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(formPanel, BorderLayout.CENTER);
        rightPanel.add(btnPanel,  BorderLayout.SOUTH);
        add(rightPanel, BorderLayout.EAST);

        loadData();
        setVisible(true);
    }

    @Override
    public void loadData() {
        model.setRowCount(0);
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM products ORDER BY product_id")) {
            while (rs.next()) {
                Product p = new Product(
                    rs.getInt("product_id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("stock"),
                    rs.getString("expiry_date"),
                    rs.getInt("supplier_id")
                );
                model.addRow(new Object[]{
                    p.getProductId(), p.getName(), p.getPrice(),
                    p.getStock(), p.getExpiryDate(), p.getSupplierId()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void fillForm() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        tfProductId.setText(model.getValueAt(row, 0).toString());
        tfName.setText(model.getValueAt(row, 1).toString());
        tfPrice.setText(model.getValueAt(row, 2).toString());
        tfStock.setText(model.getValueAt(row, 3).toString());
        tfExpiry.setText(model.getValueAt(row, 4).toString());
        tfSupplierId.setText(model.getValueAt(row, 5).toString());
    }

    private void addProduct() {
        if (!validateNotEmpty(tfName, tfPrice, tfStock, tfExpiry, tfSupplierId)) return;
        if (!validateDouble(tfPrice,   "Price"))       return;
        if (!validateInt(tfStock,      "Stock"))       return;
        if (!validateInt(tfSupplierId, "Supplier ID")) return;

        String sql = "INSERT INTO products (name, price, stock, expiry_date, supplier_id) VALUES (?,?,?,?,?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tfName.getText().trim());
            ps.setDouble(2, Double.parseDouble(tfPrice.getText().trim()));
            ps.setInt(3,    Integer.parseInt(tfStock.getText().trim()));
            ps.setString(4, tfExpiry.getText().trim());
            ps.setInt(5,    Integer.parseInt(tfSupplierId.getText().trim()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Product added!");
            loadData(); clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void updateProduct() {
        if (!validateNotEmpty(tfProductId, tfName, tfPrice, tfStock, tfExpiry, tfSupplierId)) return;
        if (!validateInt(tfProductId,  "Product ID"))  return;
        if (!validateDouble(tfPrice,   "Price"))       return;
        if (!validateInt(tfStock,      "Stock"))       return;
        if (!validateInt(tfSupplierId, "Supplier ID")) return;

        String sql = "UPDATE products SET name=?, price=?, stock=?, expiry_date=?, supplier_id=? WHERE product_id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tfName.getText().trim());
            ps.setDouble(2, Double.parseDouble(tfPrice.getText().trim()));
            ps.setInt(3,    Integer.parseInt(tfStock.getText().trim()));
            ps.setString(4, tfExpiry.getText().trim());
            ps.setInt(5,    Integer.parseInt(tfSupplierId.getText().trim()));
            ps.setInt(6,    Integer.parseInt(tfProductId.getText().trim()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Product updated!");
            loadData(); clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void deleteProduct() {
        if (!validateNotEmpty(tfProductId)) return;
        if (!validateInt(tfProductId, "Product ID")) return;
        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete this product?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "DELETE FROM products WHERE product_id=?")) {
            ps.setInt(1, Integer.parseInt(tfProductId.getText().trim()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Product deleted!");
            loadData(); clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    @Override
    public void clearForm() {
        tfProductId.setText(""); tfName.setText(""); tfPrice.setText("");
        tfStock.setText(""); tfExpiry.setText(""); tfSupplierId.setText("");
        table.clearSelection();
    }
}

class SuppliersPanel extends BasePanel {

    private JTable            table;
    private DefaultTableModel model;
    private JTextField tfId, tfName, tfContact;

    public SuppliersPanel() {
        setTitle("Suppliers Management");
        setSize(650, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] cols = {"ID","Name","Contact"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        table.getSelectionModel().addListSelectionListener(e -> fillForm());
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Supplier Details"));
        form.setPreferredSize(new Dimension(260, 200));

        tfId      = addField(form, "Supplier ID (update/delete):");
        tfName    = addField(form, "Name:");
        tfContact = addField(form, "Contact:");

        JButton btnAdd    = styledButton("Add",    new Color(46,204,113));
        JButton btnUpdate = styledButton("Update", new Color(52,152,219));
        JButton btnDelete = styledButton("Delete", new Color(231,76,60));
        JButton btnClear  = styledButton("Clear",  Color.GRAY);

        btnAdd.addActionListener(e    -> addSupplier());
        btnUpdate.addActionListener(e -> updateSupplier());
        btnDelete.addActionListener(e -> deleteSupplier());
        btnClear.addActionListener(e  -> clearForm());

        form.add(btnAdd); form.add(btnUpdate);
        form.add(btnDelete); form.add(btnClear);

        add(form, BorderLayout.EAST);
        loadData();
        setVisible(true);
    }

    private void fillForm() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        tfId.setText(model.getValueAt(row, 0).toString());
        tfName.setText(model.getValueAt(row, 1).toString());
        tfContact.setText(model.getValueAt(row, 2).toString());
    }

    @Override
    public void loadData() {
        model.setRowCount(0);
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM suppliers")) {
            while (rs.next()) {
                Supplier s = new Supplier(
                    rs.getInt("supplier_id"),
                    rs.getString("name"),
                    rs.getString("contact")
                );
                model.addRow(new Object[]{
                    s.getSupplierId(), s.getName(), s.getContact()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void addSupplier() {
        if (!validateNotEmpty(tfName, tfContact)) return;
        String sql = "INSERT INTO suppliers(name, contact) VALUES(?,?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tfName.getText().trim());
            ps.setString(2, tfContact.getText().trim());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Supplier Added");
            loadData(); clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void updateSupplier() {
        if (!validateNotEmpty(tfId, tfName, tfContact)) return;
        if (!validateInt(tfId, "Supplier ID")) return;
        String sql = "UPDATE suppliers SET name=?, contact=? WHERE supplier_id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tfName.getText().trim());
            ps.setString(2, tfContact.getText().trim());
            ps.setInt(3, Integer.parseInt(tfId.getText().trim()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Supplier Updated");
            loadData(); clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void deleteSupplier() {
        if (!validateNotEmpty(tfId)) return;
        if (!validateInt(tfId, "Supplier ID")) return;
        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete this supplier?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "DELETE FROM suppliers WHERE supplier_id=?")) {
            ps.setInt(1, Integer.parseInt(tfId.getText().trim()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Supplier Deleted");
            loadData(); clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    @Override
    public void clearForm() {
        tfId.setText(""); tfName.setText(""); tfContact.setText("");
        table.clearSelection();
    }
}

class CustomersPanel extends BasePanel {

    private JTable            table;
    private DefaultTableModel model;
    private JTextField tfId, tfName, tfPhone;

    public CustomersPanel() {
        setTitle("Customers");
        setSize(650, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] cols = {"ID","Name","Phone"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        table.getSelectionModel().addListSelectionListener(e -> fillForm());
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Customer Details"));
        form.setPreferredSize(new Dimension(260, 200));

        tfId    = addField(form, "Customer ID (update/delete):");
        tfName  = addField(form, "Name:");
        tfPhone = addField(form, "Phone:");

        JButton btnAdd    = styledButton("Add",    new Color(46,204,113));
        JButton btnUpdate = styledButton("Update", new Color(52,152,219));
        JButton btnDelete = styledButton("Delete", new Color(231,76,60));
        JButton btnClear  = styledButton("Clear",  Color.GRAY);

        btnAdd.addActionListener(e    -> addCustomer());
        btnUpdate.addActionListener(e -> updateCustomer());
        btnDelete.addActionListener(e -> deleteCustomer());
        btnClear.addActionListener(e  -> clearForm());

        form.add(btnAdd); form.add(btnUpdate);
        form.add(btnDelete); form.add(btnClear);

        add(form, BorderLayout.EAST);
        loadData();
        setVisible(true);
    }

    private void fillForm() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        tfId.setText(model.getValueAt(row, 0).toString());
        tfName.setText(model.getValueAt(row, 1).toString());
        tfPhone.setText(model.getValueAt(row, 2).toString());
    }

    @Override
    public void loadData() {
        model.setRowCount(0);
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM customers")) {
            while (rs.next()) {
                Customer c = new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("name"),
                    rs.getString("phone")
                );
                model.addRow(new Object[]{
                    c.getCustomerId(), c.getName(), c.getPhone()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void addCustomer() {
        if (!validateNotEmpty(tfName, tfPhone)) return;
        String sql = "INSERT INTO customers(name, phone) VALUES(?,?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tfName.getText().trim());
            ps.setString(2, tfPhone.getText().trim());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Customer Added");
            loadData(); clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void updateCustomer() {
        if (!validateNotEmpty(tfId, tfName, tfPhone)) return;
        if (!validateInt(tfId, "Customer ID")) return;
        String sql = "UPDATE customers SET name=?, phone=? WHERE customer_id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tfName.getText().trim());
            ps.setString(2, tfPhone.getText().trim());
            ps.setInt(3, Integer.parseInt(tfId.getText().trim()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Customer Updated");
            loadData(); clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void deleteCustomer() {
        if (!validateNotEmpty(tfId)) return;
        if (!validateInt(tfId, "Customer ID")) return;
        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete this customer?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "DELETE FROM customers WHERE customer_id=?")) {
            ps.setInt(1, Integer.parseInt(tfId.getText().trim()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Customer Deleted");
            loadData(); clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    @Override
    public void clearForm() {
        tfId.setText(""); tfName.setText(""); tfPhone.setText("");
        table.clearSelection();
    }
}

class EmployeesPanel extends BasePanel {

    private JTable            table;
    private DefaultTableModel model;
    private JTextField tfId, tfName, tfRole;

    public EmployeesPanel() {
        setTitle("Employees Management");
        setSize(650, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] cols = {"ID","Name","Role"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        table.getSelectionModel().addListSelectionListener(e -> fillForm());
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Employee Details"));
        form.setPreferredSize(new Dimension(260, 200));

        tfId   = addField(form, "Employee ID (update/delete):");
        tfName = addField(form, "Name:");
        tfRole = addField(form, "Role:");

        JButton btnAdd    = styledButton("Add",    new Color(46,204,113));
        JButton btnUpdate = styledButton("Update", new Color(52,152,219));
        JButton btnDelete = styledButton("Delete", new Color(231,76,60));
        JButton btnClear  = styledButton("Clear",  Color.GRAY);

        btnAdd.addActionListener(e    -> addEmployee());
        btnUpdate.addActionListener(e -> updateEmployee());
        btnDelete.addActionListener(e -> deleteEmployee());
        btnClear.addActionListener(e  -> clearForm());

        form.add(btnAdd); form.add(btnUpdate);
        form.add(btnDelete); form.add(btnClear);

        add(form, BorderLayout.EAST);
        loadData();
        setVisible(true);
    }

    private void fillForm() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        tfId.setText(model.getValueAt(row, 0).toString());
        tfName.setText(model.getValueAt(row, 1).toString());
        tfRole.setText(model.getValueAt(row, 2).toString());
    }

    @Override
    public void loadData() {
        model.setRowCount(0);
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM employees")) {
            while (rs.next()) {
                Employee emp = new Employee(
                    rs.getInt("employee_id"),
                    rs.getString("name"),
                    rs.getString("role")
                );
                model.addRow(new Object[]{
                    emp.getEmployeeId(), emp.getName(), emp.getRole()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void addEmployee() {
        if (!validateNotEmpty(tfName, tfRole)) return;
        String sql = "INSERT INTO employees(name, role) VALUES(?,?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tfName.getText().trim());
            ps.setString(2, tfRole.getText().trim());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Employee Added");
            loadData(); clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void updateEmployee() {
        if (!validateNotEmpty(tfId, tfName, tfRole)) return;
        if (!validateInt(tfId, "Employee ID")) return;
        String sql = "UPDATE employees SET name=?, role=? WHERE employee_id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tfName.getText().trim());
            ps.setString(2, tfRole.getText().trim());
            ps.setInt(3, Integer.parseInt(tfId.getText().trim()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Employee Updated");
            loadData(); clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void deleteEmployee() {
        if (!validateNotEmpty(tfId)) return;
        if (!validateInt(tfId, "Employee ID")) return;
        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete this employee?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "DELETE FROM employees WHERE employee_id=?")) {
            ps.setInt(1, Integer.parseInt(tfId.getText().trim()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Employee Deleted");
            loadData(); clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    @Override
    public void clearForm() {
        tfId.setText(""); tfName.setText(""); tfRole.setText("");
        table.clearSelection();
    }
}

class SupplyOrdersPanel extends BasePanel {

    private JTable            table;
    private DefaultTableModel model;
    private JTextField tfOrderId, tfSupplierId, tfProductId, tfQuantity, tfDate;

    public SupplyOrdersPanel() {
        setTitle("Supply Orders");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] cols = {"Order ID","Supplier ID","Product ID","Quantity","Order Date"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        table.getSelectionModel().addListSelectionListener(e -> fillForm());
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(7, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Order Details"));
        form.setPreferredSize(new Dimension(280, 260));

        tfOrderId    = addField(form, "Order ID (update/delete):");
        tfSupplierId = addField(form, "Supplier ID:");
        tfProductId  = addField(form, "Product ID:");
        tfQuantity   = addField(form, "Quantity:");
        tfDate       = addField(form, "Order Date (YYYY-MM-DD):");

        JButton btnAdd    = styledButton("Add",    new Color(46,204,113));
        JButton btnUpdate = styledButton("Update", new Color(52,152,219));
        JButton btnDelete = styledButton("Delete", new Color(231,76,60));
        JButton btnClear  = styledButton("Clear",  Color.GRAY);

        btnAdd.addActionListener(e    -> addOrder());
        btnUpdate.addActionListener(e -> updateOrder());
        btnDelete.addActionListener(e -> deleteOrder());
        btnClear.addActionListener(e  -> clearForm());

        form.add(btnAdd); form.add(btnUpdate);
        form.add(btnDelete); form.add(btnClear);

        add(form, BorderLayout.EAST);
        loadData();
        setVisible(true);
    }

    private void fillForm() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        tfOrderId.setText(model.getValueAt(row, 0).toString());
        tfSupplierId.setText(model.getValueAt(row, 1).toString());
        tfProductId.setText(model.getValueAt(row, 2).toString());
        tfQuantity.setText(model.getValueAt(row, 3).toString());
        tfDate.setText(model.getValueAt(row, 4).toString());
    }

    @Override
    public void loadData() {
        model.setRowCount(0);
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM supply_orders")) {
            while (rs.next()) {
                SupplyOrder so = new SupplyOrder(
                    rs.getInt("order_id"),
                    rs.getInt("supplier_id"),
                    rs.getInt("product_id"),
                    rs.getInt("quantity"),
                    rs.getString("order_date")
                );
                model.addRow(new Object[]{
                    so.getOrderId(), so.getSupplierId(), so.getProductId(),
                    so.getQuantity(), so.getOrderDate()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void addOrder() {
        if (!validateNotEmpty(tfSupplierId, tfProductId, tfQuantity, tfDate)) return;
        if (!validateInt(tfSupplierId, "Supplier ID")) return;
        if (!validateInt(tfProductId,  "Product ID"))  return;
        if (!validateInt(tfQuantity,   "Quantity"))    return;

        String sql = "INSERT INTO supply_orders(supplier_id, product_id, quantity, order_date) VALUES(?,?,?,?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(tfSupplierId.getText().trim()));
            ps.setInt(2, Integer.parseInt(tfProductId.getText().trim()));
            ps.setInt(3, Integer.parseInt(tfQuantity.getText().trim()));
            ps.setString(4, tfDate.getText().trim());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Order Added");
            loadData(); clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void updateOrder() {
        if (!validateNotEmpty(tfOrderId, tfSupplierId, tfProductId, tfQuantity, tfDate)) return;
        if (!validateInt(tfOrderId,    "Order ID"))    return;
        if (!validateInt(tfSupplierId, "Supplier ID")) return;
        if (!validateInt(tfProductId,  "Product ID"))  return;
        if (!validateInt(tfQuantity,   "Quantity"))    return;

        String sql = "UPDATE supply_orders SET supplier_id=?, product_id=?, quantity=?, order_date=? WHERE order_id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(tfSupplierId.getText().trim()));
            ps.setInt(2, Integer.parseInt(tfProductId.getText().trim()));
            ps.setInt(3, Integer.parseInt(tfQuantity.getText().trim()));
            ps.setString(4, tfDate.getText().trim());
            ps.setInt(5, Integer.parseInt(tfOrderId.getText().trim()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Order Updated");
            loadData(); clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void deleteOrder() {
        if (!validateNotEmpty(tfOrderId)) return;
        if (!validateInt(tfOrderId, "Order ID")) return;
        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete this order?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "DELETE FROM supply_orders WHERE order_id=?")) {
            ps.setInt(1, Integer.parseInt(tfOrderId.getText().trim()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Order Deleted");
            loadData(); clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    @Override
    public void clearForm() {
        tfOrderId.setText(""); tfSupplierId.setText(""); tfProductId.setText("");
        tfQuantity.setText(""); tfDate.setText("");
        table.clearSelection();
    }
}

class SalesPanel extends BasePanel {

    private JTable            table;
    private DefaultTableModel model;
    private JTextField tfSaleId, tfCustomerId, tfEmployeeId, tfDate;

    public SalesPanel() {
        setTitle("Sales");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] cols = {"Sale ID","Customer ID","Employee ID","Sale Date"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        table.getSelectionModel().addListSelectionListener(e -> fillForm());
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(6, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Sale Details"));
        form.setPreferredSize(new Dimension(280, 220));

        tfSaleId     = addField(form, "Sale ID (delete):");
        tfCustomerId = addField(form, "Customer ID:");
        tfEmployeeId = addField(form, "Employee ID:");
        tfDate       = addField(form, "Sale Date (YYYY-MM-DD HH:MM:SS):");

        JButton btnAdd    = styledButton("Add",    new Color(46,204,113));
        JButton btnDelete = styledButton("Delete", new Color(231,76,60));
        JButton btnClear  = styledButton("Clear",  Color.GRAY);

        btnAdd.addActionListener(e    -> addSale());
        btnDelete.addActionListener(e -> deleteSale());
        btnClear.addActionListener(e  -> clearForm());

        form.add(btnAdd); form.add(btnDelete); form.add(btnClear);

        add(form, BorderLayout.EAST);
        loadData();
        setVisible(true);
    }

    private void fillForm() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        tfSaleId.setText(model.getValueAt(row, 0).toString());
        tfCustomerId.setText(model.getValueAt(row, 1).toString());
        tfEmployeeId.setText(model.getValueAt(row, 2).toString());
        tfDate.setText(model.getValueAt(row, 3).toString());
    }

    @Override
    public void loadData() {
        model.setRowCount(0);
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM sales")) {
            while (rs.next()) {
                Sale s = new Sale(
                    rs.getInt("sale_id"),
                    rs.getInt("customer_id"),
                    rs.getInt("employee_id"),
                    rs.getString("sale_date")
                );
                model.addRow(new Object[]{
                    s.getSaleId(), s.getCustomerId(),
                    s.getEmployeeId(), s.getSaleDate()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void addSale() {
        if (!validateNotEmpty(tfCustomerId, tfEmployeeId, tfDate)) return;
        if (!validateInt(tfCustomerId, "Customer ID")) return;
        if (!validateInt(tfEmployeeId, "Employee ID")) return;

        String sql = "INSERT INTO sales(customer_id, employee_id, sale_date) VALUES(?,?,?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(tfCustomerId.getText().trim()));
            ps.setInt(2, Integer.parseInt(tfEmployeeId.getText().trim()));
            ps.setString(3, tfDate.getText().trim());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Sale Added");
            loadData(); clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void deleteSale() {
        if (!validateNotEmpty(tfSaleId)) return;
        if (!validateInt(tfSaleId, "Sale ID")) return;
        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete this sale?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "DELETE FROM sales WHERE sale_id=?")) {
            ps.setInt(1, Integer.parseInt(tfSaleId.getText().trim()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Sale Deleted");
            loadData(); clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    @Override
    public void clearForm() {
        tfSaleId.setText(""); tfCustomerId.setText("");
        tfEmployeeId.setText(""); tfDate.setText("");
        table.clearSelection();
    }
}

class SaleItemsPanel extends BasePanel {

    private JTable            table;
    private DefaultTableModel model;
    private JTextField tfItemId, tfSaleId, tfProductId, tfQuantity, tfPrice;

    public SaleItemsPanel() {
        setTitle("Sale Items");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] cols = {"Item ID","Sale ID","Product ID","Quantity","Price"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        table.getSelectionModel().addListSelectionListener(e -> fillForm());
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(7, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Sale Item Details"));
        form.setPreferredSize(new Dimension(280, 260));

        tfItemId    = addField(form, "Item ID (delete):");
        tfSaleId    = addField(form, "Sale ID:");
        tfProductId = addField(form, "Product ID:");
        tfQuantity  = addField(form, "Quantity:");
        tfPrice     = addField(form, "Price:");

        JButton btnAdd    = styledButton("Add",    new Color(46,204,113));
        JButton btnDelete = styledButton("Delete", new Color(231,76,60));
        JButton btnClear  = styledButton("Clear",  Color.GRAY);

        btnAdd.addActionListener(e    -> addSaleItem());
        btnDelete.addActionListener(e -> deleteSaleItem());
        btnClear.addActionListener(e  -> clearForm());

        form.add(btnAdd); form.add(btnDelete); form.add(btnClear);

        add(form, BorderLayout.EAST);
        loadData();
        setVisible(true);
    }

    private void fillForm() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        tfItemId.setText(model.getValueAt(row, 0).toString());
        tfSaleId.setText(model.getValueAt(row, 1).toString());
        tfProductId.setText(model.getValueAt(row, 2).toString());
        tfQuantity.setText(model.getValueAt(row, 3).toString());
        tfPrice.setText(model.getValueAt(row, 4).toString());
    }

    @Override
    public void loadData() {
        model.setRowCount(0);
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM sale_items")) {
            while (rs.next()) {
                SaleItem si = new SaleItem(
                    rs.getInt("sale_item_id"),
                    rs.getInt("sale_id"),
                    rs.getInt("product_id"),
                    rs.getInt("quantity"),
                    rs.getDouble("price")
                );
                model.addRow(new Object[]{
                    si.getSaleItemId(), si.getSaleId(), si.getProductId(),
                    si.getQuantity(), si.getPrice()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void addSaleItem() {
        if (!validateNotEmpty(tfSaleId, tfProductId, tfQuantity, tfPrice)) return;
        if (!validateInt(tfSaleId,    "Sale ID"))    return;
        if (!validateInt(tfProductId, "Product ID")) return;
        if (!validateInt(tfQuantity,  "Quantity"))   return;
        if (!validateDouble(tfPrice,  "Price"))      return;

        String sql = "INSERT INTO sale_items(sale_id, product_id, quantity, price) VALUES(?,?,?,?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(tfSaleId.getText().trim()));
            ps.setInt(2, Integer.parseInt(tfProductId.getText().trim()));
            ps.setInt(3, Integer.parseInt(tfQuantity.getText().trim()));
            ps.setDouble(4, Double.parseDouble(tfPrice.getText().trim()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Sale Item Added");
            loadData(); clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void deleteSaleItem() {
        if (!validateNotEmpty(tfItemId)) return;
        if (!validateInt(tfItemId, "Item ID")) return;
        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete this sale item?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "DELETE FROM sale_items WHERE sale_item_id=?")) {
            ps.setInt(1, Integer.parseInt(tfItemId.getText().trim()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Sale Item Deleted");
            loadData(); clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    @Override
    public void clearForm() {
        tfItemId.setText(""); tfSaleId.setText(""); tfProductId.setText("");
        tfQuantity.setText(""); tfPrice.setText("");
        table.clearSelection();
    }
}

class ReportsPanel extends JFrame {

    private JTextArea area;

    public ReportsPanel() {
        setTitle("Reports");
        setSize(700, 500);
        setLocationRelativeTo(null);

        area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JButton btnSales   = new JButton("Total Sales Count");
        JButton btnStock   = new JButton("Low Stock (< 10)");
        JButton btnRevenue = new JButton("Revenue per Product");

        btnSales.addActionListener(e   -> totalSales());
        btnStock.addActionListener(e   -> lowStock());
        btnRevenue.addActionListener(e -> revenuePerProduct());

        JPanel top = new JPanel(new FlowLayout());
        top.add(btnSales);
        top.add(btnStock);
        top.add(btnRevenue);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(area), BorderLayout.CENTER);

        setVisible(true);
    }

    private void totalSales() {
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM sales")) {
            if (rs.next())
                area.setText("Total Number of Sales = " + rs.getInt("total"));
        } catch (Exception e) {
            area.setText(e.getMessage());
        }
    }

    private void lowStock() {
        area.setText("--- Low Stock Products (stock < 10) ---\n");
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "SELECT * FROM products WHERE stock < 10 ORDER BY stock")) {
            boolean found = false;
            while (rs.next()) {
                area.append(rs.getString("name") +
                    " | Stock = " + rs.getInt("stock") + "\n");
                found = true;
            }
            if (!found) area.append("All products have sufficient stock.\n");
        } catch (Exception e) {
            area.setText(e.getMessage());
        }
    }

    private void revenuePerProduct() {
        area.setText("--- Revenue per Product ---\n");
        String sql = "SELECT p.name, SUM(si.quantity * si.price) AS revenue " +
                     "FROM sale_items si JOIN products p ON si.product_id = p.product_id " +
                     "GROUP BY p.name ORDER BY revenue DESC";
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                area.append(rs.getString("name") +
                    " | Revenue = " + rs.getDouble("revenue") + "\n");
            }
        } catch (Exception e) {
            area.setText(e.getMessage());
        }
    }
}
