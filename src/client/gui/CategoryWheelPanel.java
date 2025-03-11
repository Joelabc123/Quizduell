package client.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CategoryWheelPanel extends JPanel {

    private MainGameFrame mainFrame;
    // Drei Demo-Kategorien – später dynamisch änderbar
    private String[] categories = {"Sport", "Geschichte", "Musik"};

    public CategoryWheelPanel(MainGameFrame mainFrame) {
        this.mainFrame = mainFrame;
        setOpaque(false); // Damit der benutzerdefinierte Hintergrund sichtbar wird
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header-Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel headerLabel = new JLabel("Kategorie auswählen", SwingConstants.CENTER);
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Füge das Rad in die Mitte ein
        WheelPanel wheelPanel = new WheelPanel();
        add(wheelPanel, BorderLayout.CENTER);
    }

    public void setCategories(String[] categories) {
        // Annahme: Es werden genau drei Kategorien übergeben.
        System.arraycopy(categories, 0, this.categories, 0, 3);
        repaint();
    }

    // Das interne Panel, das das Rad zeichnet
    private class WheelPanel extends JPanel {
        // Verwenden Sie lebendige, moderne Farben für die Segmente:
        private Color[] sliceColors = {
                new Color(76, 175, 80),      // Grün
                new Color(244, 67, 54),      // Rot
                new Color(255, 193, 7)       // Gelb
        };

        public WheelPanel() {
            setOpaque(false);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    handleClick(e.getX(), e.getY());
                }
            });
        }

        private void handleClick(int x, int y) {
            int cx = getWidth() / 2;
            int cy = getHeight() / 2;
            double dx = x - cx;
            double dy = y - cy;
            double distance = Math.sqrt(dx * dx + dy * dy);

            int radius = Math.min(getWidth(), getHeight()) / 2 - 20;
            if (distance > radius) return;

            double angle = Math.toDegrees(Math.atan2(dy, dx));
            if (angle < 0) angle += 360;

            int segment = (int)(angle / 120);
            String chosenCategory = categories[segment];

            JOptionPane.showMessageDialog(this,
                    "Kategorie '" + chosenCategory + "' wurde gewählt!",
                    "Auswahl",
                    JOptionPane.INFORMATION_MESSAGE);
            mainFrame.getClientHandler().gameManager.selectCategory(chosenCategory);
            System.out.println("WheelPanel: Kategorie '" + chosenCategory + "' gewählt – externe Logik soll switchQuestionPanel() aufrufen.");
        }

        @Override
        protected void paintComponent(Graphics g) {
            // Zeichne zuerst den Hintergrund des Rads transparent (der übergeordnete Hintergrund zeigt den Farbverlauf)
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = Math.min(getWidth(), getHeight()) - 40;
            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;

            // Zeichne die drei Segmente
            int startAngle = 0;
            for (int i = 0; i < 3; i++) {
                g2d.setColor(sliceColors[i]);
                g2d.fillArc(x, y, size, size, startAngle, 120);
                // Optional: Zeichne einen dünnen Rand für einen 3D-Effekt
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawArc(x, y, size, size, startAngle, 120);
                startAngle += 120;
            }

            // Zeichne die Kategorienamen in moderner Schrift
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
            for (int i = 0; i < 3; i++) {
                double theta = Math.toRadians(i * 120 + 60);
                double r = size * 0.3;
                double cx = getWidth() / 2 + r * Math.cos(theta);
                double cy = getHeight() / 2 + r * Math.sin(theta);
                String cat = categories[i];
                FontMetrics fm = g2d.getFontMetrics();
                int tw = fm.stringWidth(cat);
                int th = fm.getHeight();
                // Optional: Drop-Shadow-Effekt für den Text
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.drawString(cat, (int)(cx - tw/2 + 2), (int)(cy + th/4 + 2));
                g2d.setColor(Color.WHITE);
                g2d.drawString(cat, (int)(cx - tw/2), (int)(cy + th/4));
            }
            g2d.dispose();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Hintergrund des Panels: Farbverlauf von hellblau oben zu dunkelblau unten
        Graphics2D g2d = (Graphics2D) g.create();
        int width = getWidth();
        int height = getHeight();
        GradientPaint gp = new GradientPaint(0, 0, new Color(0,150,199,255), 0, height, new Color(144,224,239));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();
        super.paintComponent(g);
    }
}
