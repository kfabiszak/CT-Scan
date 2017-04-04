package machine;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Arrays;

public class Sinogram {

    private static BufferedImage bSinogram;
    private static WritableRaster rSinogram;
    private static Image iSinogram;

    private static int[][] emdet;
    private static double[][] sinogram;

    public static void showSinogram() {
        iSinogram = SwingFXUtils.toFXImage(bSinogram, null);
        Main.getController().showSinogram(iSinogram);
    }

    public static void generateSinogram() {
        int x1, y1; //wspolrzedne emitera
        int x2, y2; //wspolrzedne detektora
        double rRange = Settings.getRangeInRad();
        double alfa = Settings.getDelta();
        double r = Graphics.getR();

        bSinogram = new BufferedImage(Settings.getnDet(), Settings.getnEmi(), BufferedImage.TYPE_INT_RGB);
        rSinogram = bSinogram.getRaster();

        int pixels[] = new int[3];
        double hsv[];
        double ww[];

        for(int i = 0; i < Settings.getnEmi(); i++) {
            x1 = (int) (r * Math.cos(i * alfa)) + (int) r;
            y1 = (int) (r * Math.sin(i * alfa)) + (int) r;

            emdet[i][0] = x1;
            emdet[i][1] = y1;

            for(int j = 0; j < Settings.getnDet(); j++) {
                x2 = (int) (r * Math.cos(i * alfa + Math.PI - (rRange / 2) + (j * rRange / (Settings.getnDet() - 1)))) + (int) r;
                y2 = (int) (r * Math.sin(i * alfa + Math.PI - (rRange / 2) + (j * rRange / (Settings.getnDet() - 1)))) + (int) r;

                emdet[i][2 * j + 2] = x2;
                emdet[i][2 * j + 3] = y2;
                sinogram[i][j] = Bresenham.bresenham(x1, y1, x2, y2, true, 0);

                Graphics.getRaster().getPixel(i, j, pixels);
                hsv = Bresenham.rgb2hsv(pixels[0], pixels[1], pixels[2]);
                ww = Bresenham.hsv2rgb(hsv[0], hsv[1], hsv[2]);
                Arrays.fill(ww, sinogram[i][j]);
                rSinogram.setPixel(j, i, ww);

            }

            iSinogram = SwingFXUtils.toFXImage(bSinogram, null);
            Main.getController().showSinogram(iSinogram);

        }

//        iSinogram = SwingFXUtils.toFXImage(bSinogram, null);
//        Main.getController().showSinogram(iSinogram);
        Normalize.normalizeSinogram();

    }

    public static void setiSinogram(Image iSinogram) {
        Sinogram.iSinogram = iSinogram;
    }

    public static double[][] getSinogram() {
        return sinogram;
    }

    public static void setSinogram(int i, int j, double value) {
        Sinogram.sinogram[i][j] = value;
    }

    public static void initSinogram() {
        Sinogram.sinogram = new double[Settings.getnEmi()][Settings.getnDet()];
        Sinogram.emdet = new int[Settings.getnEmi()][2 + 2 * Settings.getnDet()];
    }

    public static void setrSinogramPix(int j, int i, double[] ww) {
        rSinogram.setPixel(j, i, ww);
    }

    public static int[][] getEmdet() {
        return emdet;
    }

    public static Image getiSinogram() {
        return iSinogram;
    }

}
