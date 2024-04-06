package image_char_matching;

import javax.swing.event.DocumentListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * this class responsible to match ASCII char to sub image with given brightness
 */
public class SubImgCharMatcher {

    private final Map<Character, Double> charSet =new HashMap<>();
    private final static double SIZE_OF_BOOL_ARR = 256;
    private final static int ROWS_COLS_OF_BOOL_ARR = 16;
    private final Map<Character,Double> myMap = new HashMap<>();
    private final Map<Character, Double> brightnessMap = new HashMap<>();


    /**
     * constructor
     *
     * @param charset Array of chars that display the ASCII table
     */
    public SubImgCharMatcher(char[] charset){
        for (char c : charset) {
            brightnessMap.put(c,calculateCharBrightness(c));
        }
        double minBrightness = Collections.min(brightnessMap.values());
        double maxBrightness = Collections.max(brightnessMap.values());
        for (char c : charset) {
            charSet.put(c, normalizeAllChars(c,minBrightness,maxBrightness));
        }
    }

    /**
     * this function calculate the char brightness
     *
     * @return the brights of the given char.
     */
    public double calculateCharBrightness(char c) {
        double howMuchTrues = 0;
        boolean[][] matrix = CharConverter.convertToBoolArray(c);

        for (int i = 0; i < ROWS_COLS_OF_BOOL_ARR; i++) {
            for (int j = 0; j < ROWS_COLS_OF_BOOL_ARR; j++) {
                if (matrix[i][j]) {
                    howMuchTrues += 1;
                }
            }
        }
        return (howMuchTrues / SIZE_OF_BOOL_ARR);
    }

    /**
     *
     * @param c char of the ASCII table
     * @param minBrightness the min brightness of the all brightness
     * @param maxBrightness  the max brightness of the all brightness
     * @return new normalized brightness of the char
     */
    public double normalizeAllChars(char c,double minBrightness,double maxBrightness){
        return ((brightnessMap.get(c) - minBrightness) / (maxBrightness - minBrightness));

    }

    /**
     * this function has argument degree of the brightness
     * and returns char from char set
     *
     * @param brightness the brightness of char
     * @return returns the char that match the brightness in we had in the param
     */
    public char getCharByImageBrightness(double brightness) {
        for (char c : charSet.keySet()){
              myMap.put(c,Math.abs(charSet.get(c) - brightness));
        }
        double min = Collections.min(myMap.values());
        char mostCloseChar = ' ';
        boolean flag = true;
        for (char c: myMap.keySet()){
            if(myMap.get(c) == min && flag ){
                mostCloseChar = c;
                flag=false;
            }
            if(myMap.get(c) == min && c < mostCloseChar){
                mostCloseChar = c;
            }
        }
        return mostCloseChar;
    }

    /**
     * this function gets char , adds the char to the charset
     *
     * @param c char we want to add
     */
    public void addChar(char c) {
        if (brightnessMap.containsKey(c)){
            return;
        }
        brightnessMap.put(c,calculateCharBrightness(c));
        updateBrightness();
    }


    /**
     * this function gets a char ,removes this char from the charset
     *
     * @param c char we want to remove
     */
    public void removeChar(char c) {
        charSet.remove(c);
        brightnessMap.remove(c);
        if (charSet.size() > 1){
            updateBrightness();
        }
    }

    /**
     * this function updates normalized brightness according to remove char and add char
     */
    private void updateBrightness() {
        double minBrightness = Collections.min(brightnessMap.values());
        double maxBrightness = Collections.max(brightnessMap.values());
        for (char chr : this.brightnessMap.keySet()){
            charSet.put(chr, normalizeAllChars(chr,minBrightness,maxBrightness));
        }
    }

    /**
     * this function returns the charset with its brightness for each char
     * @return the map that contain keys as chars from the char set and values as doubles display
     *         the brightness of the char
     */
    public Map<Character, Double> getCharSet() {
        return charSet;
    }
}
