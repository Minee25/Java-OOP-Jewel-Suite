import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ButtonHelper {

    public static JButton createButton(String text, Color bgColor, int width, int height) {
        return new CustomButton(text, bgColor, width, height);
    }

    private static class CustomButton extends JButton {
        private boolean isHover = false;
        private boolean isPressed = false;
        private float shadowAlpha = 0.3f;
        private Color bgColor;

        public CustomButton(String text, Color bgColor, int width, int height) {
            super(text);
            this.bgColor = bgColor;

            setPreferredSize(new Dimension(width, height));
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setForeground(isLightColor(bgColor) ? new Color(60, 60, 60) : Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(8, 16, 8, 16));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHover = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHover = false;
                    repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    isPressed = true;
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    isPressed = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            if (isPressed) {
                w -= 2;
                h -= 2;
                g2.translate(1, 1);
            }

            if (!isPressed) {
                g2.setColor(new Color(0, 0, 0, (int)(shadowAlpha * 255 * (isHover ? 0.6f : 0.4f))));
                g2.fillRoundRect(2, 2, w-2, h-2, 8, 8);
            }

            Color currentBg = bgColor;
            if (isHover && !isPressed) {
                currentBg = brighten(bgColor, 0.1f);
            } else if (isPressed) {
                currentBg = darken(bgColor, 0.1f);
            }

            GradientPaint gradient = new GradientPaint(
                    0, 0, brighten(currentBg, 0.05f),
                    0, h, darken(currentBg, 0.05f)
            );
            g2.setPaint(gradient);
            g2.fillRoundRect(0, 0, w, h, 8, 8);

            g2.setColor(darken(currentBg, 0.2f));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(0, 0, w-1, h-1, 8, 8);

            if (isHover) {
                g2.setColor(new Color(255, 255, 255, 20));
                g2.fillRoundRect(1, 1, w-2, h/3, 6, 6);
            }

            super.paintComponent(g);
            g2.dispose();
        }

        private Color brighten(Color color, float factor) {
            int r = Math.min(255, (int)(color.getRed() * (1 + factor)));
            int g = Math.min(255, (int)(color.getGreen() * (1 + factor)));
            int b = Math.min(255, (int)(color.getBlue() * (1 + factor)));
            return new Color(r, g, b);
        }

        private Color darken(Color color, float factor) {
            int r = Math.max(0, (int)(color.getRed() * (1 - factor)));
            int g = Math.max(0, (int)(color.getGreen() * (1 - factor)));
            int b = Math.max(0, (int)(color.getBlue() * (1 - factor)));
            return new Color(r, g, b);
        }
    }

    private static boolean isLightColor(Color color) {
        double brightness = (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue()) / 255;
        return brightness > 0.7;
    }
}