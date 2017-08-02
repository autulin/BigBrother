import com.github.sarxos.webcam.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by jolin on 2017/7/27.
 */
public class Motion implements WebcamMotionListener{

    private Webcam webcam;
    private WebcamMotionDetector detector;
//    private Dimension[] nonStandardResolutions = new Dimension[] {
//            WebcamResolution.PAL.getSize(),
//            WebcamResolution.HD720.getSize(),
//            new Dimension(2000, 1000),
//            new Dimension(1000, 500),
//    };

    public Motion() {
        Webcam.setDriver(new IpCamDriver(new IpCamStorage("src/main/resources/cameras.xml")));
        webcam = Webcam.getDefault();
//        webcam.setCustomViewSizes(nonStandardResolutions);
//        webcam.setViewSize(WebcamResolution.HD720.getSize());
        detector = new WebcamMotionDetector(webcam);
        detector.setInterval(5000);
        detector.addMotionListener(this);
        detector.start();
    }


    @Override
    public void motionDetected(WebcamMotionEvent wme) {
        try {
            ImageIO.write(webcam.getImage(), "PNG", new File(new Date().getTime() + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        new Motion();
        System.in.read(); // keep program open
    }
}
