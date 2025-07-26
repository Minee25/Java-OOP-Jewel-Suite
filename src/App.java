import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class App extends JFrame {

    private Data data;
    private Grid grid;
    private JTextField fluidText;
    private JLabel volumeLabel;
    private JLabel statusLabel;
    private BufferedImage bg;
    private Timer breath;
    private float glow = 0.0f;
    private boolean up = true;
    private boolean isWindows;

    public App() {
        isWindows = System.getProperty("os.name").toLowerCase().contains("windows");
        setupLook();
        data = new Data();
        setupUI();
        makeBG();
        makeContent();
        updateAll();
        if (!isWindows) {
            startBreath();
        }
        setVisible(true);
    }

    private void setupLook() {
        try {
            if (isWindows) {
                System.setProperty("sun.java2d.noddraw", "true");
                System.setProperty("sun.java2d.d3d", "false");
                System.setProperty("sun.java2d.opengl", "false");
                System.setProperty("swing.volatileImageBufferEnabled", "false");
            }

            UIManager.setLookAndFeel(new FlatLightLaf());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupUI() {
        setTitle(Config.LANG_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(Config.WIN_W, Config.WIN_H);
        setLocationRelativeTo(null);
        setResizable(true);
        setMinimumSize(new Dimension(800, 600));

        if (isWindows) {
            setBackground(Config.BG_START);
        }
    }

    private void makeBG() {
        bg = new BufferedImage(Config.WIN_W, Config.WIN_H, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

        GradientPaint grad = new GradientPaint(0, 0, Config.BG_START, Config.WIN_W, Config.WIN_H, Config.BG_END);
        g2.setPaint(grad);
        g2.fillRect(0, 0, Config.WIN_W, Config.WIN_H);

        if (!isWindows) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.15f));

            RadialGradientPaint orb1 = new RadialGradientPaint(180, 120, 80, new float[]{0.0f, 1.0f},
                    new Color[]{Config.BLUE, new Color(0, 0, 0, 0)});
            g2.setPaint(orb1);
            g2.fillOval(100, 40, 160, 160);

            RadialGradientPaint orb2 = new RadialGradientPaint(Config.WIN_W - 120, 150, 100, new float[]{0.0f, 1.0f},
                    new Color[]{Config.SUCCESS, new Color(0, 0, 0, 0)});
            g2.setPaint(orb2);
            g2.fillOval(Config.WIN_W - 220, 50, 200, 200);

            RadialGradientPaint orb3 = new RadialGradientPaint(300, Config.WIN_H - 100, 90, new float[]{0.0f, 1.0f},
                    new Color[]{Config.PURPLE, new Color(0, 0, 0, 0)});
            g2.setPaint(orb3);
            g2.fillOval(210, Config.WIN_H - 190, 180, 180);
        }

        g2.dispose();
    }

    private void startBreath() {
        breath = new Timer(150, e -> {
            if (up) {
                glow += 0.015f;
                if (glow >= 1.0f) {
                    glow = 1.0f;
                    up = false;
                }
            } else {
                glow -= 0.015f;
                if (glow <= 0.0f) {
                    glow = 0.0f;
                    up = true;
                }
            }
            repaint();
        });
        breath.start();
    }

    private void makeContent() {
        JPanel main = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                if (bg != null) {
                    g2.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
                }

                if (!isWindows && glow > 0) {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, glow * 0.05f));
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                }

                g2.dispose();
            }
        };

        main.setLayout(new BorderLayout());
        main.setBorder(new EmptyBorder(25, 25, 25, 25));
        main.setOpaque(false);

        makeTop(main);
        makeMiddle(main);

        setContentPane(main);
    }

    private void makeTop(JPanel parent) {
        JPanel top = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                if (isWindows) {
                    g2.setColor(new Color(255, 255, 255, 200));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(new Color(100, 149, 237, 150));
                    g2.setStroke(new BasicStroke(1.0f));
                    g2.drawRect(0, 0, getWidth()-1, getHeight()-1);
                } else {
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
                }

                g2.dispose();
            }
        };

        top.setLayout(new BorderLayout());
        top.setBorder(new EmptyBorder(20, 25, 20, 25));
        top.setOpaque(false);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
        left.setOpaque(false);

        JLabel icon = createIconLabel();
        JLabel title = UI.makeBigLabel(Config.LANG_TITLE);
        title.setForeground(Config.BLUE);

        left.add(icon);
        left.add(Box.createHorizontalStrut(10));
        left.add(title);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setOpaque(false);

        JButton about = UI.makeButton(Config.LANG_ABOUT, Config.ORANGE);
        about.setPreferredSize(new Dimension(100, 45));
        about.addActionListener(e -> UI.showAbout(this));

        right.add(about);

        top.add(left, BorderLayout.WEST);
        top.add(right, BorderLayout.EAST);

        parent.add(top, BorderLayout.NORTH);
    }

    private void makeMiddle(JPanel parent) {
        JPanel middle = new JPanel(new BorderLayout(25, 25));
        middle.setOpaque(false);
        middle.setBorder(new EmptyBorder(25, 0, 25, 0));

        makeLeft(middle);
        makeRight(middle);

        parent.add(middle, BorderLayout.CENTER);
    }

    private void makeLeft(JPanel parent) {
        JPanel left = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                if (isWindows) {
                    g2.setColor(new Color(255, 255, 255, 200));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(new Color(100, 149, 237, 150));
                    g2.setStroke(new BasicStroke(1.0f));
                    g2.drawRect(0, 0, getWidth()-1, getHeight()-1);
                } else {
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
                }

                g2.dispose();
            }
        };

        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBorder(new EmptyBorder(25, 25, 25, 25));
        left.setPreferredSize(new Dimension(320, 0));
        left.setOpaque(false);

        JLabel ctrlTitle = UI.makeBigLabel(Config.LANG_CONTROL);
        ctrlTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        ctrlTitle.setForeground(Config.BLUE);

        JPanel input = makeInputPanel();
        JPanel legend = makeLegendPanel();
        JPanel results = makeResultPanel();

        left.add(ctrlTitle);
        left.add(Box.createVerticalStrut(25));
        left.add(input);
        left.add(Box.createVerticalStrut(25));
        left.add(legend);
        left.add(Box.createVerticalStrut(25));
        left.add(results);
        left.add(Box.createVerticalGlue());

        parent.add(left, BorderLayout.WEST);
    }

    private JPanel makeInputPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel label = UI.makeLabel("ความลึกระดับของเหลว (เมตร):");
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        fluidText = UI.makeText();
        fluidText.setText(String.valueOf(data.getFluid()));
        fluidText.setMaximumSize(new Dimension(280, 45));
        fluidText.setPreferredSize(new Dimension(280, 45));
        fluidText.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton calc = UI.makeButton("คำนวณ", Config.BLUE);
        calc.setMaximumSize(new Dimension(280, 50));
        calc.setPreferredSize(new Dimension(280, 50));
        calc.setAlignmentX(Component.LEFT_ALIGNMENT);
        calc.addActionListener(e -> doCalc());

        JButton load = UI.makeButton("โหลดไฟล์", Config.SUCCESS);
        load.setMaximumSize(new Dimension(280, 50));
        load.setPreferredSize(new Dimension(280, 50));
        load.setAlignmentX(Component.LEFT_ALIGNMENT);
        load.addActionListener(e -> openFile());

        panel.add(label);
        panel.add(Box.createVerticalStrut(10));
        panel.add(fluidText);
        panel.add(Box.createVerticalStrut(20));
        panel.add(calc);
        panel.add(Box.createVerticalStrut(15));
        panel.add(load);

        return panel;
    }

    private JPanel makeLegendPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel title = UI.makeLabel("คำอธิบาย:");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel red = makeColorBox("ไม่มีแก๊ส (0%)", Config.RED);
        JPanel yellow = makeColorBox("แก๊สน้อย (<50%)", Config.YELLOW);
        JPanel green = makeColorBox("แก๊สมาก (≥50%)", Config.GREEN);

        panel.add(title);
        panel.add(Box.createVerticalStrut(12));
        panel.add(red);
        panel.add(Box.createVerticalStrut(8));
        panel.add(yellow);
        panel.add(Box.createVerticalStrut(8));
        panel.add(green);

        return panel;
    }

    private JPanel makeColorBox(String text, Color color) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(300, 30));

        JPanel box = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(color);
                if (isWindows) {
                    g2.fillRect(0, 0, getWidth(), getHeight());
                } else {
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.setColor(new Color(255, 255, 255, 100));
                    g2.fillRoundRect(2, 2, getWidth()-4, getHeight()/3, 6, 6);
                }
                g2.dispose();
            }
        };
        box.setPreferredSize(new Dimension(24, 24));
        box.setOpaque(false);

        JLabel label = UI.makeLabel(text);

        panel.add(box);
        panel.add(Box.createHorizontalStrut(12));
        panel.add(label);

        return panel;
    }

    private JPanel makeResultPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel title = UI.makeLabel("ผลลัพธ์:");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        volumeLabel = UI.makeLabel("ปริมาตรแก๊สทั้งหมด: 0 ลบ.ม.");
        volumeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        volumeLabel.setForeground(Config.BLACK_TEXT);

        statusLabel = UI.makeSmallLabel("พร้อม");
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statusLabel.setForeground(Config.SUCCESS);

        panel.add(title);
        panel.add(Box.createVerticalStrut(12));
        panel.add(volumeLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(statusLabel);

        return panel;
    }

    private void makeRight(JPanel parent) {
        JPanel right = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                if (isWindows) {
                    g2.setColor(new Color(255, 255, 255, 200));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(new Color(100, 149, 237, 150));
                    g2.setStroke(new BasicStroke(1.0f));
                    g2.drawRect(0, 0, getWidth()-1, getHeight()-1);
                } else {
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
                }

                g2.dispose();
            }
        };

        right.setLayout(new BorderLayout());
        right.setBorder(new EmptyBorder(25, 25, 25, 25));
        right.setOpaque(false);

        JLabel title = UI.makeBigLabel("แผนที่การกระจายแก๊ส");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(Config.PURPLE);

        grid = new Grid(data);
        grid.setFileDropCallback(file -> {
            if (data.loadFromFile(file.getAbsolutePath())) {
                updateAll();
                statusLabel.setText("โหลดไฟล์: " + file.getName());
                statusLabel.setForeground(Config.SUCCESS);
                UI.showMessage(this, "สำเร็จ", "โหลดไฟล์เสร็จสิ้น!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                statusLabel.setText("โหลดไฟล์ไม่สำเร็จ");
                statusLabel.setForeground(Config.DANGER);
                UI.showMessage(this, "ข้อผิดพลาด", "ไม่สามารถโหลดไฟล์ได้ กรุณาตรวจสอบรูปแบบไฟล์", JOptionPane.ERROR_MESSAGE);
            }
        });

        grid.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (!hasGridData()) {
                    openFile();
                }
            }
        });

        JPanel gridContainer = new JPanel(new BorderLayout());
        gridContainer.setOpaque(false);
        gridContainer.add(grid, BorderLayout.CENTER);

        JLabel info = UI.makeSmallLabel("คลิกเพื่อเลือกไฟล์ หรือลากวางไฟล์ dept.txt");
        info.setHorizontalAlignment(SwingConstants.CENTER);

        right.add(title, BorderLayout.NORTH);
        right.add(gridContainer, BorderLayout.CENTER);
        right.add(info, BorderLayout.SOUTH);

        parent.add(right, BorderLayout.CENTER);
    }

    private JLabel createIconLabel() {
        try {
            ImageIcon originalIcon = new ImageIcon(Config.APP_ICON_PATH);
            Image scaledImage = originalIcon.getImage().getScaledInstance(Config.ICON_SIZE_SMALL, Config.ICON_SIZE_SMALL, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            return new JLabel(scaledIcon);
        } catch (Exception e) {
            JLabel fallbackIcon = new JLabel("💎");
            fallbackIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, Config.ICON_SIZE_SMALL));
            return fallbackIcon;
        }
    }

    private boolean hasGridData() {
        return data.getRows() > 0 && data.getCols() > 0;
    }

    private void openFile() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("เลือกไฟล์ dept.txt");
        fc.setFileFilter(new FileNameExtensionFilter("ไฟล์ข้อความ (*.txt)", "txt"));
        fc.setCurrentDirectory(new File("."));

        int result = fc.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            if (data.loadFromFile(file.getAbsolutePath())) {
                updateAll();
                statusLabel.setText("โหลดไฟล์: " + file.getName());
                statusLabel.setForeground(Config.SUCCESS);
                UI.showMessage(this, "สำเร็จ", "โหลดไฟล์เสร็จสิ้น!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                statusLabel.setText("โหลดไฟล์ไม่สำเร็จ");
                statusLabel.setForeground(Config.DANGER);
                UI.showMessage(this, "ข้อผิดพลาด", "ไม่สามารถโหลดไฟล์ได้ กรุณาตรวจสอบรูปแบบไฟล์", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void doCalc() {
        try {
            double fluid = Double.parseDouble(fluidText.getText());
            data.setFluid(fluid);
            updateAll();
            statusLabel.setText("คำนวณเสร็จสิ้น");
            statusLabel.setForeground(Config.SUCCESS);
        } catch (NumberFormatException e) {
            UI.showMessage(this, "ข้อผิดพลาดการป้อนข้อมูล", "กรุณาป้อนตัวเลขที่ถูกต้อง", JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("ข้อมูลไม่ถูกต้อง");
            statusLabel.setForeground(Config.DANGER);
        }
    }

    private void updateAll() {
        double total = data.getTotalVolume();
        volumeLabel.setText(String.format("ปริมาตรแก๊สทั้งหมด: %.0f ลบ.ม.", total));
        grid.update();
    }

    @Override
    public void dispose() {
        if (breath != null) {
            breath.stop();
        }
        super.dispose();
    }

    public static void main(String[] args) {
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            System.setProperty("sun.java2d.noddraw", "true");
            System.setProperty("sun.java2d.d3d", "false");
            System.setProperty("sun.java2d.opengl", "false");
        }

        SwingUtilities.invokeLater(() -> {
            try {
                new App();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}