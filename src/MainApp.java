import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class MainApp extends JFrame {

    private DataFile data;
    private GridUI grid;
    private JTextField waterInput;
    private JLabel totalLabel;
    private JLabel statusLabel;

    public MainApp() {
        setupLook();
        data = new DataFile();
        setupWindow();
        makeContent();
        updateDisplay();
        setVisible(true);
    }

    private void setupLook() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupWindow() {
        setTitle(Settings.APP_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(Settings.WIN_W, Settings.WIN_H);
        setLocationRelativeTo(null);
        setResizable(true);
        setUndecorated(Settings.WINDOW_MENU);
        getContentPane().setBackground(Colors.BG_MAIN);
    }

    private void makeContent() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBorder(new EmptyBorder(25, 25, 25, 25));
        main.setOpaque(false);

        makeTopBar(main);
        makeMainArea(main);

        setContentPane(main);
    }

    private void makeTopBar(JPanel parent) {
        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(new EmptyBorder(20, 25, 20, 25));
        top.setBackground(Colors.BG_PANEL);
        top.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Colors.BORDER_LIGHT, 1),
                new EmptyBorder(20, 25, 20, 25)
        ));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
        left.setOpaque(false);

        JLabel icon = makeIcon();
        JLabel title = new JLabel(Settings.APP_TITLE);
        title.setFont(Settings.BIG_FONT);
        title.setForeground(Colors.BLUE);

        left.add(icon);
        left.add(Box.createHorizontalStrut(10));
        left.add(title);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setOpaque(false);

        JButton about = ButtonHelper.createButton(Settings.BTN_ABOUT, Colors.ORANGE, 100, 45);
        about.addActionListener(e -> Display.showAbout(this));

        JButton exit = ButtonHelper.createButton(Settings.BTN_EXIT, Colors.DANGER_RED, 100, 45);
        exit.addActionListener(e -> exitApp());

        right.add(about);
        right.add(exit);

        top.add(left, BorderLayout.WEST);
        top.add(right, BorderLayout.EAST);

        parent.add(top, BorderLayout.NORTH);
    }

    private void makeMainArea(JPanel parent) {
        JPanel middle = new JPanel(new BorderLayout(25, 25));
        middle.setOpaque(false);
        middle.setBorder(new EmptyBorder(25, 0, 25, 0));

        makeLeftPanel(middle);
        makeRightPanel(middle);

        parent.add(middle, BorderLayout.CENTER);
    }

    private void makeLeftPanel(JPanel parent) {
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Colors.BORDER_LIGHT, 1),
                new EmptyBorder(25, 25, 25, 25)
        ));
        left.setPreferredSize(new Dimension(320, 0));
        left.setBackground(Colors.BG_PANEL);

        JLabel controlTitle = new JLabel("Control");
        controlTitle.setFont(Settings.BIG_FONT);
        controlTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlTitle.setForeground(Colors.BLUE);

        JPanel input = makeInputArea();
        JPanel legend = makeLegendArea();
        JPanel results = makeResultArea();

        left.add(controlTitle);
        left.add(Box.createVerticalStrut(25));
        left.add(input);
        left.add(Box.createVerticalStrut(25));
        left.add(legend);
        left.add(Box.createVerticalStrut(25));
        left.add(results);
        left.add(Box.createVerticalGlue());

        parent.add(left, BorderLayout.WEST);
    }

    private JPanel makeInputArea() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel label = new JLabel(Settings.INPUT_LABEL);
        label.setFont(Settings.MID_FONT);
        label.setForeground(Colors.TEXT_LIGHT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        waterInput = new JTextField(String.valueOf(data.getWater()));
        waterInput.setMaximumSize(new Dimension(280, 45));
        waterInput.setPreferredSize(new Dimension(280, 45));
        waterInput.setAlignmentX(Component.LEFT_ALIGNMENT);
        waterInput.setFont(Settings.MID_FONT);
        waterInput.setBackground(Colors.BG_INPUT);
        waterInput.setForeground(Colors.TEXT_DARK);
        waterInput.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Colors.BORDER_DARK, 1),
                new EmptyBorder(12, 18, 12, 18)
        ));

        JButton calc = ButtonHelper.createButton(Settings.BTN_CALC, Colors.BLUE, 280, 50);
        calc.setMaximumSize(new Dimension(280, 50));
        calc.setAlignmentX(Component.LEFT_ALIGNMENT);
        calc.addActionListener(e -> calculate());

        JButton load = ButtonHelper.createButton(Settings.BTN_LOAD, Colors.SUCCESS_GREEN, 280, 50);
        load.setMaximumSize(new Dimension(280, 50));
        load.setAlignmentX(Component.LEFT_ALIGNMENT);
        load.addActionListener(e -> openFile());

        panel.add(label);
        panel.add(Box.createVerticalStrut(10));
        panel.add(waterInput);
        panel.add(Box.createVerticalStrut(20));
        panel.add(calc);
        panel.add(Box.createVerticalStrut(15));
        panel.add(load);

        return panel;
    }

    private JPanel makeLegendArea() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel title = new JLabel(Settings.LEGEND_TITLE);
        title.setFont(Settings.MID_FONT);
        title.setForeground(Colors.TEXT_LIGHT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel red = makeColorBox(Settings.NO_GAS, Colors.RED);
        JPanel yellow = makeColorBox(Settings.LOW_GAS, Colors.YELLOW);
        JPanel green = makeColorBox(Settings.HIGH_GAS, Colors.GREEN);

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

        JPanel box = new JPanel();
        box.setPreferredSize(new Dimension(24, 24));
        box.setBackground(color);
        box.setBorder(BorderFactory.createLineBorder(Colors.BORDER_LIGHT, 1));

        JLabel label = new JLabel(text);
        label.setFont(Settings.MID_FONT);
        label.setForeground(Colors.TEXT_LIGHT);

        panel.add(box);
        panel.add(Box.createHorizontalStrut(12));
        panel.add(label);

        return panel;
    }

    private JPanel makeResultArea() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel title = new JLabel(Settings.RESULT_TITLE);
        title.setFont(Settings.MID_FONT);
        title.setForeground(Colors.TEXT_LIGHT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        totalLabel = new JLabel(Settings.TOTAL_GAS);
        totalLabel.setFont(Settings.MID_FONT);
        totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        totalLabel.setForeground(Colors.TEXT_DARK);

        statusLabel = new JLabel(Settings.STATUS_READY);
        statusLabel.setFont(Settings.TINY_FONT);
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statusLabel.setForeground(Colors.SUCCESS);

        panel.add(title);
        panel.add(Box.createVerticalStrut(12));
        panel.add(totalLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(statusLabel);

        return panel;
    }

    private void makeRightPanel(JPanel parent) {
        JPanel right = new JPanel(new BorderLayout());
        right.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Colors.BORDER_LIGHT, 1),
                new EmptyBorder(25, 25, 25, 25)
        ));
        right.setBackground(Colors.BG_PANEL);

        JLabel title = new JLabel(Settings.GRID_TITLE);
        title.setFont(Settings.BIG_FONT);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(Colors.PURPLE);

        grid = new GridUI(data);
        grid.setFileCallback(file -> {
            if (data.loadFromFile(file.getAbsolutePath())) {
                updateDisplay();
                statusLabel.setText(Settings.STATUS_LOAD + file.getName());
                statusLabel.setForeground(Colors.SUCCESS);
                Display.showMessage(this, Settings.STATUS_OK, Settings.STATUS_DONE, JOptionPane.INFORMATION_MESSAGE);
            } else {
                statusLabel.setText(Settings.STATUS_FAIL);
                statusLabel.setForeground(Colors.DANGER);
                Display.showMessage(this, Settings.STATUS_ERROR, Settings.STATUS_CHECK, JOptionPane.ERROR_MESSAGE);
            }
        });

        grid.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (!hasData()) {
                    openFile();
                }
            }
        });

        JLabel info = new JLabel(Settings.GRID_INFO);
        info.setFont(Settings.TINY_FONT);
        info.setForeground(Colors.TEXT_LIGHT);
        info.setHorizontalAlignment(SwingConstants.CENTER);

        right.add(title, BorderLayout.NORTH);
        right.add(grid, BorderLayout.CENTER);
        right.add(info, BorderLayout.SOUTH);

        parent.add(right, BorderLayout.CENTER);
    }

    private JLabel makeIcon() {
        try {
            ImageIcon icon = new ImageIcon(Settings.APP_ICON_PATH);
            Image scaled = icon.getImage().getScaledInstance(Settings.ICON_SIZE_SMALL, Settings.ICON_SIZE_SMALL, Image.SCALE_SMOOTH);
            ImageIcon newIcon = new ImageIcon(scaled);
            return new JLabel(newIcon);
        } catch (Exception e) {
            JLabel fallback = new JLabel("💎");
            fallback.setFont(new Font("Segoe UI Emoji", Font.PLAIN, Settings.ICON_SIZE_SMALL));
            return fallback;
        }
    }

    private boolean hasData() {
        return data.getRows() > 0 && data.getCols() > 0;
    }

    private void openFile() {
        setupFileChooser();

        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("เลือกไฟล์ dept.txt");
        fc.setFileFilter(new FileNameExtensionFilter("ไฟล์ข้อความ (*.txt)", "txt"));
        fc.setCurrentDirectory(new File("src"));

        int result = fc.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            if (data.loadFromFile(file.getAbsolutePath())) {
                updateDisplay();
                statusLabel.setText(Settings.STATUS_LOAD + file.getName());
                statusLabel.setForeground(Colors.SUCCESS);
                Display.showMessage(this, Settings.STATUS_OK, Settings.STATUS_DONE, JOptionPane.INFORMATION_MESSAGE);
            } else {
                statusLabel.setText("โหลดไฟล์ไม่สำเร็จ");
                statusLabel.setForeground(Colors.DANGER);
                Display.showMessage(this, "ข้อผิดพลาด", "ไม่สามารถโหลดไฟล์ได้ กรุณาตรวจสอบรูปแบบไฟล์", JOptionPane.ERROR_MESSAGE);
            }
        }

        resetFileChooser();
    }

    private void setupFileChooser() {
        UIManager.put("FileChooser.background", Colors.BG_MAIN);
        UIManager.put("Panel.background", Colors.BG_MAIN);
        UIManager.put("Label.foreground", Colors.TEXT_DARK);
        UIManager.put("List.background", Colors.BG_PANEL);
        UIManager.put("List.foreground", Colors.TEXT_DARK);
        UIManager.put("List.selectionBackground", Colors.BLUE);
        UIManager.put("List.selectionForeground", Color.WHITE);
        UIManager.put("TextField.background", Colors.BG_INPUT);
        UIManager.put("TextField.foreground", Colors.TEXT_DARK);
        UIManager.put("Button.background", Colors.BG_PANEL);
        UIManager.put("Button.foreground", Colors.TEXT_DARK);
        UIManager.put("ComboBox.background", Colors.BG_INPUT);
        UIManager.put("ComboBox.foreground", Colors.TEXT_DARK);
    }

    private void resetFileChooser() {
        UIManager.put("FileChooser.background", null);
        UIManager.put("Panel.background", null);
        UIManager.put("Label.foreground", null);
        UIManager.put("List.background", null);
        UIManager.put("List.foreground", null);
        UIManager.put("List.selectionBackground", null);
        UIManager.put("List.selectionForeground", null);
        UIManager.put("TextField.background", null);
        UIManager.put("TextField.foreground", null);
        UIManager.put("Button.background", null);
        UIManager.put("Button.foreground", null);
        UIManager.put("ComboBox.background", null);
        UIManager.put("ComboBox.foreground", null);
    }

    private void calculate() {
        if (!hasData()) {
            Display.showMessage(this, "กรุณาโหลดไฟล์ก่อน", "คุณต้องโหลดไฟล์ dept.txt ก่อนคำนวณ", JOptionPane.WARNING_MESSAGE);
            statusLabel.setText("ยังไม่มีข้อมูลไฟล์");
            statusLabel.setForeground(Colors.DANGER);
            return;
        }

        try {
            double water = Double.parseDouble(waterInput.getText());
            data.setWater(water);
            updateDisplay();
            statusLabel.setText("คำนวณเสร็จสิ้น");
            statusLabel.setForeground(Colors.SUCCESS);
        } catch (NumberFormatException e) {
            Display.showMessage(this, "ข้อผิดพลาดการป้อนข้อมูล", "กรุณาป้อนตัวเลขที่ถูกต้อง", JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("ข้อมูลไม่ถูกต้อง");
            statusLabel.setForeground(Colors.DANGER);
        }
    }

    private void updateDisplay() {
        double total = data.getTotalVolume();
        totalLabel.setText(String.format(Settings.TOTAL_GAS, total));
        grid.refresh();
    }

    private void exitApp(){
        UIManager.put("OptionPane.background", Colors.BG_MAIN);
        UIManager.put("Panel.background", Colors.BG_MAIN);
        UIManager.put("OptionPane.messageForeground", Colors.TEXT_DARK);
        UIManager.put("OptionPane.messageFont", Settings.MID_FONT);
        UIManager.put("Button.background", Colors.BG_PANEL);
        UIManager.put("Button.foreground", Colors.TEXT_DARK);
        UIManager.put("Button.border", BorderFactory.createLineBorder(Colors.BORDER_LIGHT));

        int result = JOptionPane.showConfirmDialog(
                this,
                Settings.EXIT_MSG,
                Settings.EXIT_TITLE,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }

        UIManager.put("OptionPane.background", null);
        UIManager.put("Panel.background", null);
        UIManager.put("OptionPane.messageForeground", null);
        UIManager.put("OptionPane.messageFont", null);
        UIManager.put("Button.background", null);
        UIManager.put("Button.foreground", null);
        UIManager.put("Button.border", null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainApp();
        });
    }
}