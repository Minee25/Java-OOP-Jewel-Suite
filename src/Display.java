import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Display {

    public static void showMessage(Component parent, String title, String msg, int type) {
        UIManager.put("OptionPane.background", Colors.BG_MAIN);
        UIManager.put("Panel.background", Colors.BG_MAIN);
        UIManager.put("OptionPane.messageForeground", Colors.TEXT_DARK);
        UIManager.put("OptionPane.messageFont", Settings.MID_FONT);
        UIManager.put("Button.background", Colors.BG_INPUT);
        UIManager.put("Button.foreground", Colors.TEXT_DARK);
        UIManager.put("Button.border", BorderFactory.createLineBorder(Colors.BORDER_DARK));

        JOptionPane.showMessageDialog(parent, msg, title, type);

        UIManager.put("OptionPane.background", null);
        UIManager.put("Panel.background", null);
        UIManager.put("OptionPane.messageForeground", null);
        UIManager.put("OptionPane.messageFont", null);
        UIManager.put("Button.background", null);
        UIManager.put("Button.foreground", null);
        UIManager.put("Button.border", null);
    }

    public static void showAbout(Component parent) {
        JDialog aboutBox = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), "About Jewel Suite", true);
        aboutBox.setSize(850, 480);
        aboutBox.setLocationRelativeTo(parent);
        aboutBox.setResizable(false);
        aboutBox.getContentPane().setBackground(Colors.BG_MAIN);

        JPanel main = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(Colors.BG_MAIN);
            }
        };
        main.setBackground(Colors.BG_MAIN);
        main.setOpaque(true);
        main.setBorder(new EmptyBorder(35, 35, 35, 35));

        JLabel title = new JLabel("Jewel Suite", SwingConstants.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 32));
        title.setForeground(Colors.BLUE);

        JLabel subtitle = new JLabel("ระบบจำลองการกระจายตัวของแก๊ส", SwingConstants.CENTER);
        subtitle.setFont(new Font("Tahoma", Font.PLAIN, 16));
        subtitle.setForeground(Colors.TEXT_LIGHT);

        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(Colors.BG_MAIN);
            }
        };
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(Colors.BG_MAIN);
        header.setOpaque(true);
        header.add(title);
        header.add(Box.createVerticalStrut(8));
        header.add(subtitle);
        header.add(Box.createVerticalStrut(30));

        JPanel team = new JPanel(new GridLayout(1, 3, 25, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(Colors.BG_MAIN);
            }
        };
        team.setBackground(Colors.BG_MAIN);
        team.setOpaque(true);

        team.add(makeMemberBox(Settings.IMG1, Settings.MEMBER1, Settings.ID1, Settings.JOB1));
        team.add(makeMemberBox(Settings.IMG2, Settings.MEMBER2, Settings.ID2, Settings.JOB2));
        team.add(makeMemberBox(Settings.IMG3, Settings.MEMBER3, Settings.ID3, Settings.JOB3));

        JPanel buttons = new JPanel(new FlowLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(Colors.BG_MAIN);
            }
        };
        buttons.setBackground(Colors.BG_MAIN);
        buttons.setBorder(new EmptyBorder(20, 0, 0, 0));
        buttons.setOpaque(true);

        JButton close = ButtonHelper.createButton("ปิด", Colors.BLUE, 100, 45);
        close.addActionListener(e -> aboutBox.dispose());
        buttons.add(close);

        main.add(header, BorderLayout.NORTH);
        main.add(team, BorderLayout.CENTER);
        main.add(buttons, BorderLayout.SOUTH);

        aboutBox.add(main);
        aboutBox.setVisible(true);
    }

    private static JPanel makeMemberBox(String imgPath, String name, String id, String job) {
        JPanel box = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(Colors.BG_PANEL);
            }
        };
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(Colors.BG_PANEL);
        box.setOpaque(true);
        box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Colors.BORDER_DARK, 1),
                new EmptyBorder(25, 20, 25, 20)
        ));

        JLabel img = makeImage(imgPath);
        img.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
        nameLabel.setForeground(Colors.TEXT_DARK);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel idLabel = new JLabel("รหัสนิสิต: " + id, SwingConstants.CENTER);
        idLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        idLabel.setForeground(Colors.TEXT_LIGHT);
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel jobLabel = new JLabel("ตำแหน่ง: " + job, SwingConstants.CENTER);
        jobLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        jobLabel.setForeground(Colors.PURPLE);
        jobLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        box.add(img);
        box.add(Box.createVerticalStrut(15));
        box.add(nameLabel);
        box.add(Box.createVerticalStrut(8));
        box.add(idLabel);
        box.add(Box.createVerticalStrut(8));
        box.add(jobLabel);

        return box;
    }

    private static JLabel makeImage(String imgPath) {
        try {
            ImageIcon icon = new ImageIcon(imgPath);
            Image scaled = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            ImageIcon newIcon = new ImageIcon(scaled);
            return new JLabel(newIcon, SwingConstants.CENTER);
        } catch (Exception e) {
            JLabel fallback = new JLabel("👤", SwingConstants.CENTER);
            fallback.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 72));
            return fallback;
        }
    }
}