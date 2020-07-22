package com.dont.mapamental.utils;

import com.mortennobel.imagescaling.ResampleFilters;
import com.mortennobel.imagescaling.ResampleOp;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class Utils {

    public static final String LANGUAGE = "pt";
    public static final String MAIN_DIRECTORY = "C:\\Users\\Eduardo\\Desktop\\mapas";
    private final static SimpleDateFormat SDF = new SimpleDateFormat("[HH:mm:ss]");
    private final static Scanner scanner = new Scanner(System.in);

    public static String input(String question) {
        log(question);
        return scanner.nextLine();
    }

    public static void log(String message) {
        System.out.println(SDF.format(new Date()) + " > " + message);
    }

    public static void end(String message) {
        log(message);
        System.exit(0);
    }

    public static BufferedImage getImage(String path) throws IOException {
        return ImageIO.read(Utils.class.getClassLoader().getResourceAsStream(path));
    }

    public static BufferedImage scale(BufferedImage imageToScale, int dWidth, int dHeight) {
        ResampleOp resizeOp = new ResampleOp(dWidth, dHeight);
        resizeOp.setFilter(ResampleFilters.getLanczos3Filter());
        return resizeOp.filter(imageToScale, null);
    }

    public static int parseInt(String message) {
        try {
            return Integer.parseInt(message);
        } catch (Exception e) {
            return -1;
        }
    }

    public static String removeBlanklinesandMarkdown(String originalContent) {
        return Arrays.stream(originalContent.split("\n"))
                .filter(string -> string.length() != 0 && !string.startsWith("="))
                .collect(Collectors.joining(" "));
    }

    public static String removeDatesInParentheses(String originalContent) {
        return originalContent.replaceAll("\\((.*?)\\)", "");
    }

    private static final Random random = new Random();

    public static BufferedImage getRandomImage(String path) throws IOException {
        return getImage(getRandomFile(path));
    }

    public static Font getRandomFont(String path) throws IOException, FontFormatException {
        Font font = Font.createFont(Font.TRUETYPE_FONT, Utils.class.getClassLoader().getResourceAsStream(getRandomFile(path)));
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(font);
        return font;
    }

    public static String getRandomFile(String path) throws IOException {
        URL url = Utils.class.getClassLoader().getResource(path);
        File file;
        try {
            file = new File(url.toURI());
        } catch (Exception e) {
            file = new File(url.getPath());
        }
        List<File> urls = Arrays.asList(file.listFiles());
        return path + File.separator + urls.get(random.nextInt(urls.size())).getName();
    }
}
