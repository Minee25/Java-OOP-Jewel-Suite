import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

public class Grid extends JPanel {

    private Data data;
    private Timer pulse;
    private float beat = 0.0f;
    private boolean up = true;
    private boolean hasData = false;
    private DropTarget dropTarget;
    private GridDropHandler dropHandler;
    private boolean isWindows;

    public Grid(Data d) {
        this.data = d;
        this.hasData = (data.getRows() > 0 && data.getCols() > 0);
        this.isWindows = System.getProperty("os.name").toLowerCase().contains("windows");

        setOpaque(true);
        setBackground(new Color(0, 0, 0, 0));
        updateSize();

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (hasData) {
                    showInfo(e.getX(), e.getY());
                }
            }
        });

        setupDropZone();
        startPulse();
    }

    private void updateSize() {
        if (hasData) {
            setPreferredSize(new Dimension(
                    Config.GRID_W * Config.CELL_DRAW + 30,
                    Config.GRID_H * Config.CELL_DRAW + 30
            ));
        } else {
            setPreferredSize(new Dimension(700, 380));
        }
    }

    private void setupDropZone() {
        dropHandler = new GridDropHandler();
        dropTarget = new DropTarget(this, dropHandler);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        if (isWindows) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        } else {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        if (hasData) {
            paintGrid(g2);
        } else {
            paintDropZone(g2);
        }

        g2.dispose();
    }

    private void paintGrid(Graphics2D g2) {
        int startX = 15;
        int startY = 15;

        g2.setColor(new Color(255, 255, 255, 80));
        if (isWindows) {
            g2.fillRect(startX-8, startY-8,
                    Config.GRID_W * Config.CELL_DRAW + 16,
                    Config.GRID_H * Config.CELL_DRAW + 16);
        } else {
            g2.fillRoundRect(startX-8, startY-8,
                    Config.GRID_W * Config.CELL_DRAW + 16,
                    Config.GRID_H * Config.CELL_DRAW + 16, 20, 20);
        }

        for (int r = 0; r < data.getRows(); r++) {
            for (int c = 0; c < data.getCols(); c++) {
                int x = startX + c * Config.CELL_DRAW;
                int y = startY + r * Config.CELL_DRAW;

                Color cellColor = getColor(r, c);

                if (data.getLevel(r, c) == 2 && !isWindows) {
                    int alpha = (int)(255 - beat * 30);
                    cellColor = new Color(cellColor.getRed(), cellColor.getGreen(), cellColor.getBlue(), alpha);
                }

                g2.setColor(cellColor);
                if (isWindows) {
                    g2.fillRect(x, y, Config.CELL_DRAW-1, Config.CELL_DRAW-1);
                } else {
                    g2.fillRoundRect(x, y, Config.CELL_DRAW-1, Config.CELL_DRAW-1, 10, 10);
                }

                g2.setColor(new Color(255, 255, 255, 120));
                if (isWindows) {
                    g2.fillRect(x+2, y+2, Config.CELL_DRAW-5, (Config.CELL_DRAW-1)/3);
                } else {
                    g2.fillRoundRect(x+2, y+2, Config.CELL_DRAW-5, (Config.CELL_DRAW-1)/3, 8, 8);
                }

                g2.setColor(new Color(255, 255, 255, 150));
                g2.setStroke(new BasicStroke(0.5f));
                if (isWindows) {
                    g2.drawRect(x, y, Config.CELL_DRAW-1, Config.CELL_DRAW-1);
                } else {
                    g2.drawRoundRect(x, y, Config.CELL_DRAW-1, Config.CELL_DRAW-1, 10, 10);
                }
            }
        }
    }

    private void paintDropZone(Graphics2D g2) {
        int w = getWidth();
        int h = getHeight();

        g2.setColor(new Color(255, 255, 255, 60));
        if (isWindows) {
            g2.fillRect(20, 20, w-40, h-40);
        } else {
            g2.fillRoundRect(20, 20, w-40, h-40, 30, 30);
        }

        if (!isWindows) {
            float dashIntensity = 0.5f + beat * 0.3f;
            g2.setColor(new Color(Config.BLUE.getRed(), Config.BLUE.getGreen(), Config.BLUE.getBlue(), (int)(dashIntensity * 255)));
            g2.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{10, 10}, 0));
            g2.drawRoundRect(25, 25, w-50, h-50, 25, 25);
        } else {
            g2.setColor(Config.BLUE);
            g2.setStroke(new BasicStroke(2.0f));
            g2.drawRect(25, 25, w-50, h-50);
        }

        g2.setColor(Config.BLUE);
        Font iconFont = new Font("Segoe UI", Font.BOLD, 48);
        g2.setFont(iconFont);
        String icon = "📁";
        FontMetrics fm = g2.getFontMetrics();
        int iconX = (w - fm.stringWidth(icon)) / 2;
        int iconY = h/2 - 40;
        g2.drawString(icon, iconX, iconY);

        g2.setColor(Config.BLACK_TEXT);
        Font titleFont = new Font("Segoe UI", Font.BOLD, 20);
        g2.setFont(titleFont);
        String title = "วางไฟล์ dept.txt ที่นี่";
        fm = g2.getFontMetrics();
        int titleX = (w - fm.stringWidth(title)) / 2;
        int titleY = h/2 + 10;
        g2.drawString(title, titleX, titleY);

        g2.setColor(Config.GRAY_TEXT);
        Font subFont = new Font("Tahoma", Font.PLAIN, 14);
        g2.setFont(subFont);
        String sub = "หรือคลิกเพื่อเลือกไฟล์";
        fm = g2.getFontMetrics();
        int subX = (w - fm.stringWidth(sub)) / 2;
        int subY = h/2 + 35;
        g2.drawString(sub, subX, subY);
    }

    private void startPulse() {
        if (isWindows) {
            pulse = new Timer(500, e -> {
                beat = beat > 0.5f ? 0.0f : 1.0f;
                repaint();
            });
        } else {
            pulse = new Timer(120, e -> {
                if (up) {
                    beat += 0.03f;
                    if (beat >= 1.0f) {
                        beat = 1.0f;
                        up = false;
                    }
                } else {
                    beat -= 0.03f;
                    if (beat <= 0.0f) {
                        beat = 0.0f;
                        up = true;
                    }
                }
                repaint();
            });
        }
        pulse.start();
    }

    private Color getColor(int r, int c) {
        int level = data.getLevel(r, c);

        if (level == 0) return Config.RED;
        if (level == 1) return Config.YELLOW;
        return Config.GREEN;
    }

    private void showInfo(int mx, int my) {
        int startX = 15;
        int startY = 15;

        int c = (mx - startX) / Config.CELL_DRAW;
        int r = (my - startY) / Config.CELL_DRAW;

        if (r >= 0 && r < data.getRows() && c >= 0 && c < data.getCols()) {
            double vol = data.getVolume(r, c);
            double per = data.getPercent(r, c) * 100;
            double top = data.getTop(r, c);
            double base = data.getBase(r, c);

            String info = String.format(
                    "ช่อง: [%d, %d]\n\n" +
                            "ชั้นบน: %.1f ม.\n" +
                            "ชั้นล่าง: %.1f ม.\n" +
                            "ปริมาตร: %.0f ลบ.ม.\n" +
                            "แก๊ส: %.1f%%\n\n" +
                            "สถานะ: %s",
                    r+1, c+1, top, base, vol, per,
                    getStatus(data.getLevel(r, c))
            );

            System.out.println(info);
        }
    }

    private String getStatus(int level) {
        if (level == 0) return "ไม่มีแก๊ส";
        if (level == 1) return "แก๊สน้อย";
        return "แก๊สมาก";
    }

    public void update() {
        this.hasData = (data.getRows() > 0 && data.getCols() > 0);
        updateSize();
        revalidate();
        repaint();
    }

    public void setFileDropCallback(FileDropCallback callback) {
        dropHandler.setCallback(callback);
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        if (pulse != null) {
            pulse.stop();
        }
    }

    public interface FileDropCallback {
        void onFileDrop(File file);
    }

    private class GridDropHandler extends DropTargetAdapter {
        private FileDropCallback callback;

        public void setCallback(FileDropCallback callback) {
            this.callback = callback;
        }

        @Override
        public void dragEnter(DropTargetDragEvent e) {
            if (e.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                e.acceptDrag(DnDConstants.ACTION_COPY);
            } else {
                e.rejectDrag();
            }
        }

        @Override
        public void drop(DropTargetDropEvent e) {
            try {
                e.acceptDrop(DnDConstants.ACTION_COPY);
                Transferable t = e.getTransferable();
                List<File> files = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);

                if (!files.isEmpty()) {
                    File file = files.get(0);
                    if (file.getName().toLowerCase().endsWith(".txt") && callback != null) {
                        callback.onFileDrop(file);
                    }
                }
                e.dropComplete(true);
            } catch (Exception ex) {
                e.dropComplete(false);
            }
        }
    }
}