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
//        double filtered[][] = new double[Settings.getnEmi()][Settings.getnDet()];
//        for(int i = 0; i < Settings.getnEmi(); i++) {
//            for(int j = 0; j < Settings.getnDet(); j++) {
//                for(int k = 0; k < Settings.getnDet(); k++) {
//                    if(k != j) {
//                        if (k % 2 == 1) {
//                            filtered[i][j] += Sinogram.getSinogram()[i][k] * ((-4 / Math.PI) / (k * k));
////
//                        }
//                    } else {
//                        filtered[i][j] = Sinogram.getSinogram()[i][j];
//                    }
//                    System.out.println(filtered[i][j]);
//                }
//            }
//        }
//
//        for(int i = 0; i < Settings.getnEmi(); i++) {
//            for(int j = 0; j < Settings.getnDet(); j++) {
//                if(filtered[i][j] < minPixel) {
//                    minPixel = filtered[i][j];
//                }
//                if(filtered[i][j] > maxPixel) {
//                    maxPixel = filtered[i][j];
//                }
//            }
//            System.out.println(minPixel);
//        }
//
//        for(int i = 0; i < Settings.getnEmi(); i++) {
//            for(int j = 0; j < Settings.getnDet(); j++) {
//                filtered[i][j] = ((filtered[i][j] - minPixel) / (maxPixel - minPixel)) * 255.0;
//                Graphics.getRaster().getPixel(i, j, pixels);
//                hsv = Brenesham.rgb2hsv(pixels[0], pixels[1], pixels[2]);
//                ww = Brenesham.hsv2rgb(hsv[0], hsv[1], hsv[2]);
//                Arrays.fill(ww, filtered[i][j]);
//                Sinogram.setrSinogramPix(j, i, ww);
//            }
//            Sinogram.showSinogram();
//        }

        for(int i = 0; i < Settings.getnEmi(); i++) {
            for(int j = 0; j < Settings.getnDet(); j++) {
                Sinogram.setSinogram(i, j, ((Sinogram.getSinogram()[i][j] - minPixel) / (maxPixel - minPixel)) * 255.0);
                Graphics.getRaster().getPixel(i, j, pixels);
                hsv = Brenesham.rgb2hsv(pixels[0], pixels[1], pixels[2]);
                ww = Brenesham.hsv2rgb(hsv[0], hsv[1], hsv[2]);
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
                double temp = Brenesham.getValues()[i][j];
                if(temp < minPixel) { minPixel = temp; }
                if(temp > maxPixel) { maxPixel = temp; }
            }
        }

        for (int i = 0; i < Graphics.getWidth(); i++) {
            for (int j = 0; j < Graphics.getHeight(); j++) {
                double temp = Brenesham.getValues()[i][j];
                Scan.getRaster().getPixel(i, j, pixels);
                hsv = Brenesham.rgb2hsv(pixels[0], pixels[1], pixels[2]);
                hsv[2] = ((temp - minPixel) / (maxPixel - minPixel)) * 255.0;
                ww = Brenesham.hsv2rgb(hsv[0], hsv[1], hsv[2]);
                Scan.setRaster(i, j, ww);
            }
        }
        Scan.showResult();
    }

}
