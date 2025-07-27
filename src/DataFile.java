import java.io.*;
import java.util.*;

public class DataFile {

    private double[][] numbers;
    private double water;
    private int rows;
    private int cols;

    public DataFile() {
        this.water = Settings.DEFAULT_FLUID;
        loadFile();
    }

    public boolean loadFile() {
        return loadFromFile("dept.txt");
    }

    public boolean loadFromFile(String path) {
        try {
            Scanner s = new Scanner(new File(path));
            List<Double> nums = new ArrayList<>();

            while (s.hasNextDouble()) {
                nums.add(s.nextDouble());
            }
            s.close();

            this.rows = Settings.GRID_H;
            this.cols = Settings.GRID_W;
            this.numbers = new double[rows][cols];

            int i = 0;
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (i < nums.size()) {
                        numbers[r][c] = nums.get(i++);
                    }
                }
            }
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public double getBottom(int r, int c) {
        if (r >= 0 && r < rows && c >= 0 && c < cols) {
            return numbers[r][c];
        }
        return 0;
    }

    public double getTop(int r, int c) {
        return getBottom(r, c) - Settings.TOP_BASE;
    }

    public double getVolume(int r, int c) {
        double top = getTop(r, c);
        double bottom = getBottom(r, c);

        if (water <= top) {
            return 0;
        }

        double depth = Math.min(water, bottom) - top;
        if (depth <= 0) return 0;

        return Settings.CELL_SIZE * Settings.CELL_SIZE * depth;
    }

    public double getPercent(int r, int c) {
        double top = getTop(r, c);
        double bottom = getBottom(r, c);
        double total = bottom - top;

        if (total <= 0) return 0;

        double gas = Math.max(0, Math.min(water, bottom) - top);
        return gas / total;
    }

    public int getLevel(int r, int c) {
        double p = getPercent(r, c);

        if (p <= 0) return 0;
        if (p < Settings.GAS_LIMIT) return 1;
        return 2;
    }

    public double getTotalVolume() {
        double total = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                total += getVolume(r, c);
            }
        }
        return total;
    }

    public void setWater(double w) {
        this.water = w;
    }

    public double getWater() {
        return water;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}