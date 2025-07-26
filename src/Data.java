import java.io.*;
import java.util.*;

public class Data {

    private double[][] base;
    private double fluid;
    private int rows;
    private int cols;

    public Data() {
        this.fluid = Config.DEFAULT_FLUID;
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

            this.rows = Config.GRID_H;
            this.cols = Config.GRID_W;
            this.base = new double[rows][cols];

            int i = 0;
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (i < nums.size()) {
                        base[r][c] = nums.get(i++);
                    }
                }
            }
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public double getBase(int r, int c) {
        if (r >= 0 && r < rows && c >= 0 && c < cols) {
            return base[r][c];
        }
        return 0;
    }

    public double getTop(int r, int c) {
        return getBase(r, c) - Config.TOP_BASE;
    }

    public double getVolume(int r, int c) {
        double top = getTop(r, c);
        double bottom = getBase(r, c);

        if (fluid <= top) {
            return 0;
        }

        double depth = Math.min(fluid, bottom) - top;
        if (depth <= 0) return 0;

        return Config.CELL_SIZE * Config.CELL_SIZE * depth;
    }

    public double getPercent(int r, int c) {
        double top = getTop(r, c);
        double bottom = getBase(r, c);
        double total = bottom - top;

        if (total <= 0) return 0;

        double gas = Math.max(0, Math.min(fluid, bottom) - top);
        return gas / total;
    }

    public int getLevel(int r, int c) {
        double p = getPercent(r, c);

        if (p <= 0) return 0;
        if (p < Config.GAS_LIMIT) return 1;
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

    public void setFluid(double f) {
        this.fluid = f;
    }

    public double getFluid() {
        return fluid;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}