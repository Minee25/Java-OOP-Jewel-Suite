import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
        JDialog aboutDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), "About Jewel Suite", true);
        aboutDialog.setSize(900, 550);
        aboutDialog.setLocationRelativeTo(parent);
        aboutDialog.setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        
        JLabel titleLabel = new JLabel("Jewel Suite", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 32));
        titleLabel.setForeground(Config.BLUE);
        titleLabel.setBorder(new EmptyBorder(0, 0, 30, 0));
        
        JLabel subtitleLabel = new JLabel("ระบบจำลองการกระจายตัวของแก๊ส", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        subtitleLabel.setForeground(Config.GRAY_TEXT);
        subtitleLabel.setBorder(new EmptyBorder(0, 0, 30, 0));
        
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.add(titleLabel);
        headerPanel.add(subtitleLabel);
        
        JPanel teamPanel = new JPanel(new GridLayout(1, 3, 30, 0));
        teamPanel.setBackground(Color.WHITE);
        
        teamPanel.add(createMemberPanel("src/res/team/member1.png", "นายสมชาย", "2055", "Project Manager"));
        teamPanel.add(createMemberPanel("src/res/team/member2.png", "นางสาวสมหญิง ", "2056", "Lead Developer"));
        teamPanel.add(createMemberPanel("src/res/team/member3.png", "นายสมศักดิ์ ", "2138", "UI/UX Designer"));
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        JButton closeButton = new JButton("ปิด");
        closeButton.setPreferredSize(new Dimension(120, 45));
        closeButton.setBackground(Config.BLUE);
        closeButton.setForeground(Color.WHITE);
        closeButton.setFont(Config.MID_FONT);
        closeButton.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> aboutDialog.dispose());
        buttonPanel.add(closeButton);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(teamPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        aboutDialog.add(mainPanel);
        aboutDialog.setVisible(true);
    }
    
    private static JPanel createMemberPanel(String imagePath, String name, String studentId, String position) {
        JPanel memberPanel = new JPanel();
        memberPanel.setLayout(new BoxLayout(memberPanel, BoxLayout.Y_AXIS));
        memberPanel.setBackground(new Color(248, 249, 250));
        memberPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            new EmptyBorder(25, 20, 25, 20)
        ));
        
        JLabel avatarLabel = createImageLabel(imagePath);
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        avatarLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        nameLabel.setForeground(Config.BLACK_TEXT);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel idLabel = new JLabel("รหัสนิสิต: " + studentId, SwingConstants.CENTER);
        idLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        idLabel.setForeground(Config.GRAY_TEXT);
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel positionLabel = new JLabel("ตำแหน่ง: " + position, SwingConstants.CENTER);
        positionLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        positionLabel.setForeground(Config.PURPLE);
        positionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        memberPanel.add(avatarLabel);
        memberPanel.add(nameLabel);
        memberPanel.add(Box.createVerticalStrut(8));
        memberPanel.add(idLabel);
        memberPanel.add(Box.createVerticalStrut(8));
        memberPanel.add(positionLabel);
        
        return memberPanel;
    }
    
    private static JLabel createImageLabel(String imagePath) {
        try {
            ImageIcon originalIcon = new ImageIcon(imagePath);
            Image scaledImage = originalIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            return new JLabel(scaledIcon, SwingConstants.CENTER);
        } catch (Exception e) {
            JLabel fallbackLabel = new JLabel("👤", SwingConstants.CENTER);
            fallbackLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
            return fallbackLabel;
        }
    }
}