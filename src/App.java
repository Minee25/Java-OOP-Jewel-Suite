import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class App extends JFrame {

    private Data data;
    private Grid grid;
    private JTextField fluidText;
    private JLabel volumeLabel;
    private JLabel statusLabel;

    public App() {
        setupLook();
        data = new Data();
        setupUI();
        makeContent();
        updateAll();
        setVisible(true);
    }

    private void setupLook() {
        try {
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
        setUndecorated(Config.WINDOW_MENU);
        getContentPane().setBackground(Config.BG_START);
    }

    private void makeContent() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBorder(new EmptyBorder(25, 25, 25, 25));
        main.setOpaque(false);

        makeTop(main);
        makeMiddle(main);

        setContentPane(main);
    }

    private void makeTop(JPanel parent) {
        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(new EmptyBorder(20, 25, 20, 25));
        top.setBackground(new Color(255, 255, 255, 180));
        top.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237, 100), 1),
                new EmptyBorder(20, 25, 20, 25)
        ));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
        left.setOpaque(false);

        JLabel icon = createIconLabel();
        JLabel title = new JLabel(Config.LANG_TITLE);
        title.setFont(Config.BIG_FONT);
        title.setForeground(Config.BLUE);

        left.add(icon);
        left.add(Box.createHorizontalStrut(10));
        left.add(title);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setOpaque(false);

        JButton about = new JButton(Config.LANG_ABOUT);
        about.setPreferredSize(new Dimension(100, 45));
        about.setBackground(Config.ORANGE);
        about.setForeground(Color.WHITE);
        about.setFont(Config.MID_FONT);
        about.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        about.setFocusPainted(false);
        about.addActionListener(e -> UI.showAbout(this));

        JButton exit = new JButton(Config.LANG_EXIT);
        exit.setPreferredSize(new Dimension(100, 45));
        exit.setBackground(Config.DANGER);
        exit.setForeground(Color.WHITE);
        exit.setFont(Config.MID_FONT);
        exit.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        exit.setFocusPainted(false);
        exit.addActionListener(e -> exitApp());

        right.add(about);
        right.add(exit);

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
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237, 100), 1),
                new EmptyBorder(25, 25, 25, 25)
        ));
        left.setPreferredSize(new Dimension(320, 0));
        left.setBackground(new Color(255, 255, 255, 180));

        JLabel ctrlTitle = new JLabel(Config.LANG_CONTROL);
        ctrlTitle.setFont(Config.BIG_FONT);
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

        JLabel label = new JLabel(Config.LANG_LABEL);
        label.setFont(Config.MID_FONT);
        label.setForeground(Config.GRAY_TEXT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        fluidText = new JTextField(String.valueOf(data.getFluid()));
        fluidText.setMaximumSize(new Dimension(280, 45));
        fluidText.setPreferredSize(new Dimension(280, 45));
        fluidText.setAlignmentX(Component.LEFT_ALIGNMENT);
        fluidText.setFont(Config.MID_FONT);
        fluidText.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Config.BLUE_BORDER, 1),
                new EmptyBorder(12, 18, 12, 18)
        ));

        JButton calc = new JButton(Config.LANG_CALC);
        calc.setMaximumSize(new Dimension(280, 50));
        calc.setPreferredSize(new Dimension(280, 50));
        calc.setAlignmentX(Component.LEFT_ALIGNMENT);
        calc.setBackground(Config.BLUE);
        calc.setForeground(Color.WHITE);
        calc.setFont(Config.MID_FONT);
        calc.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        calc.setFocusPainted(false);
        calc.addActionListener(e -> doCalc());

        JButton load = new JButton(Config.LANG_LOAD);
        load.setMaximumSize(new Dimension(280, 50));
        load.setPreferredSize(new Dimension(280, 50));
        load.setAlignmentX(Component.LEFT_ALIGNMENT);
        load.setBackground(Config.SUCCESS);
        load.setForeground(Color.WHITE);
        load.setFont(Config.MID_FONT);
        load.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        load.setFocusPainted(false);
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

        JLabel title = new JLabel(Config.LANG_DESC);
        title.setFont(Config.MID_FONT);
        title.setForeground(Config.GRAY_TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel red = makeColorBox(Config.LANG_NO_GAS, Config.RED);
        JPanel yellow = makeColorBox(Config.LANG_LOW_GAS, Config.YELLOW);
        JPanel green = makeColorBox(Config.LANG_LOT_GAS, Config.GREEN);

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
        box.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        JLabel label = new JLabel(text);
        label.setFont(Config.MID_FONT);
        label.setForeground(Config.GRAY_TEXT);

        panel.add(box);
        panel.add(Box.createHorizontalStrut(12));
        panel.add(label);

        return panel;
    }

    private JPanel makeResultPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel title = new JLabel(Config.LANG_RESULT);
        title.setFont(Config.MID_FONT);
        title.setForeground(Config.GRAY_TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        volumeLabel = new JLabel(Config.LANG_TOTAL_GAS);
        volumeLabel.setFont(Config.MID_FONT);
        volumeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        volumeLabel.setForeground(Config.BLACK_TEXT);

        statusLabel = new JLabel(Config.LANG_STATUS);
        statusLabel.setFont(Config.TINY_FONT);
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
        JPanel right = new JPanel(new BorderLayout());
        right.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237, 100), 1),
                new EmptyBorder(25, 25, 25, 25)
        ));
        right.setBackground(new Color(255, 255, 255, 180));

        JLabel title = new JLabel(Config.LANG_GAS_TABLE);
        title.setFont(Config.BIG_FONT);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(Config.PURPLE);

        grid = new Grid(data);
        grid.setFileDropCallback(file -> {
            if (data.loadFromFile(file.getAbsolutePath())) {
                updateAll();
                statusLabel.setText(Config.LANG_STATUS_LOADFILE + file.getName());
                statusLabel.setForeground(Config.SUCCESS);
                UI.showMessage(this, Config.LANG_STATUS_SUCCEED, Config.LANG_STATUS_COM, JOptionPane.INFORMATION_MESSAGE);
            } else {
                statusLabel.setText(Config.LANG_STATUS_FAILED);
                statusLabel.setForeground(Config.DANGER);
                UI.showMessage(this, Config.LANG_STATUS_ERE, Config.LANG_STATUS_CHECK, JOptionPane.ERROR_MESSAGE);
            }
        });

        grid.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (!hasGridData()) {
                    openFile();
                }
            }
        });

        JLabel info = new JLabel(Config.LANG_CLICK);
        info.setFont(Config.TINY_FONT);
        info.setForeground(Config.GRAY_TEXT);
        info.setHorizontalAlignment(SwingConstants.CENTER);

        right.add(title, BorderLayout.NORTH);
        right.add(grid, BorderLayout.CENTER);
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
        fc.setCurrentDirectory(new File("src"));

        int result = fc.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            if (data.loadFromFile(file.getAbsolutePath())) {
                updateAll();
                statusLabel.setText(Config.LANG_STATUS_LOADFILE + file.getName());
                statusLabel.setForeground(Config.SUCCESS);
                UI.showMessage(this, Config.LANG_STATUS_SUCCEED, Config.LANG_STATUS_COM, JOptionPane.INFORMATION_MESSAGE);
            } else {
                statusLabel.setText("โหลดไฟล์ไม่สำเร็จ");
                statusLabel.setForeground(Config.DANGER);
                UI.showMessage(this, "ข้อผิดพลาด", "ไม่สามารถโหลดไฟล์ได้ กรุณาตรวจสอบรูปแบบไฟล์", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void doCalc() {
        if (!hasGridData()) {
            UI.showMessage(this, "กรุณาโหลดไฟล์ก่อน", "คุณต้องโหลดไฟล์ dept.txt ก่อนคำนวณ", JOptionPane.WARNING_MESSAGE);
            statusLabel.setText("ยังไม่มีข้อมูลไฟล์");
            statusLabel.setForeground(Config.DANGER);
            return;
        }

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
        volumeLabel.setText(String.format(Config.LANG_TOTAL_GAS, total));
        grid.update();
    }

    private void exitApp(){
        int result = JOptionPane.showConfirmDialog(
                this,
                Config.LANG_TITLE_EXIT,
                Config.LANG_MSG_EXIT,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new App();
        });
    }
}