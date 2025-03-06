package client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CategoryWheelPanel extends JPanel {

    private MainGameFrame mainFrame;
    // Drei Demo-Kategorien – später dynamisch änderbar
    private String[] categories = {"Sport", "Geschichte", "Musik"};

    public CategoryWheelPanel(MainGameFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30,144,255));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel headerLabel = new JLabel("Kategorie auswählen", SwingConstants.CENTER);
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        WheelPanel wheelPanel = new WheelPanel();
        add(wheelPanel, BorderLayout.CENTER);
    }

    public void setCategories(String c1, String c2, String c3) {
        categories[0] = c1;
        categories[1] = c2;
        categories[2] = c3;
        repaint();
    }

    private class WheelPanel extends JPanel {
        private Color[] sliceColors = {
                new Color(76,175,80),
                new Color(244,67,54),
                new Color(255,193,7)
        };

        public WheelPanel() {
            setBackground(Color.WHITE);
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

            System.out.println("WheelPanel: Button gedrückt, externe Logik soll switchQuestionPanel() aufrufen.");
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = Math.min(getWidth(), getHeight()) - 40;
            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;

            int startAngle = 0;
            for (int i = 0; i < 3; i++) {
                g2d.setColor(sliceColors[i]);
                g2d.fillArc(x, y, size, size, startAngle, 120);
                startAngle += 120;
            }

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            for (int i = 0; i < 3; i++) {
                double theta = Math.toRadians(i * 120 + 60);
                double r = size * 0.3;
                double cx = getWidth() / 2 + r * Math.cos(theta);
                double cy = getHeight() / 2 + r * Math.sin(theta);
                String cat = categories[i];
                FontMetrics fm = g2d.getFontMetrics();
                int tw = fm.stringWidth(cat);
                int th = fm.getHeight();
                g2d.drawString(cat, (int)(cx - tw / 2), (int)(cy + th / 4));
            }
            g2d.dispose();
        }
    }
}
