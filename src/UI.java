import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class UI {

    public static JPanel makeGlass() {
        return new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Config.LIGHT_BLUE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                g2.setColor(Config.WHITE_OVERLAY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                g2.setColor(new Color(255, 255, 255, 120));
                g2.fillRoundRect(2, 2, getWidth()-4, getHeight()/4, 23, 23);

                g2.setColor(Config.BLUE_BORDER);
                g2.setStroke(new BasicStroke(2.0f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 25, 25);

                g2.dispose();
            }
        };
    }

    public static JButton makeButton(String text, Color color) {
        JButton btn = new JButton(text) {
            private boolean hover = false;
            private boolean press = false;
            private float glow = 0.0f;
            private Timer glowTimer;

            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                float size = press ? 0.98f : 1.0f;
                int w = (int)(getWidth() * size);
                int h = (int)(getHeight() * size);
                int x = (getWidth() - w) / 2;
                int y = (getHeight() - h) / 2;


                if (hover && glow > 0) {
                    g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(40 * glow)));
                    g2.fillRoundRect(x-2, y-2, w+4, h+4, 20, 20);
                }

                GradientPaint bgGrad = new GradientPaint(
                        x, y, new Color(color.getRed(), color.getGreen(), color.getBlue(), hover ? 120 : 100),
                        x, y + h, new Color(color.getRed(), color.getGreen(), color.getBlue(), hover ? 80 : 60)
                );
                g2.setPaint(bgGrad);
                g2.fillRoundRect(x, y, w, h, 18, 18);


                g2.setColor(new Color(255, 255, 255, hover ? 100 : 80));
                g2.fillRoundRect(x + 2, y + 2, w - 4, h/3, 16, 16);


                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 180));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(x, y, w - 1, h - 1, 18, 18);

                super.paintComponent(g);
                g2.dispose();
            }

            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        hover = true;
                        startGlow();
                        setCursor(new Cursor(Cursor.HAND_CURSOR));
                        repaint();
                    }

                    public void mouseExited(MouseEvent e) {
                        hover = false;
                        if (glowTimer != null) glowTimer.stop();
                        glow = 0.0f;
                        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        repaint();
                    }

                    public void mousePressed(MouseEvent e) {
                        press = true;
                        repaint();
                    }

                    public void mouseReleased(MouseEvent e) {
                        press = false;
                        repaint();
                    }
                });
            }

            private void startGlow() {
                if (glowTimer != null) glowTimer.stop();

                glowTimer = new Timer(60, e -> {
                    glow += 0.1f;
                    if (glow >= 1.0f) {
                        glow = 1.0f;
                        glowTimer.stop();
                    }
                    repaint();
                });
                glowTimer.start();
            }
        };

        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setPreferredSize(new Dimension(120, 50));

        return btn;
    }

    public static JTextField makeText() {
        JTextField txt = new JTextField() {
            private boolean focus = false;
            private float pulse = 0.0f;
            private Timer pulseTimer;

            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


                Color bg1 = focus ? new Color(255, 255, 255, 220) : new Color(255, 255, 255, 170);
                Color bg2 = focus ? new Color(248, 249, 250, 200) : new Color(248, 249, 250, 150);

                GradientPaint bgGrad = new GradientPaint(0, 0, bg1, 0, getHeight(), bg2);
                g2.setPaint(bgGrad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                if (focus) {
                    float glowAlpha = 0.3f + (pulse * 0.2f);
                    g2.setColor(new Color(Config.BLUE.getRed(), Config.BLUE.getGreen(), Config.BLUE.getBlue(), (int)(glowAlpha * 255)));
                    g2.fillRoundRect(2, 2, getWidth()-4, getHeight()-4, 13, 13);
                }


                g2.setColor(new Color(255, 255, 255, focus ? 180 : 120));
                g2.fillRoundRect(3, 3, getWidth()-6, getHeight()/3, 12, 12);


                Color borderColor = focus ? Config.BLUE : Config.BLUE_BORDER;
                float borderWidth = focus ? 2.5f : 1.5f;
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(borderWidth));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 15, 15);


                if (!focus) {
                    g2.setColor(new Color(0, 0, 0, 20));
                    g2.setStroke(new BasicStroke(1.0f));
                    g2.drawRoundRect(2, 2, getWidth()-4, getHeight()-4, 13, 13);
                }

                super.paintComponent(g);
                g2.dispose();
            }

            private void startPulse() {
                if (pulseTimer != null) pulseTimer.stop();

                pulseTimer = new Timer(50, e -> {
                    pulse += 0.1f;
                    if (pulse >= Math.PI * 2) pulse = 0;
                    repaint();
                });
                pulseTimer.start();
            }

            private void stopPulse() {
                if (pulseTimer != null) {
                    pulseTimer.stop();
                    pulse = 0;
                }
            }
        };

        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setForeground(Config.BLACK_TEXT);
        txt.setCaretColor(Config.BLUE);
        txt.setOpaque(false);
        txt.setBorder(new EmptyBorder(12, 18, 12, 18));

        txt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                txt.repaint();
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                txt.repaint();
            }
        });

        return txt;
    }

    public static JLabel makeBigLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(Config.BIG_FONT);
        lbl.setForeground(Config.BLACK_TEXT);
        return lbl;
    }

    public static JLabel makeLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(Config.MID_FONT);
        lbl.setForeground(Config.GRAY_TEXT);
        return lbl;
    }

    public static JLabel makeSmallLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(Config.TINY_FONT);
        lbl.setForeground(Config.GRAY_TEXT);
        return lbl;
    }

    public static void showMessage(Component parent, String title, String msg, int type) {
        JOptionPane.showMessageDialog(parent, msg, title, type);
    }


    public static void showAbout(Component parent) {

    }
}