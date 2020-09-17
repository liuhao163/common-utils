package com.ericliu.ocr4j;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.opencv.highgui.Highgui.imencode;
import static org.opencv.highgui.Highgui.imread;

public class BlindWatermarkFetcher {

    private static byte[] getWaterImage(String path) {
        Mat img = imread(path);

        // Creating an empty matrix
        Mat equ = new Mat();
        img.copyTo(equ);

        // Applying blur
        Imgproc.blur(equ, equ, new Size(3, 3));

        // Applying color
        Imgproc.cvtColor(equ, equ, Imgproc.COLOR_BGR2YCrCb);
        List<Mat> channels = new ArrayList<Mat>();

        // Splitting the channels
        Core.split(equ, channels);

        // Equalizing the histogram of the image
        Imgproc.equalizeHist(channels.get(0), channels.get(0));
        Core.merge(channels, equ);
        Imgproc.cvtColor(equ, equ, Imgproc.COLOR_YCrCb2BGR);

        Mat gray = new Mat();
        Imgproc.cvtColor(equ, gray, Imgproc.COLOR_BGR2RGBA);  //COLOR_BGR2GRAY
        Mat grayOrig = new Mat();
        Imgproc.cvtColor(img, grayOrig, Imgproc.COLOR_BGR2RGBA);

        //Imgcodecs.imwrite("E:/OpenCV/chap20/histo_output.jpg", equ);
//        HighGui.imshow( "Equalized Image", equ );
//        Imgcodecs.imwrite("C:/Users/zhangbingbo/Desktop//histo_output.jpg", equ);

        return Mat2Image(equ, ".jpg");
    }

    public static byte[] Mat2Image(Mat matrix, String fileExtension) {
        MatOfByte mob = new MatOfByte();
        imencode(fileExtension, matrix, mob);
        // convert the "matrix of bytes" into a byte array
//        byte[] byteArray = mob.toArray();
//        BufferedImage bufImage = null;
//        try {
//            InputStream in = new ByteArrayInputStream(byteArray);
//            bufImage = ImageIO.read(in);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return mob.toArray();
    }

    public static void main(String[] args) throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        byte[] b = getWaterImage(args[0]);

        FileOutputStream f = new FileOutputStream(new File(args[1]));
        f.write(b, 0, b.length);
        System.out.println();
    }
}
