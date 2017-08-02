package com.autulin;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamMotionDetector;
import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.*;

@Component
public class WebCamManager implements WebcamMotionListener {
    private Logger logger = LoggerFactory.getLogger(WebCamManager.class);
    private Webcam webcam;
    private WebcamMotionDetector detector;

    private List<Picture> pictures = new LinkedList<>();

    private static int MAX_LENGTH = 20; //节省内存，只保存20张

    private WebCamManager() {
        webcam = Webcam.getDefault();
        webcam.open();
        detector = new WebcamMotionDetector(webcam);
        detector.setInterval(5000);
        detector.addMotionListener(this);
        detector.start();
//        pictures.add(new Picture(new Date(), "123123"));
//        pictures.add(new Picture(new Date(), "123123"));
//        pictures.add(new Picture(new Date(), "123123"));
//        pictures.add(new Picture(new Date(), "123123"));
    }

    public void closeWebCam() {
        webcam.close();
    }

    @Override
    public void motionDetected(WebcamMotionEvent wme) {
        logger.debug("detected motion");
        if (pictures.size() > MAX_LENGTH) { //lazy
            pictures.remove(MAX_LENGTH - 1);
        }
        pictures.add(0, getOnePicture());
    }

    public List<Picture> getPictures() {
        return pictures;
    }
    public void clearPictures() {
        pictures.clear();
    }

    public Picture getOnePicture() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(webcam.getImage(), "JPG", baos);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        String base64 = null;
        try {
            base64 = new String(Base64.getEncoder().encode(baos.toByteArray()), "UTF8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }
        return new Picture(new Date(), base64);
    }
}
