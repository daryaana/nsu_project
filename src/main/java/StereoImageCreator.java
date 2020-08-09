import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public class StereoImageCreator {


 public BufferedImage generateStereoPicture(BufferedImage bitmapMask, BufferedImage stereoImg)
 {
     // Переводим маску в массив сдвигов
     int w = bitmapMask.getWidth();
     int h = bitmapMask.getHeight();
     int[][] mask = new int[w][];
     for (int x = 0; x < w; x++)
     {
         mask[x] = new int[h];
         for (int y = 0; y < h; y++) {
             int rgb = bitmapMask.getRGB(x,y);
             int red = (rgb >> 16) & 0xFF;
             mask[x][y] = red / 32;
         }
     }

     // Cоздаем фон
     int s = 100;

     // Сдвигаем каждый пиксел
     for (int y = 0; y < h; y++)
         for (int x = 0; x < w; x++)
             if (mask[x][y] > 0)
             {
                 int pixel = stereoImg.getRGB(x + mask[x][y], y);
                 for (int i = x + s; i < w + s; i += s)
                     stereoImg.setRGB(i, y, pixel);
             }

     return stereoImg;
 }
}

