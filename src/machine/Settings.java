package machine;


public class Settings {

    private static int nDet = 100; //liczba detektorow
    private static int range = 250; //rozwartosc detektorów
    private static int nEmi = 180; //liczba skanów
    private static double delta; //przyrost kąta


    public static int getnDet() {
        return nDet;
    }

    public static void setnDet(int nDet) {
        Settings.nDet = nDet;
    }

    public static int getRange() {
        return range;
    }

    public static void setRange(int range) {
        Settings.range = range;
    }

    public static int getnEmi() {
        return nEmi;
    }

    public static void setnEmi(int nEmi) {
        Settings.nEmi = nEmi;
    }

    public static double getDelta() {
        return delta;
    }

    public static void setDelta() {
        Settings.delta = (2*Math.PI) / nEmi;
    }

    public static double getRangeInRad() {
        return Math.toRadians(range);
    }

}
