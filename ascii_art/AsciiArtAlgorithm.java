package ascii_art;

import image.Image;
import image.Upholstery;
import image_char_matching.SubImgCharMatcher;

import java.awt.*;
import java.util.ArrayList;

/**
 * this class is responsible to run the algorithm
 */
public class AsciiArtAlgorithm {

    private final Image image;
    private final SubImgCharMatcher subImgCharMatcher;
    private final int resolution;


    /**
     * constructor , this constructor initialize the image , subimgcharmatcher and the resolution
     * @param image the image that we are working on.
     * @param subImgCharMatcher  the subImageMatcher.
     * @param resolution the resolution of the picture.
     */
    public AsciiArtAlgorithm(Image image, SubImgCharMatcher subImgCharMatcher,int resolution) {
        this.image = image;
        this.subImgCharMatcher = subImgCharMatcher;
        this.resolution=resolution;
    }


    /**
     * this function runs algorithm and returns 2D array that contains chars that display the image  
     * @return A 2D array which is the new picture.
     */
    public char[][] run() {
        Upholstery upholstery = new Upholstery(this.image);
        ArrayList<Color[][]> subImages = upholstery.newImage.splitToSubImg
                (upholstery.newImage, this.resolution);
        char[][] asciiImage = new char[this.resolution][this.resolution];
        int counter = 0;
            for (int i = 0; i < resolution; i++) {
                for (int j = 0; j < resolution; j++) {
                    asciiImage[i][j] = subImgCharMatcher.getCharByImageBrightness
                            (upholstery.newImage.calculateSubImageBrightness(subImages.get(counter)));
                    counter++;
                }
            }
        return asciiImage;
    }

}
