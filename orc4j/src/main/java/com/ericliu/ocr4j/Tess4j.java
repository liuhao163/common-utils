package com.ericliu.ocr4j;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.Base64;

public class Tess4j {

    public static void ocrBase64(String base64) throws IOException {
//        File imageFile = new File("C:\\Users\\xxxx\\123.bmp");
        int idx=base64.indexOf(",");
        if (idx >= 0) {
            base64=base64.substring(idx+1);
        }

        byte[] b = Base64.getDecoder().decode(base64);

        ByteArrayInputStream bi = null;
        BufferedInputStream bis = null;
        try {
            bi = new ByteArrayInputStream(b);
            bis = new BufferedInputStream(bi,b.length);
            BufferedImage image = ImageIO.read(bis);
            ITesseract instance = new Tesseract();
            try {
                instance.setLanguage("chi_sim"); //加载语言包
                String result = instance.doOCR(image);
                System.out.println("result：");
                System.out.print(result);
            } catch (TesseractException e) {
                System.out.println(e.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            bi.close();
            bis.close();
        }

    }

    public static void main(String[] args) throws FileNotFoundException {
        File imageFile = new File("/Users/xxx/Desktop/人脸测试专用/WechatIMG373.jpeg");
        byte[] fileByte = null;
        try {
            fileByte = Files.readAllBytes(imageFile.toPath());
            String imgBase64="data:image/png;base64," + Base64.getEncoder().encodeToString(fileByte);
            Tess4j.ocrBase64(imgBase64);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
