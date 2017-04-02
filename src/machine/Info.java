package machine;

import java.time.LocalDate;


public class Info {

    //Pacient
    private static int patientID = 0;
    private static String name;
    private static String surname;
    private static String sex;
    private static LocalDate birthDate;

    //Scan info
    private static String date;
    private static String comment;

    public static int getPatientID() {
        return patientID;
    }

    public static void nextPatient() {
        Info.patientID += 1;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        Info.name = name;
    }

    public static String getSurname() {
        return surname;
    }

    public static void setSurname(String surname) {
        Info.surname = surname;
    }

    public static String getDate() {
        return date;
    }

    public static void setDate(String date) {
        Info.date = date;
    }

    public static String getComment() {
        return comment;
    }

    public static void setComment(String comment) {
        Info.comment = comment;
    }

    public static LocalDate getBirthDate() {
        return birthDate;
    }

    public static void setBirthDate(LocalDate birthDate) {
        Info.birthDate = birthDate;
    }

    public static String getSex() {
        return sex;
    }

    public static void setSex(String sex) {
        Info.sex = sex;
    }
}
