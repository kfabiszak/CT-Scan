package machine;

import java.util.Arrays;


public class Normalize {

    private static double maxPixel;
    private static double minPixel;


    public static void normalizeSinogram() {
        int pixels[] = new int[3];
        double hsv[];
        double ww[];

        maxPixel = -255.0;
        minPixel = 255.0;

        for(int i = 0; i < Settings.getnEmi(); i++) {
            for(int j = 0; j < Settings.getnDet(); j++) {
                if(Sinogram.getSinogram()[i][j] < minPixel) {
                    minPixel = Sinogram.getSinogram()[i][j];
                }
                if(Sinogram.getSinogram()[i][j] > maxPixel) {
                    maxPixel = Sinogram.getSinogram()[i][j];
                }
            }
        }

        for(int i = 0; i < Settings.getnEmi(); i++) {
            for(int j = 0; j < Settings.getnDet(); j++) {
                Sinogram.setSinogram(i, j, ((Sinogram.getSinogram()[i][j] - minPixel) / (maxPixel - minPixel)) * 255.0);
                Graphics.getRaster().getPixel(i, j, pixels);
                hsv = Bresenham.rgb2hsv(pixels[0], pixels[1], pixels[2]);
                ww = Bresenham.hsv2rgb(hsv[0], hsv[1], hsv[2]);
                Arrays.fill(ww, Sinogram.getSinogram()[i][j]);
                Sinogram.setrSinogramPix(j, i, ww);
            }
            Sinogram.showSinogram();
        }
    }

    public static void normalizeOutput() {
        int pixels[] = new int[3];
        double hsv[];
        double ww[];

        maxPixel = 0.0;
        minPixel = 255.0;

        for (int i = 0; i < Graphics.getWidth(); i++) {
            for (int j = 0; j < Graphics.getHeight(); j++) {
                double temp = Bresenham.getValues()[i][j];
                if(temp < minPixel) { minPixel = temp; }
                if(temp > maxPixel) { maxPixel = temp; }
            }
        }

        for (int i = 0; i < Graphics.getWidth(); i++) {
            for (int j = 0; j < Graphics.getHeight(); j++) {
                double temp = Bresenham.getValues()[i][j];
                Scan.getRaster().getPixel(i, j, pixels);
                hsv = Bresenham.rgb2hsv(pixels[0], pixels[1], pixels[2]);
                hsv[2] = ((temp - minPixel) / (maxPixel - minPixel)) * 255.0;
                ww = Bresenham.hsv2rgb(hsv[0], hsv[1], hsv[2]);
                Scan.setRaster(i, j, ww);
            }
        }
        Scan.showResult();
    }

}
