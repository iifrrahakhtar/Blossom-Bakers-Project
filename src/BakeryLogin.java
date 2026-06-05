import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class BakeryLogin extends JFrame {
    private final Color softPink = new Color(255, 209, 220);
    private final Color deepPink = new Color(255, 143, 171);
    private final Color coffeeBrown = new Color(93, 64, 55);

    public BakeryLogin() {
        setTitle("Blossom Bakers - Login");
        setSize(1400, 850); // Slightly taller window
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Background setup
        BackgroundPanel mainPanel = new BackgroundPanel("/image/bakery.png");
        mainPanel.setLayout(new GridBagLayout()); // This centers the login box automatically
        setContentPane(mainPanel);

        mainPanel.add(createLoginBox());
    }

    private JPanel createLoginBox() {
        JPanel box = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 50, 50)); // Smoother corners
                g2.dispose();
            }
        };
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(new Color(255, 255, 255, 200)); // Slightly more solid for readability
        box.setOpaque(false);

        // --- INCREASED HEIGHT & BETTER PADDING ---
        box.setPreferredSize(new Dimension(500, 780));
        box.setBorder(new EmptyBorder(30, 40, 40, 40));


        box.add(Box.createRigidArea(new Dimension(0, 15)));

        // 2. BRAND NAME
        JLabel titleLabel = new JLabel("Blossom Bakers");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 45));
        titleLabel.setForeground(coffeeBrown);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.add(titleLabel);

        JLabel subtitle = new JLabel("HOME BAKERY MANAGEMENT PORTAL");
        subtitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        subtitle.setForeground(deepPink);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.add(subtitle);

        box.add(Box.createRigidArea(new Dimension(0, 50))); // Space before fields

        // 3. INPUT FIELDS
        JTextField userField = new JTextField();
        userField.setMaximumSize(new Dimension(380, 55));
        userField.setFont(new Font("SansSerif", Font.PLAIN, 12));
        userField.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(coffeeBrown, 1), "Username"));
        box.add(userField);

        box.add(Box.createRigidArea(new Dimension(0, 25))); // Space between fields

        JPasswordField passField = new JPasswordField();
        passField.setMaximumSize(new Dimension(380, 55));
        passField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        passField.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(coffeeBrown, 1), "Password"));
        box.add(passField);

        box.add(Box.createRigidArea(new Dimension(0, 60))); // Space before button

        // 4. LOGIN BUTTON
        JButton loginBtn = new JButton("LOGIN");
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(380, 65));
        loginBtn.setBackground(deepPink);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginBtn.addActionListener(e -> {
            if (userField.getText().equals("Ifrah") && new String(passField.getPassword()).equals("1234")) {
                this.dispose();
                new BakeryGUI().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password!", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        box.add(loginBtn);
        return box;
    }

    class BackgroundPanel extends JPanel {
        private Image img;
        public BackgroundPanel(String path) {
            try {
                InputStream is = BakeryLogin.class.getResourceAsStream(path);
                if (is != null) img = ImageIO.read(is);
            } catch (Exception e) { e.printStackTrace(); }
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (img != null) g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            else { g.setColor(softPink); g.fillRect(0, 0, getWidth(), getHeight()); }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BakeryLogin().setVisible(true));
    }
}