package image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A package-private class of the package image.
 * @author Dan Nirel
 */
public class Image {

    private final ArrayList<Color[][]> allSubImages = new ArrayList<>();
    private final Color[][] pixelArray;
    private final int width;
    private final int height;

    /**
     * constructor with input String filename of image
     * @param filename the input file name.
     * @throws IOException .
     */

    public Image(String filename) throws IOException {
        BufferedImage im = ImageIO.read(new File(filename));
        width = im.getWidth();
        height = im.getHeight();
        pixelArray = new Color[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixelArray[i][j]=new Color(im.getRGB(j, i));
            }
        }
    }

    /**
     * constructor gets pixel array contain pixels of the image and width and height

     * @param pixelArray 2D array that contains the colors.
     * @param width pixel array width.
     * @param height pixel array height.
     */
    public Image(Color[][] pixelArray, int width, int height) {
        this.pixelArray = pixelArray;
        this.width = width;
        this.height = height;
    }

    /**
     * function that gets the width
     * @return the width of the picture.
     */
    public int getWidth() {
        return width;
    }

    /**
     * function that gets the height
     * @return the height of the picture.
     */

    public int getHeight() {
        return height;
    }

    /**
     * function gets the pixel
     * @param x row index.
     * @param y col index.
     * @return the pixel array.
     */

    public Color getPixel(int x, int y) {
        return pixelArray[x][y];
    }

    /**
     * function gets String filename and save it according to the pixels of the image
     * @param fileName the input file name.
     */
    public void saveImage(String fileName){
        // Initialize BufferedImage, assuming Color[][] is already properly populated.
        BufferedImage bufferedImage = new BufferedImage(pixelArray[0].length, pixelArray.length,
                BufferedImage.TYPE_INT_RGB);
        // Set each pixel of the BufferedImage to the color from the Color[][].
        for (int x = 0; x < pixelArray.length; x++) {
            for (int y = 0; y < pixelArray[x].length; y++) {
                bufferedImage.setRGB(y, x, pixelArray[x][y].getRGB());
            }
        }
        File outputfile = new File(fileName+".jpeg");
        try {
            ImageIO.write(bufferedImage, "jpeg", outputfile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * this function every sub image brightness according to the 2D pixel array that contains Colors
     * @param pixelArr the pixel array.
     * @return the grey Scale Degree.
     */
    public double calculateSubImageBrightness(Color[][] pixelArr){
        double  greyScaleDegree = 0;
            for (int i = 0; i < pixelArr.length; i++) {
                for (int j = 0; j < pixelArr[0].length; j++) {
                    greyScaleDegree += greyCalculator(pixelArr[i][j]);
                }
            }
        return greyScaleDegree / ((double)(pixelArr.length*pixelArr[0].length)*(double)255);
    }

    /**
     * this function gets color and returns the greyScale of the color according of the equation
     * @param color an arrays that contains three degrees for the colors RED, GREEN, BLUE.
     * @return it returns the degree according to the given equation.
     */
    private double greyCalculator(Color color){
        double REDTOGREYPDF = 0.2126;
        double GREENTOGREYPDF = 0.7152;
        double BLUETOGREYPDF = 0.0722;
        return (double) (color.getRed()) * REDTOGREYPDF +
                (double)(color.getGreen()) * GREENTOGREYPDF +
                (double)(color.getBlue()) * BLUETOGREYPDF;
    }

    /**
     * this function gets the data structure that contain all sub images
     * @return Array List that contains all 2D picture Colors.
     */

    public ArrayList<Color[][]>  getAllSubImages() {
        return allSubImages;
    }

    /**
     * this function split image to sub images with resolution size
     * @param image the picture
     * @param resolution resolution
     * @return Array List that contains all 2D picture Colors.
     */
    public ArrayList<Color[][]> splitToSubImg(Image image, int resolution){
        ArrayList<Color[][]> arrayList = new ArrayList<>();
        int subImageSizeHeight =  image.getHeight() /resolution;
        int subImageSizeWidth =  image.getWidth() /resolution;
        for (int i = 0; i < image.getHeight() ; i+=subImageSizeHeight) {
            for (int j = 0; j < image.getWidth(); j+= subImageSizeWidth) {
                Color[][] subImages = new Color[subImageSizeHeight][subImageSizeWidth];
                for (int l = 0; l < subImageSizeHeight ; l++) {
                    for (int k = 0; k < subImageSizeWidth ; k++) {
                        subImages[l][k] = image.getPixel(i+l, j+k);
                    }
                }
                arrayList.add(subImages);
            }
        }
        return arrayList;
    }

}
