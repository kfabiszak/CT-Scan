package machine;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;


public class Scan {

    private static Image iResult; //obraz wygenerowany z sinogramu
    private static Image iNormalizedResult; //znormalizowany obraz wyjściowy
    private static BufferedImage result; //obraz wygenerowany z sinogramu
    private static WritableRaster rRaster; //raster obrazu wygenerowanego z sinogramu

    private static List<Image> stages;
    private static boolean generated = false;

    public static void scan() {
        Sinogram.generateSinogram();

        result = new BufferedImage(Graphics.getWidth(), Graphics.getHeight(), BufferedImage.TYPE_INT_RGB);
        rRaster = result.getRaster();
        stages = new ArrayList<>();
        List<Number> helper = new ArrayList<>();
        for (int i = 0; i < Settings.getnEmi(); i++) {
            helper.add(i, (int) (i * Math.floor(Settings.getnEmi() / 10)));
        }

        int[][] emdet = Sinogram.getEmdet();
        double[][] sinogram = Sinogram.getSinogram();

        for (int i = 0; i < Settings.getnEmi(); i++) {
            for (int j = 0; j < Settings.getnDet(); j++) {
                Bresenham.bresenham(emdet[i][2 * j + 2], emdet[i][2 * j + 3], emdet[i][0], emdet[i][1], false, sinogram[i][j]);
            }
            iResult = SwingFXUtils.toFXImage(result, null);
            Main.getController().showResult(iResult);

            if(stages.size() < 10) {
                if(helper.contains(i)) {
                    stages.add(iResult);
                }
            } else if(i == Settings.getnEmi() - 1) {
                stages.add(iResult);
                generated = true;
            }
        }
//        iResult = SwingFXUtils.toFXImage(result, null);
//        Main.getController().showResult(iResult);
        Normalize.normalizeOutput();
    }

    public static void showResult() {
        iNormalizedResult = SwingFXUtils.toFXImage(result, null);
        Main.getController().showNormalizedResult(iNormalizedResult);
    }

    public static void setRaster(int i, int j, double[] ww) {
        Scan.rRaster.setPixel(i, j, ww);
    }

    public static WritableRaster getRaster() {
        return rRaster;
    }

    public static Image getiResult() {
        return iResult;
    }

    public static List<Image> getStages() {
        return stages;
    }

    public static boolean isGenerated() {
        return generated;
    }

    public static BufferedImage getResult() {
        return result;
    }

    public static Image getiNormalizedResult() {
        return iNormalizedResult;
    }
}
