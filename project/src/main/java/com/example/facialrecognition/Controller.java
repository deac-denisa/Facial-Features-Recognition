package com.example.facialrecognition;

import javafx.fxml.FXML;
import javafx.scene.image.*;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import java.io.ByteArrayInputStream;


public class Controller {

    @FXML
    ImageView camView;

    CascadeClassifier faceCascade;
    CascadeClassifier eyeCascade ;
    CascadeClassifier noseCascade ;
    CascadeClassifier mouthCascade ;

    Scalar color1 = new Scalar(293, 71, 111);   //purple
    Scalar color2 = new Scalar(102, 209, 255);  //yellow
    Scalar color3 = new Scalar(160, 214, 6);    //green
    Scalar color4 = new Scalar(178, 138, 17);   //blue

    VideoCapture cap ;
    Mat frame ;

    Image myImag;
    @FXML
    protected void inputPhotoButtonClick() {
        System.out.println("input Photo");
        frame = Imgcodecs.imread("D:\\an3\\SCS\\FacialRecognition\\src\\main\\resources\\com\\example\\facialrecognition/imag1.png");
        processFrame(frame);

    }

    @FXML
    protected void takePhotoButtonClick() {
        System.out.println("take photo");
        System.out.println("Open Camera");
        frame = new Mat();

        cap = new VideoCapture(0);

        if (!cap.isOpened()) {
            System.out.println("No cam found");
            return;
        }
        else{
            System.out.println("Camera is Opened - Photo Taken");
        }

        cap.read(frame);
        cap.release();
        processFrame(frame);
    }


    private void initClassifiers() {
        faceCascade = new CascadeClassifier();
        eyeCascade = new CascadeClassifier();
        noseCascade = new CascadeClassifier();
        mouthCascade = new CascadeClassifier();

        String xmlFace = "D:\\programeee\\OpenCV\\opencv\\sources\\data\\lbpcascades/lbpcascade_frontalface.xml";
        String xmlEyes = "D:\\programeee\\OpenCV\\opencv\\sources\\data\\haarcascades/haarcascade_eye.xml";
        String xmlMouth = "D:\\programeee\\OpenCV\\opencv\\sources\\data\\haarcascades/mouth.xml";
        String xmlNose = "D:\\programeee\\OpenCV\\opencv\\sources\\data\\haarcascades/nose.xml";

        if (!faceCascade.load(xmlFace)) {
            System.out.println("could not load haarcascade_frontalface_alt.xml");
            return;
        }
        if (!eyeCascade.load(xmlEyes)) {
            System.out.println("could not load haarcascade_eye_tree_eyeglasses.xml");
            return;
        }
        if (!noseCascade.load(xmlNose)) {
            System.out.println("could not load nose.xml");
            return;
        }
        if (!mouthCascade.load(xmlMouth)) {
            System.out.println("could not load mouth.xml");
            return;
        }
    }

    private void processFrame(Mat frame){

        initClassifiers();

        Mat gray = new Mat();
        Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(gray, gray);

        MatOfRect faces = new MatOfRect();
        faceCascade.detectMultiScale(gray, faces, 1.1, 4, 0, new Size(100, 100));


        for (Rect r : faces.toArray()) {
            //FACE DETECTION CODE
            Imgproc.rectangle(
                    frame, new Point(r.x, r.y), new Point(r.x + r.width, r.y + r.height), color1, 3);
            Mat face = new Mat(frame, r);

            //EYE DETECTION CODE
            MatOfRect eyes = new MatOfRect();
            eyeCascade.detectMultiScale(face, eyes, 1.1, 3, 0);

            for (Rect e : eyes.toArray()) {
                Imgproc.rectangle(frame, new Point(r.tl().x + e.tl().x, r.tl().y + e.tl().y), new Point(r.tl().x + e.br().x, r.tl().y + e.br().y), color2, 2);
            }

            //NOSE DETECTION CODE
            MatOfRect nose = new MatOfRect();
            noseCascade.detectMultiScale(face, nose, 1.1, 5, 0);

            if (nose.toArray().length == 1) {
                for (Rect n : nose.toArray()) {
                    Imgproc.rectangle(frame, new Point(r.tl().x + n.tl().x, r.tl().y + n.tl().y), new Point(r.tl().x + n.br().x, r.tl().y + n.br().y), color3, 2);
                }
            }

            //MOUTH DETECTION CODE
            MatOfRect mouth = new MatOfRect();
            mouthCascade.detectMultiScale(face, mouth, 1.5, 5, 0);

            if (mouth.toArray().length == 1) {
                for (Rect m : mouth.toArray()) {
                    Imgproc.rectangle(frame, new Point(r.tl().x + m.tl().x, r.tl().y + m.tl().y), new Point(r.tl().x + m.br().x, r.tl().y + m.br().y), color4, 2);
                }
            }
        }
        System.out.println("Image Processed");


    }


    @FXML
    public void showPhotoButton() {

        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode("output2.png", frame, buffer);
        myImag = new Image(new ByteArrayInputStream(buffer.toArray()));
        camView.setImage(myImag);
        System.out.println("should see photo");
    }

    public void stop(){
        cap.release();
    }
}