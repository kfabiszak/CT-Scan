package machine;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import java.io.File;


public class Graphics {

    private static WritableRaster raster = null;
    private static int width = 0; //szerokość obrazu
    private static int height = 0; //wysokość obrazu
    private static double r; //promień

    private static Image image; //obraz wejściowy
    private static BufferedImage bImg;

    private static boolean loaded; //przechowujące informację czy obraz został wczytany

    public static void initImage(File file) {
        if (file != null) {
            image = new Image(file.toURI().toString());
            bImg = SwingFXUtils.fromFXImage(image, null);

            raster = bImg.getRaster();
            width = raster.getWidth();
            height = raster.getHeight();

            if (width == height) {
                r = width / 2 - 1;

                Main.getController().showInput(image);

                Brenesham.setCounterOut();
                Brenesham.setValues();
                Sinogram.initSinogram();
                Settings.setDelta();

                Main.getController().generateSinogram();

                loaded = true;

            } else {
                Main.getController().notSquare();
            }
        } else {
            Main.getController().chooseAgain();
        }
    }

    public static boolean isLoaded() {
        return loaded;
    }


    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static WritableRaster getRaster() {
        return raster;
    }

    public static double getR() {
        return r;
    }

    public static Image getImage() {
        return image;
    }
}
