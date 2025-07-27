import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;


public class GridUI extends JPanel {

    private DataFile data;
    private boolean hasData = false;
    private DropTarget dropBox;
    private FileDropper dropper;

    public GridUI(DataFile d) {
        this.data = d;
        this.hasData = (data.getRows() > 0 && data.getCols() > 0);

        setOpaque(true);
        setBackground(Colors.BG_PANEL);
        updateSize();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (hasData) {
                    showCellInfo(e.getX(), e.getY());

                }
            }

        });

        setupDrop();
    }

    private void updateSize() {
        if (hasData) {
            setPreferredSize(new Dimension(
                    Settings.GRID_W * Settings.CELL_DRAW + 30,
                    Settings.GRID_H * Settings.CELL_DRAW + 30
            ));
        } else {
            setPreferredSize(new Dimension(700, 380));
        }
    }

    private void setupDrop() {
        dropper = new FileDropper();
        dropBox = new DropTarget(this, dropper);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (hasData) {
            drawGrid(g2);
        } else {
            drawDropZone(g2);
        }

        g2.dispose();
    }

    private void drawGrid(Graphics2D g2) {
        int startX = 15;
        int startY = 15;

        g2.setColor(Colors.BORDER_LIGHT);
        g2.fillRect(startX-5, startY-5,
                Settings.GRID_W * Settings.CELL_DRAW + 10,
                Settings.GRID_H * Settings.CELL_DRAW + 10);

        for (int r = 0; r < data.getRows(); r++) {
            for (int c = 0; c < data.getCols(); c++) {
                int x = startX + c * Settings.CELL_DRAW;
                int y = startY + r * Settings.CELL_DRAW;

                Color cellColor = getCellColor(r, c);
                g2.setColor(cellColor);
                g2.fillRect(x, y, Settings.CELL_DRAW-2, Settings.CELL_DRAW-2);

                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(1.0f));
                g2.drawRect(x, y, Settings.CELL_DRAW-2, Settings.CELL_DRAW-2);
            }
        }
    }

    private void drawDropZone(Graphics2D g2) {
        int w = getWidth();
        int h = getHeight();

        g2.setColor(Colors.BG_MAIN);
        g2.fillRect(0, 0, w, h);

        g2.setColor(Colors.BLUE);
        g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{10, 10}, 0));
        g2.drawRect(20, 20, w-40, h-40);

        g2.setColor(Colors.BLUE);
        Font iconFont = new Font("Dialog", Font.BOLD, 48);
        g2.setFont(iconFont);
        String icon = "📁";
        FontMetrics fm = g2.getFontMetrics();
        int iconX = (w - fm.stringWidth(icon)) / 2;
        int iconY = h/2 - 20;
        g2.drawString(icon, iconX, iconY);

        g2.setColor(Colors.TEXT_DARK);
        g2.setFont(Settings.MID_FONT);
        String title = Settings.FILE_TITLE;
        fm = g2.getFontMetrics();
        int titleX = (w - fm.stringWidth(title)) / 2;
        int titleY = h/2 + 20;
        g2.drawString(title, titleX, titleY);

        g2.setColor(Colors.TEXT_LIGHT);
        g2.setFont(Settings.SMALL_FONT);
        String sub = Settings.FILE_SUB;
        fm = g2.getFontMetrics();
        int subX = (w - fm.stringWidth(sub)) / 2;
        int subY = h/2 + 45;
        g2.drawString(sub, subX, subY);
    }

    private Color getCellColor(int r, int c) {
        int level = data.getLevel(r, c);
        if (level == 0) return Colors.RED;
        if (level == 1) return Colors.YELLOW;
        return Colors.GREEN;
    }


    private void showCellInfo(int mx, int my) {
        int startX = 15;
        int startY = 15;

        int c = (mx - startX) / Settings.CELL_DRAW;
        int r = (my - startY) / Settings.CELL_DRAW;

        if (r >= 0 && r < data.getRows() && c >= 0 && c < data.getCols()) {
            double vol = data.getVolume(r, c);
            double per = data.getPercent(r, c) * 100;
            double top = data.getTop(r, c);
            double bottom = data.getBottom(r, c);

            String info = String.format(
                    "ช่อง: [%d, %d]\n" +
                            "ชั้นบน: %.1f ม.\n" +
                            "ชั้นล่าง: %.1f ม.\n" +
                            "ปริมาตร: %.0f ลบ.ม.\n" +
                            "แก๊ส: %.1f%%\n" +
                            "สถานะ: %s",
                    r+1, c+1, top, bottom, vol, per,
                    getStatusText(data.getLevel(r, c))
            );
            Display.showMessage(this, "Info", info, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private String getStatusText(int level) {
        if (level == 0) return "ไม่มีแก๊ส";
        if (level == 1) return "แก๊สน้อย";
        return "แก๊สมาก";
    }

    public void refresh() {
        this.hasData = (data.getRows() > 0 && data.getCols() > 0);
        updateSize();
        revalidate();
        repaint();
    }

    public void setFileCallback(FileCallback callback) {
        dropper.setCallback(callback);
    }

    public interface FileCallback {
        void onFileDropped(File file);
    }

    private class FileDropper extends DropTargetAdapter {
        private FileCallback callback;

        public void setCallback(FileCallback callback) {
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
                        callback.onFileDropped(file);
                    }
                }
                e.dropComplete(true);
            } catch (Exception ex) {
                e.dropComplete(false);
            }
        }
    }
}