package machine;

import org.dcm4che2.data.*;
import org.dcm4che2.io.DicomOutputStream;
import org.dcm4che2.util.UIDUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DICOM {

    private static File savingp;
    private static File savingj;

    public static void generateDICOM() {
        savingp = new File(Integer.toString(Info.getPatientID()) + ".png");
        savingj = new File(Integer.toString(Info.getPatientID()) + ".jpg");
        if(Scan.isGenerated()) {
            try {
                ImageIO.write(Scan.getResult(), "png", savingp);
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedImage bufferedImage;
            try {
                bufferedImage = ImageIO.read(savingp);
                BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
                        bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
                newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
                ImageIO.write(newBufferedImage, "jpg", savingj);
            } catch (IOException e) {
                e.printStackTrace();
            }
            File dcmDestination = new File(Integer.toString(Info.getPatientID()) + ".dcm");
            try {
                BufferedImage jpegImage = ImageIO.read(savingj);
                if (jpegImage == null)
                    throw new Exception("Invalid file.");
                int colorComponents = jpegImage.getColorModel().getNumColorComponents();
                int bitsPerPixel = jpegImage.getColorModel().getPixelSize();
                int bitsAllocated = (bitsPerPixel / colorComponents);
                int samplesPerPixel = colorComponents;

                DicomObject dicom = new BasicDicomObject();

                String oldstring = Info.getBirthDate() + " 00:00:00.0";
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(oldstring);

                dicom.putString(Tag.StudyComments, VR.LT, Info.getDate());
                dicom.putDate(Tag.StudyDate, VR.DA, new Date());
                dicom.putString(Tag.PatientSex, VR.CS, Info.getSex());
                dicom.putDate(Tag.PatientBirthDate, VR.DA, date);
                dicom.putString(Tag.PatientName, VR.CS, Info.getName() + " " + Info.getSurname());
                dicom.putString(Tag.SpecificCharacterSet, VR.CS, "ISO_IR 100");
                dicom.putString(Tag.PhotometricInterpretation, VR.CS, samplesPerPixel == 3 ? "MONOCHROME2" : "MONOCHROME2");
                dicom.putInt(Tag.SamplesPerPixel, VR.US, samplesPerPixel);
                dicom.putInt(Tag.Rows, VR.US, jpegImage.getHeight());
                dicom.putInt(Tag.Columns, VR.US, jpegImage.getWidth());
                dicom.putInt(Tag.BitsAllocated, VR.US, bitsAllocated);
                dicom.putInt(Tag.BitsStored, VR.US, bitsAllocated);
                dicom.putInt(Tag.HighBit, VR.US, bitsAllocated-1);
                dicom.putInt(Tag.PixelRepresentation, VR.US, 0);
                dicom.putDate(Tag.InstanceCreationDate, VR.DA, new Date());
                dicom.putDate(Tag.InstanceCreationTime, VR.TM, new Date());
                dicom.putString(Tag.StudyInstanceUID, VR.UI, UIDUtils.createUID());
                dicom.putString(Tag.SeriesInstanceUID, VR.UI, UIDUtils.createUID());
                dicom.putString(Tag.SOPInstanceUID, VR.UI, UIDUtils.createUID());
                dicom.initFileMetaInformation(UID.JPEGBaseline1);
                FileOutputStream fos = new FileOutputStream(dcmDestination);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                DicomOutputStream dos = new DicomOutputStream(bos);
                dos.writeDicomFile(dicom);
                dos.writeHeader(Tag.PixelData, VR.OB, -1);
                dos.writeHeader(Tag.Item, null, 0);
                int jpgLen = (int) savingj.length();
                dos.writeHeader(Tag.Item, null, (jpgLen+1)&~1);
                FileInputStream fis = new FileInputStream(savingj);
                BufferedInputStream bis = new BufferedInputStream(fis);

                DataInputStream dis = new DataInputStream(bis);

                byte[] buffer = new byte[65536];
                int b;
                while ((b = dis.read(buffer)) > 0) {
                    dos.write(buffer, 0, b);
                }
                if ((jpgLen&1) != 0) dos.write(0);

                dos.writeHeader(Tag.SequenceDelimitationItem, null, 0);
                dos.close();
            } catch(Exception e) {
                System.out.println("ERROR: "+ e.getMessage());
            }
        }
    }

}
