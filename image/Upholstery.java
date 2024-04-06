package image;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * this class is for upholstery
 */
public class Upholstery {

    private int newWidth;
    private int newHeight;
    private final Color[][] pixelArray;
    /**
     * this is the image field (composition between the Upholstery and the image).
     */
    public Image newImage;

    /**
     * constructor for Upholstery
     */
    public Upholstery(Image image) {
        int[] imageSizes = new int[]{image.getHeight(), image.getWidth()};
        boolean powerTwo = isPowerOfTwo(imageSizes[0]) && isPowerOfTwo(imageSizes[1]);
        
        if (powerTwo){
            pixelArray = originalImage(image, imageSizes);
        }
        else {
            newHeight = imageSizes[0];
            newWidth = imageSizes[1];
            getPowerOfTwo(imageSizes);
            pixelArray = new Color[newHeight][newWidth];
            upholsteryFunc(image, imageSizes);
            newImage = new Image(pixelArray, newWidth, newHeight);
        }
    }

    /**
     *
     * @param image the original image.
     * @param imageSizes the original image sizes.
     *                   in this function we apply the Upholstery.
     */
    private void upholsteryFunc(Image image, int[] imageSizes) {
        int eachSideCols = (newWidth - imageSizes[1]) / 2;
        int eachSideRows = (newHeight - imageSizes[0]) / 2;
        int counter1 = 0;
        int counter2 = 0;
        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                pixelArray[i][j] = Color.WHITE;
            }
        }
        for (int i = eachSideRows; i < newHeight - eachSideRows; i++) {
            for (int j = eachSideCols; j < newWidth - eachSideCols; j++) {
                pixelArray[i][j] = image.getPixel(counter1, counter2);
                counter2++;
            }
            counter1++;
            if (counter2 == imageSizes[1]){
                counter2=0;
            }
            if(counter1== imageSizes[0]){
                break;
            }
        }
    }

    /**
     *
     * @param imageSizes checks if the sizes are not power of two and gets the
     *                   new closest power of 2 number.
     */
    private void getPowerOfTwo(int[] imageSizes) {
        if(!isPowerOfTwo(imageSizes[1])){
            newWidth = getNextPowerOfTwo(imageSizes[1]);

        }
        if(!isPowerOfTwo(imageSizes[0])){
            newHeight = getNextPowerOfTwo(imageSizes[0]);
        }
    }

    /**
     *
     * @param image the original image.
     * @param imageSizes the original image sizes.
     * @return A 2D array that contains the colors of the picture.
     */
    private Color[][] originalImage(Image image, int[] imageSizes) {
        final Color[][] pixelArray;
        newHeight = imageSizes[0];
        newWidth = imageSizes[1];
        pixelArray = new Color[imageSizes[0]][imageSizes[1]];
        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                pixelArray[i][j] = image.getPixel(i,j);
            }
        }
        newImage = new Image(pixelArray, newWidth, newHeight);
        return pixelArray;
    }

    /**
     * this function checks if number is power of two
     * @param num number we have to check
     * @return true if the number is power of two false else
     */
    private boolean isPowerOfTwo(int num){
        if(num<=0){
            return false;
        }
        while(num>1){
            if(num%2 != 0){
                return false;
            }
            num /= 2;
        }
        return true;
    }

    /**
     * gets the next power of two
     *
     * @param n number we want to check the next of it
     * @return the next power of two number
     */
    private int getNextPowerOfTwo(int n) {
        return (int) Math.pow(2, Math.ceil(Math.log(n) / Math.log(2)));

    }

    /**
     * this function returns the new pixel array
     * @return 2D array that contains colors.
     */
    public Color[][] getPixelArray() {
        return pixelArray;
    }

    /**
     * this function returns the new image
     * @return new image after the Upholstery.
     */
    public Image getNewImage() {
        return newImage;
    }

    /**
     * gets the new height
     * @return the new-Height of the Upholstery.
     */
    public int getNewHeight() {
        return newHeight;
    }

    /**
     * gets the new width
     * @return the new-width of the Upholstery.
     */
    public int getNewWidth() {
        return newWidth;
    }
}
