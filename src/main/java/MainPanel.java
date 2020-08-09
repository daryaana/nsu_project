import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public class MainPanel extends JPanel {
    BufferedImage firstImage = null;
    BufferedImage secondImage = null;
    BufferedImage thirdImage = null;
    private int imageWidth = 350;
    private int imageHeight = 350;
    boolean visible;
    private boolean dragging;
    Rectangle selector;
    Rectangle bounds;
    private int X = 0;
    private int Y = 0;
    private double scale;
    int red, green, blue;
    private final int[][] matrixSharpness = {
            {0, -1, 0},
            {-1, 5, -1},
            {0, -1, 0}};
    private final int[][] matrixEmbossing = {
            {0, 1, 0},
            {-1, 0, 1},
            {0, -1, 0}};
    private final int[][] matrixBlur = {
            {0, 1, 0},
            {1, 2, 1},
            {0, 1, 0}};
    private final int[][] orderedMatrix = {

                /*    { 0, 48, 12, 60, 3, 51, 15, 63 },
                    { 32, 16, 44, 28, 35, 19, 47, 31 },
                    { 8, 56, 4, 52, 11, 59, 7, 55 },
                    { 40, 24, 36, 20, 43, 27, 39, 23 },
                    { 2, 50, 14, 62, 1, 49, 13, 51 },
                    { 35, 18, 46, 30, 33, 17, 45, 29 },
                    { 10, 58, 6, 54, 9, 57, 5, 53 },
                    { 42, 26, 38, 22, 41, 25, 37, 21 }
*/
            {0, 32, 8, 40, 2, 34, 10, 42},
            {48, 16, 56, 24, 50, 18, 58, 26},
            {12, 44, 4, 36, 14, 46, 6, 38},
            {60, 28, 52, 20, 62, 30, 54, 22},
            {3, 35, 11, 43, 1, 33, 9, 41},
            {51, 19, 59, 27, 49, 17, 57, 25},
            {15, 47, 7, 39, 13, 45, 5, 37},
            {63, 31, 55, 23, 61, 29, 53, 21}
    };


    boolean selected = false;
    private int angle;//=0;

    private int threshold; //= 10;

    void setAngle(int angle) {
        this.angle = angle;
    }

    /*private Point setSelector(int x, int y) {
        if (x < bounds.x) x = bounds.x;
        if (y < bounds.y) y = bounds.y;
        if (x + selector.width > bounds.x + bounds.width) {
            x = bounds.x + bounds.width - selector.width;
        }
        if (y + selector.height > bounds.y + bounds.height) {
            y = bounds.y + bounds.height - selector.height;
        }
        Point p = new Point(x, y);
        selector.setLocation(p);
        return p;
    }*/

    void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    MainPanel() {
       /* addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("Mouse Pressed in MP");
                Point p = e.getPoint();
                if (bounds != null && selected && bounds.contains(p)) {
                    visible = true;
                    dragging = true;
                    int x = e.getX() - selector.width / 2;
                    int y = e.getY() - selector.height / 2;
                    setSelector(x, y);
                    X = (int) ((selector.x - 10) / scale);
                    Y = (int) ((selector.y - 10) / scale);
                    int shiftX = 0;
                    int shiftY = 0;
                    int sideX = 350;
                    int sideY = 350;
                  /* if (scale == 1.0) {
                        sideX = firstImage.getWidth();
                        sideY = firstImage.getHeight();
                    }*/
        /*            if (Y + sideY > firstImage.getHeight()) {
                        shiftY = firstImage.getHeight() - Y - sideY;
                    }
                    if (X + sideX > firstImage.getWidth()) {
                        shiftX = firstImage.getWidth() - X - sideX;
                    }
                    secondImage = firstImage.getSubimage(X + shiftX, Y + shiftY, sideX, sideY);

                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println("Mouse Released MP");
                dragging = false;
                visible = false;
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                System.out.println("DRRRRAAAAAGGED");
                Point p = e.getPoint();
                if (bounds != null && selected && bounds.contains(p)) {
                    if (dragging) {
                        int x = e.getX();
                        int y = e.getY();
                        setSelector(x - selector.width / 2, y - selector.height / 2);
                        X = (int) ((selector.x - 10) / scale);
                        Y = (int) ((selector.y - 10) / scale);
                        int shiftX = 0;
                        int shiftY = 0;
                        int sideX = 350;
                        int sideY = 350;
                    /*   if (scale == 1.0) {
                            sideX = firstImage.getWidth();
                            sideY = firstImage.getHeight();
                        }*/
                   /*     if (Y + sideY > firstImage.getHeight()) {
                            shiftY = firstImage.getHeight() - Y - sideY;
                        }
                        if (X + sideX > firstImage.getWidth()) {
                            shiftX = firstImage.getWidth() - X - sideX;
                        }
                        secondImage = firstImage.getSubimage(X + shiftX, Y + shiftY, sideX, sideY);

                        repaint();
                    }
                }
            }
        });*/
    }


    public void loadFile(File file) {
        try {
            firstImage = ImageIO.read(new File(file.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int imageHeight = firstImage.getHeight();
        int imageWidth = firstImage.getWidth();

        double scaleHeight = imageHeight / (double) imageHeight;
        double scaleWidth = imageWidth / (double) imageWidth;
        scale = scaleHeight < scaleWidth ? scaleHeight : scaleWidth;

        if (scale >= 1.0) {
            scale = 1.0;
        }

        if (scale == 1.0) {
            selector = new Rectangle(0, 0, imageWidth, imageHeight);
        } else {
            selector = new Rectangle(0, 0, (int) (350 * scale), (int) (350 * scale));
        }

        int height = (int) (imageHeight * scale);
        int width = (int) (imageWidth * scale);

        bounds = new Rectangle(10, 10, width, height);

        secondImage = null;
        thirdImage = null;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.BLACK);
        drawDashedLine(g, 10, 10, 360, 10);//-
        drawDashedLine(g, 360, 10, 360, 360);//|
        drawDashedLine(g, 360, 360, 10, 360);
        drawDashedLine(g, 10, 360, 10, 10);


        drawDashedLine(g, 370, 10, 720, 10);
        drawDashedLine(g, 720, 10, 720, 360);
        drawDashedLine(g, 720, 360, 370, 360);
        drawDashedLine(g, 370, 360, 370, 10);

        drawDashedLine(g, 730, 10, 1080, 10);
        drawDashedLine(g, 1080, 10, 1080, 360);
        drawDashedLine(g, 1080, 360, 730, 360);
        drawDashedLine(g, 730, 360, 730, 10);

        if (firstImage != null) {
            if (firstImage.getWidth() > 350 || firstImage.getHeight() > 350) {
                g.drawImage(firstImage, 10, 10, 350, 350, null);
            } else {
                g.drawImage(firstImage, 10, 10, null);
            }
        }

        if (secondImage != null) {
            if (secondImage.getWidth() > 350 || secondImage.getHeight() > 350) {
                g.drawImage(secondImage, 370, 10, 350, 350, null);
            } else {
                g.drawImage(secondImage, 370, 10, null);
            }
        }

        if (thirdImage != null) {
            if (thirdImage.getWidth() > 350 || thirdImage.getHeight() > 350) {
                g.drawImage(thirdImage, 730, 10, 350, 350, null);
            } else {
                g.drawImage(thirdImage, 730, 10, null);
            }
        }
        g.setXORMode(Color.WHITE);
        Graphics2D g2d = (Graphics2D) g.create();
        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{3}, 0);
        g2d.setStroke(dashed);
        if (visible) {
            g2d.draw(selector);
        }

    }

    private void drawDashedLine(Graphics g, int x1, int y1, int x2, int y2) {
        Graphics2D graphics2D = (Graphics2D) g.create();
        Stroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{3}, 0);
        graphics2D.setStroke(dashed);
        graphics2D.drawLine(x1, y1, x2, y2);
    }

    public void blackWhiteFilter() {
        thirdImage = blackWhite(secondImage);
        repaint();
    }
    public void mirrorFilter(){
        thirdImage = mirror(secondImage);
        repaint();
    }
    public BufferedImage mirror(BufferedImage image) {
        if (secondImage == null) {
            image = firstImage;
        }
        thirdImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB/*image.getType()*/);
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight() / 2; j++) {
                int tmp = image.getRGB(i, j);
                thirdImage.setRGB(i, j, image.getRGB(i, image.getHeight() - j - 1));
                thirdImage.setRGB(i, image.getHeight() - j - 1, tmp);
            }
        }
        return thirdImage;
    }


    public BufferedImage blackWhite(BufferedImage image) {

        if (secondImage == null) {
            image = firstImage;
        }
        thirdImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB/*image.getType()*/);

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                double newPixel = (new Color(image.getRGB(i, j)).getRed() * 0.299) +
                        (new Color(image.getRGB(i, j)).getBlue() * 0.144) +
                        (new Color(image.getRGB(i, j)).getGreen() * 0.587);
                if (newPixel > 255) {
                    newPixel = 255;
                } else {
                    newPixel = newPixel;
                }
                newPixel = new Color((int) newPixel, (int) newPixel, (int) newPixel).getRGB();
                thirdImage.setRGB(i, j, (int) newPixel);
            }
        }
        BufferedImage pattern = null;
        try {
            pattern = ImageIO.read(new File("src/main/resources/images/pattern (3).jpg"));
        } catch (IOException e) {
        }
        StereoImageCreator stereoImageCreator = new StereoImageCreator();
        System.out.println(thirdImage.getWidth() + " " + thirdImage.getHeight());
    BufferedImage imgg = stereoImageCreator.generateStereoPicture(firstImage, pattern);
        return imgg;
    }


    public void blurFilter() {
        thirdImage = blur(secondImage);
        repaint();

    }

    public BufferedImage blur(BufferedImage secondImage) {

        if (secondImage == null) {
            secondImage = firstImage;
        }
        return multiplyMatrix(secondImage, matrixBlur, 6, 0);
    }

    public void watercolorFilter() {
        thirdImage = watercolor(secondImage);
        repaint();
    }

    public BufferedImage watercolor(BufferedImage image) {
        if (secondImage == null) {
            image = firstImage;
        }
        int pixels[] = null;

        BufferedImage thirdImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        for (int i = 2; i < image.getWidth() - 2; i++) {
            for (int j = 2; j < image.getHeight() - 2; j++) {
                pixels = image.getRGB(i - 2, j - 2, 5, 5, pixels, 0, 5);
                Arrays.sort(pixels, 0, pixels.length);
                thirdImage.setRGB(i, j, pixels[13]);
            }
        }
        return thirdImage;
    }

    public void c2b() {
        secondImage = thirdImage;
        repaint();
    }

    public void rotateFilter() {
        thirdImage = rotate(secondImage, angle);
        repaint();
    }

    BufferedImage rotate(BufferedImage secondImage, int angle) {
        if (secondImage == null) {
            secondImage = firstImage;
        }

        int width = secondImage.getWidth();
        int height = secondImage.getHeight();

        double oldWidthRadius = (double) (width - 1) / 2;
        double oldHeightRadius = (double) (height - 1) / 2;

        double angleCos = Math.cos(angle * Math.PI / 180);
        double angleSin = Math.sin(angle * Math.PI / 180);

        double halfWidth = (double) secondImage.getWidth() / 2;
        double halfHeight = (double) secondImage.getHeight() / 2;

        double x1 = halfWidth * angleCos;
        double y1 = halfWidth * angleSin;

        double x2 = halfWidth * angleCos - halfHeight * angleSin;
        double y2 = halfWidth * angleSin + halfHeight * angleCos;

        double x3 = -halfHeight * angleSin;
        double y3 = halfHeight * angleCos;

        double x4 = 0;
        double y4 = 0;

        halfWidth = Math.max(Math.max(x1, x2), Math.max(x3, x4)) - Math.min(Math.min(x1, x2), Math.min(x3, x4));
        halfHeight = Math.max(Math.max(y1, y2), Math.max(y3, y4)) - Math.min(Math.min(y1, y2), Math.min(y3, y4));

        int newWidth = (int) (halfWidth * 2);
        int newHeight = (int) (halfHeight * 2);

        BufferedImage thirdImage = new BufferedImage(newWidth, newHeight, secondImage.getType());

        double newWidthRadius = (double) (newWidth - 1) / 2;
        double newHeightRadius = (double) (newHeight - 1) / 2;

        double negHR = -newHeightRadius;

        for (int i = 0; i < newHeight; i++) {
            double negWR = -newWidthRadius;
            for (int j = 0; j < newWidth; j++) {
                int oi = (int) (angleCos * negHR + angleSin * negWR + oldHeightRadius);
                int oj = (int) (-angleSin * negHR + angleCos * negWR + oldWidthRadius);

                if ((oi < 0) || (oj < 0) || (oi >= height) || (oj >= width)) {
                    thirdImage.setRGB(i, j, new Color(255, 255, 255).getRGB());
                } else {
                    int red = new Color(secondImage.getRGB(oi, oj)).getRed();
                    int blue = new Color(secondImage.getRGB(oi, oj)).getBlue();
                    int green = new Color(secondImage.getRGB(oi, oj)).getGreen();
                    thirdImage.setRGB(i, j, new Color(red, green, blue).getRGB());
                }
                negWR++;
            }
            negHR++;
        }
        return thirdImage;

    }

    public void orderedFilter() {
        thirdImage = ordered(secondImage);
        repaint();
    }

    BufferedImage ordered(BufferedImage secondImage) {
        if (secondImage == null) {
            secondImage = firstImage;
        }


        final int N = 8;
        final int length = 64;

        BufferedImage thirdImage = new BufferedImage(secondImage.getWidth(), secondImage.getHeight(), secondImage.getType());

        for (int i = 0; i < secondImage.getWidth(); i++) {
            for (int j = 0; j < secondImage.getHeight(); j++) {
                int red = new Color(secondImage.getRGB(i, j)).getRed();
                int blue = new Color(secondImage.getRGB(i, j)).getBlue();
                int green = new Color(secondImage.getRGB(i, j)).getGreen();
                if (red * length / 256 > orderedMatrix[i % N][j % N]) {
                    red = 255 * (red / 256 +1 );
                } else {
                    red = 255 * (red / 256);
                }
                if (blue * length / 256 > orderedMatrix[i % N][j % N]) {
                    blue = 255 * (blue / 256 +1);
                } else {
                    blue = 255 * (blue / 256);
                }
                if (green * length / 256 > orderedMatrix[i % N][j % N]) {
                    green = 255 * (green / 256+ 1);
                } else {
                    green = 255 * (green / 256);
                }
                thirdImage.setRGB(i, j, new Color(red, green, blue).getRGB());
            }
        }
        return thirdImage;

    }

    private BufferedImage multiplyMatrix(BufferedImage image, int[][] matrix, int div, int constant) {
        if (secondImage == null) {
            image = firstImage;
        }
        BufferedImage thirdImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int[] newPixel = newColor(image, i, j, matrix);
                int red = checkColor(newPixel[0] / div + constant);
                int green = checkColor(newPixel[1] / div + constant);
                int blue = checkColor(newPixel[2] / div + constant);
                thirdImage.setRGB(i, j, new Color(red, green, blue).getRGB());
            }
        }


        return thirdImage;
    }

    private int checkColor(int color) {
        if (color < 0) {
            color = 0;
        }
        if (color > 255) {
            color = 255;
        }


        return color;
    }

    public void embossing() {
        thirdImage = embossingFilter(secondImage);
        repaint();
    }

    BufferedImage embossingFilter(BufferedImage secondImage) {
        if (secondImage == null) {
            secondImage = firstImage;
        }

        return multiplyMatrix(secondImage, matrixEmbossing, 1, 128);


    }

    private int[] newColor(BufferedImage image, int i, int j, int[][] matrix) {
        int[] newPixel = new int[3];
        int newRed = 0;
        int newGreen = 0;
        int newBlue = 0;
        for (int n = 0; n < matrix.length; n++) {
            for (int m = 0; m < matrix.length; m++) {
                int x = countIndex(image.getWidth(), i, n, matrix.length);
                int y = countIndex(image.getHeight(), j, m, matrix.length);

                newRed += matrix[n % matrix.length][m % matrix.length] * (new Color(image.getRGB(x, y))).getRed();
                newGreen += matrix[n % matrix.length][m % matrix.length] * (new Color(image.getRGB(x, y))).getGreen();
                newBlue += matrix[n % matrix.length][m % matrix.length] * (new Color(image.getRGB(x, y))).getBlue();
            }
        }
        newPixel[0] = newRed;
        newPixel[1] = newGreen;
        newPixel[2] = newBlue;

        return newPixel;
    }

    private int countIndex(int param, int index_1, int index_2, int size) {
        int result;

        if (index_1 == 0) result = index_1 + index_2 % size;
        else if (index_1 == param - 1) result = (index_1 - 2) + index_2 % size;
        else result = (index_1 - 1) + index_2 % size;

        return result;
    }


    public void setRGB(int redInit, int greenInit, int blueInit) {
        red = redInit;
        green = greenInit;
        blue = blueInit;
    }


    void sharpnessFilter() {
        thirdImage = sharpness(secondImage);
        repaint();
    }

    public BufferedImage sharpness(BufferedImage secondImage) {
        if (secondImage == null) {
            secondImage = firstImage;
        }
        return multiplyMatrix(secondImage, matrixSharpness, 1, 0);

    }

    public void negativeFilter() {
        thirdImage = negative(secondImage);
        repaint();
    }

    public BufferedImage negative(BufferedImage secondImage) {
        if (secondImage == null) {
            secondImage = firstImage;
        }

        BufferedImage thirdImage = new BufferedImage(secondImage.getWidth(), secondImage.getHeight(), secondImage.getType());

        for (int i = 0; i < secondImage.getWidth(); i++) {
            for (int j = 0; j < secondImage.getHeight(); j++) {
                int newPixel = secondImage.getRGB(i, j);
                int red = 255 - (newPixel >> 16) & 255;
                int blue = 255 - newPixel & 255;
                int green = 255 - (newPixel >> 8) & 255;
                newPixel = ((red << 16) | (green << 8) | blue);
                thirdImage.setRGB(i, j, newPixel);
            }
        }
        return thirdImage;

    }

    public void doubleFilter() {
        thirdImage = doubleF(secondImage);
        repaint();
    }

    private BufferedImage doubleF(BufferedImage secondImage) {
        if (secondImage == null) {
            secondImage = firstImage;
        }
        BufferedImage thirdImage = new BufferedImage(secondImage.getWidth(), secondImage.getHeight(), secondImage.getType());

        for (int i = 0; i < secondImage.getWidth(); i += 2) {
            for (int j = 0; j < secondImage.getHeight(); j += 2) {
                int newPixel = secondImage.getRGB(secondImage.getWidth() / 4 + i / 2, secondImage.getHeight() / 4 + j / 2);
                thirdImage.setRGB(i, j, newPixel);
                if (i + 1 < secondImage.getWidth()) {
                    thirdImage.setRGB(i + 1, j, newPixel);
                }
                if (j + 1 < secondImage.getHeight()) {
                    thirdImage.setRGB(i, j + 1, newPixel);
                }
                if (i + 1 < secondImage.getWidth() && j + 1 < secondImage.getHeight()) {
                    thirdImage.setRGB(i + 1, j + 1, newPixel);
                }
            }
        }
        return thirdImage;
    }

    BufferedImage sobel(BufferedImage secondImage, int threshold) {
        if (secondImage == null) {
            secondImage = firstImage;
        }

        final int[][] matrix_X = {{-1, 0, 1},
                {-2, 1, 2},
                {-1, 0, 1}};

        final int[][] matrix_Y = {{-1, -2, -1},
                {0, 0, 0},
                {1, 2, 1}};

        BufferedImage result_X = multiplyMatrix(secondImage, matrix_X, 1, 0);
        BufferedImage result_Y = multiplyMatrix(secondImage, matrix_Y, 1, 0);

        for (int i = 0; i < secondImage.getWidth(); i++) {
            for (int j = 0; j < secondImage.getHeight(); j++) {
                int res_X = result_X.getRGB(i, j) & 0xFF;
                int res_Y = result_Y.getRGB(i, j) & 0xFF;
                int res = (int) (Math.sqrt(res_X * res_X + res_Y * res_Y) + 0.5);
                int color;
                if (res < threshold) {
                    color = Color.BLACK.getRGB();
                } else {
                    color = Color.WHITE.getRGB();
                }
                result_X.setRGB(i, j, color);
            }
        }
        return result_X;

    }

    public void sobelFilter() {
        thirdImage = sobel(secondImage, threshold);
        repaint();

    }

    public void robertFilter() {
        thirdImage = robert(secondImage, threshold);
        repaint();
    }

    BufferedImage robert(BufferedImage secondImage, int threshold) {
        if (secondImage == null) {
            secondImage = firstImage;
        }
        final int[][] matrix_X = {{0, 0, 0},
                {0, 1, 0},
                {0, 0, -1}};

        final int[][] matrix_Y = {{0, 0, 0},
                {0, 0, 1},
                {0, -1, 0}};

        BufferedImage result_X = multiplyMatrix(secondImage, matrix_X, 1, 0);
        BufferedImage result_Y = multiplyMatrix(secondImage, matrix_Y, 1, 0);

        for (int i = 0; i < secondImage.getWidth(); i++) {
            for (int j = 0; j < secondImage.getHeight(); j++) {
                int res_X = result_X.getRGB(i, j) & 0xFF;
                int res_Y = result_Y.getRGB(i, j) & 0xFF;
                int res = (int) (Math.sqrt(res_X * res_X + res_Y * res_Y) + 0.5);
                int color;
                if (res < threshold) {
                    color = Color.BLACK.getRGB();
                } else {
                    color = Color.WHITE.getRGB();
                }

                result_X.setRGB(i, j, color);
            }
        }
        return result_X;


    }

    public void setRed(int redColor) {
        red = redColor;
    }

    public void setGreen(int greenColor) {
        green = greenColor;
    }

    public void setBlue(int blueColor) {
        green = blueColor;
    }

    public void setSelected(boolean b) {
        System.out.println("set selected");
        selected = b;
    }
}
