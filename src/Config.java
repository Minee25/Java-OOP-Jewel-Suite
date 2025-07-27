import java.awt.*;

public class Config {

    public static final Color LIGHT_BLUE = new Color(240, 248, 255, 180);
    public static final Color WHITE_OVERLAY = new Color(255, 255, 255, 60);
    public static final Color BLUE_BORDER = new Color(100, 149, 237, 100);
    public static final Color BLACK_TEXT = new Color(30, 30, 30);
    public static final Color GRAY_TEXT = new Color(100, 100, 100);

    public static final Color RED = new Color(255, 99, 71);
    public static final Color YELLOW = new Color(255, 215, 0);
    public static final Color GREEN = new Color(50, 205, 50);

    public static final Color BLUE = new Color(0, 123, 255);
    public static final Color SUCCESS = new Color(40, 167, 69);
    public static final Color ORANGE = new Color(255, 193, 7);
    public static final Color DANGER = new Color(220, 53, 69);
    public static final Color PURPLE = new Color(108, 117, 255);

    public static final Color BG_START = new Color(250, 251, 255);
    public static final Color BG_END = new Color(236, 246, 255);

    public static final int GRID_W = 22;
    public static final int GRID_H = 10;
    public static final double CELL_SIZE = 150.0;
    public static final double DEFAULT_FLUID = 2500.0;
    public static final double TOP_BASE = 200.0;
    public static final double GAS_LIMIT = 0.5;

    public static final int WIN_W = 1350;
    public static final int WIN_H = 800;
    public static final int CELL_DRAW = 40;
    public static final Boolean WINDOW_MENU = true;


    public static final Font BIG_FONT = new Font("Tahoma", Font.BOLD, 26);
    public static final Font MID_FONT = new Font("Tahoma", Font.PLAIN, 14);
    public static final Font SMALL_FONT = new Font("Tahoma", Font.PLAIN, 13);
    public static final Font TINY_FONT = new Font("Tahoma", Font.PLAIN, 11);

    public static final String APP_ICON_PATH = "src/res/icon.png";
    public static final int ICON_SIZE_SMALL = 64;
    public static final int ICON_SIZE_LARGE = 72;

    public static final String LANG_TITLE = "Jewel Suite";
    public static final String LANG_ABOUT = "About";
    public static final String LANG_CONTROL = "Control";
    public static final String LANG_EXIT = "Exit";
    public static final String LANG_TITLE_EXIT = "Exit Confirmation";
    public static final String LANG_MSG_EXIT = "Are you sure you want to exit?";
    public static final String LANG_LABEL = "Liquid depth (M.)";
    public static final String LANG_CALC = "Calculate";
    public static final String LANG_LOAD = "Load File";
    public static final String LANG_DESC = "Description";
    public static final String LANG_NO_GAS = "NO GAS (0%)";
    public static final String LANG_LOW_GAS = "Low Gas (<50%)";
    public static final String LANG_LOT_GAS = "Lots of Gas (>50%)";
    public static final String LANG_RESULT = "Resultant:";
    public static final String LANG_TOTAL_GAS = "Total Gas Volume %.0f CB.M";
    public static final String LANG_STATUS = "Ready";
    public static final String LANG_GAS_TABLE = "GAS DISTRIBUTION TABLE";
    public static final String LANG_STATUS_LOADFILE = "Load File : ";
    public static final String LANG_STATUS_SUCCEED = "Succeed";
    public static final String LANG_STATUS_FILE = "File Loading Complete!";








}