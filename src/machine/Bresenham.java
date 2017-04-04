package machine;

import java.util.Arrays;

public class Bresenham {

    private static double[][] values;
    private static int[][] counterOut;


    public static double[] rgb2hsv(double red, double grn, double blu)
    {
        double hue, sat, val;
        double x, f, i;
        double result[] = new double[3];

        x = Math.min(Math.min(red, grn), blu);
        val = Math.max(Math.max(red, grn), blu);
        if (x == val){
            hue = 0;
            sat = 0;
        }
        else
        {
            f = (red == x) ? grn-blu : ((grn == x) ? blu-red : red-grn);
            i = (red == x) ? 3 : ((grn == x) ? 5 : 1);
            hue = ((i-f/(val-x))*60)%360;
            sat = ((val-x)/val);
        }
        result[0] = hue;
        result[1] = sat;
        result[2] = val;
        return result;
    }

    public static double[] hsv2rgb(double hue, double sat, double val)
    {
        double red = 0, grn = 0, blu = 0;
        double i, f, p, q, t;
        double result[] = new double[3];

        if(val==0)
        {
            red = 0;
            grn = 0;
            blu = 0;
        } else
        {
            hue/=60;
            i = Math.floor(hue);
            f = hue-i;
            p = val*(1-sat);
            q = val*(1-(sat*f));
            t = val*(1-(sat*(1-f)));
            if (i==0) {red=val; grn=t; blu=p;}
            else if (i==1) {red=q; grn=val; blu=p;}
            else if (i==2) {red=p; grn=val; blu=t;}
            else if (i==3) {red=p; grn=q; blu=val;}
            else if (i==4) {red=t; grn=p; blu=val;}
            else if (i==5) {red=val; grn=p; blu=q;}
        }
        result[0] = red;
        result[1] = grn;
        result[2] = blu;
        return result;
    }

    //Algorytm Bresenhama - zwraca zsumowana (znormalizowaną) wartosc koloru
    public static double bresenham(int x1, int y1, int x2, int y2, boolean reading, double wValue) {
        // delta of exact value and rounded value of the dependant variable
        int d = 0;

        int dy = Math.abs(y2 - y1);
        int dx = Math.abs(x2 - x1);

        int dy2 = (dy << 1); // slope scaling factors to avoid floating
        int dx2 = (dx << 1); // point

        int ix = x1 < x2 ? 1 : -1; // increment direction
        int iy = y1 < y2 ? 1 : -1;


        int value = 0;
        int points = 0;

        if (dy <= dx) {
            for (;;) {
                if(reading) {
                    value = readRaster(x1, y1, value);
                    points++;
                } else {
                    writeRaster(x1, y1, wValue);
                }
                if (x1 == x2)
                    break;
                x1 += ix;
                d += dy2;
                if (d > dx) {
                    y1 += iy;
                    d -= dx2;
                }
            }
        } else {
            for (;;) {
                if(reading) {
                    value = readRaster(x1, y1, value);
                    points++;
                } else {
                    writeRaster(x1, y1, wValue);
                }
                if (y1 == y2)
                    break;
                y1 += iy;
                d += dx2;
                if (d > dy) {
                    x1 += ix;
                    d -= dy2;
                }
            }
        }

        if(reading) {
            return value / points;
        } else {
            return 0;
        }
    }

    private static int readRaster(int x1, int y1, int value) {
        int pixels[] = new int[3];
        double hsv[];

        Graphics.getRaster().getPixel(x1, y1, pixels);
        hsv = rgb2hsv(pixels[0], pixels[1], pixels[2]);
        value += hsv[2];

        return value;
    }

    private static void writeRaster(int x1, int y1, double value) {
        int pixels[] = new int[3];
        double hsv[];
        double ww[];

        Scan.getRaster().getPixel(x1, y1, pixels);
        hsv=rgb2hsv(pixels[0], pixels[1], pixels[2]);
        ww = hsv2rgb(hsv[0], hsv[1], hsv[2]);
//        ww[0] += value; //TODO drugi sposob (trzeba dzielic przez jakas stala przy wywolyaniu breneshama)
        counterOut[x1][y1] += 1;
        values[x1][y1] += value;
        if(counterOut[x1][y1] != 0) {
            ww[0] = values[x1][y1] / counterOut[x1][y1];
        } else {
            ww[0] = values[x1][y1];
        }
        Arrays.fill(ww, ww[0]);
        Scan.setRaster(x1, y1, ww);
    }

    public static void setValues() {
        Bresenham.values = new double[Graphics.getWidth()][Graphics.getHeight()];
    }

    public static void setCounterOut() {
        Bresenham.counterOut = new int[Graphics.getWidth()][Graphics.getHeight()];
    }

    public static double[][] getValues() {
        return values;
    }
}