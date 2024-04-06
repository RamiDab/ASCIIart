package ascii_art;



import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import image_char_matching.SubImgCharMatcher;
import java.io.IOException;
import java.util.*;

import static java.lang.Math.max;

/**
 * this class is responsible about the user implementation
 */
public class Shell {

    private final static int MAX_CHAR_INDEX = 127;
    private final static int MIN_CHAR_INDEX = 32;

    private final char[] charSet = new char[]{'0','1','2','3','4','5','6','7','8','9'};
    private final SubImgCharMatcher subImgCharMatcher = new SubImgCharMatcher(charSet) ;
    private int defaultResultion = 128;
    private Image image;

    private final ConsoleAsciiOutput consoleAsciiOutput = new ConsoleAsciiOutput();
    private final HtmlAsciiOutput htmlAsciiOutput = new HtmlAsciiOutput("out.html","Courier New");
    private AsciiArtAlgorithm asciiArtAlgorithm;
    private char[][] finalPicture;


    /**
     * constructor of the shell
     * @param image image we have to display it as ascii table
     */
    public Shell(Image image){
        this.image = image;
        asciiArtAlgorithm = new AsciiArtAlgorithm(this.image,subImgCharMatcher,defaultResultion);
        finalPicture = asciiArtAlgorithm.run();
    }

    /**
     * this is the main function that runs the whole algorithm
     * @param args the arguments from the user.
     * @throws IOException Exception.
     */
    public static void main(String[] args) throws IOException {
        String picPath = "cat.jpeg";
        if(args.length == 1){
            picPath = args[0];
        }
        Image images = new Image(picPath);
        Shell shell = new Shell(images);
        shell.run();
    }
    /**
     * this function runs the program and contain all the methods
     */
    public void run() throws IOException {
        System.out.print(">>> ");
        String inputLine = KeyboardInput.readLine();
        while(!inputLine.equals("exit")){
            String[] splittedLine = inputLine.split("\\s+");
            if(splittedLine[0].equals("chars")){
                chars();
            }
            else if(splittedLine[0].equals("add")) {
                add(splittedLine);
            }
            else if (splittedLine[0].equals("remove")) {
                remove(splittedLine);
            }
            else if(splittedLine[0].equals("res")){
                if(splittedLine.length == 1 || (!splittedLine[1].equals("up")
                        && !splittedLine[1].equals("down"))){
                    System.out.println("Did not change resolution due to incorrect format");
                }
                else{
                    res(splittedLine[1]);
                }

            }
            else if(splittedLine[0].equals("image")){
                    image(splittedLine);
            }
            else if(splittedLine[0].equals("output")){
                output(splittedLine);
            }
            else if(splittedLine[0].equals("asciiArt")){
                if(splittedLine.length > 1){
                    System.out.println("Did not execute due to incorrect command.");
                }
                else {
                    finalPicture = asciiArt();
                }
            }
            else{
                System.out.println("Did not execute due to incorrect command.");
            }
            System.out.print(">>> ");
            inputLine =  KeyboardInput.readLine();
        }
        exit();
    }

    /**
     * this method is responsible about the run of the program.
     * @return a 2D array that contain the picture
     */
    private char[][] asciiArt(){
        try{
            if(!subImgCharMatcher.getCharSet().isEmpty()){
                this.asciiArtAlgorithm = new AsciiArtAlgorithm(this.image, subImgCharMatcher,
                this.defaultResultion);
        return asciiArtAlgorithm.run();
            }
        }catch (Exception e){
            System.out.println("Did not execute. Charset is empty.");
            return new char[0][0];
        }
        return new char[0][0];
    }

    /**
     * this method is responsible about the output
     * @param outputType a string that we have to check if ts console or html
     */
    private void output(String[] outputType) throws ArrayIndexOutOfBoundsException{
        try{
            if(outputType[1].equals("html")){
            htmlAsciiOutput.out(finalPicture);
        }
        else if(outputType[1].equals("console")){
            consoleAsciiOutput.out(finalPicture);
        }
        else{
            System.out.println("Did not change output method due to incorrect format.");
            }
        }catch (ArrayIndexOutOfBoundsException exception){
            System.out.println("Did not change output method due to incorrect format.");
        }
    }

    /**
     * this is the image method .
     * @param splittedLine the line that we got from the user splitted in an array.
     */
    private void image(String[] splittedLine)throws  IOException{
        try{
            this.image = new Image(splittedLine[1]);
        }
        catch (IOException ioException){
            System.out.println("Did not execute due to problem with image file.");
        }
    }

    /**
     * this method updates the resolution according to the string if its up we multiply it with two
     * if its down we divide it with two
     * @param upDownRes a string we have to check if its up or down
     */
    private void res(String upDownRes) {
        int max = this.image.getWidth();
        int min = max(1, this.image.getWidth()/this.image.getHeight());
        if(upDownRes.equals("up")){
            if(this.defaultResultion * 2 > max){
                System.out.println("Did not change resolution due to exceeding boundaries.");
            }
            else {
                this.defaultResultion *= 2;
                System.out.println("Resolution set to " + this.defaultResultion + ".");
            }
        }
        else if(upDownRes.equals("down")){
            if(this.defaultResultion / 2 < min){
                System.out.println("Did not change resolution due to exceeding boundaries.");
            }
            else {
                this.defaultResultion /= 2;
                System.out.println("Resolution set to " + this.defaultResultion + ".");
            }
        }
    }

    /**
     * this functions mission is to sort the charSet so we can have the minimum
     * and the maximum faster.
     */
    private void chars() {
        Set<Character> sortedArr = new TreeSet<>(subImgCharMatcher.getCharSet().keySet());
        for(char ch :sortedArr ){
            System.out.print(ch + " ");
        }
        System.out.println();
    }


    /**
     * the add function.
     * @param splittedLine the line that we got from the user splitted in an array.
     */
    private void add(String[] splittedLine) {
        try{
            addRemoveHelper(splittedLine, true);
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Did not add due to incorrect format");
        }
    }

    /**
     * the remove function.
     * @param splittedLine the line that we got from the user splitted in an array.
     */
    private void remove(String[] splittedLine) {
        try{
            addRemoveHelper(splittedLine, false);
        }
        catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Did not remove due to incorrect format");
        }
    }


    /**
     * the main remove add function that checks everything needed and applies the correct other
     * helper functions.
     * @param splittedLine the line that we got from the user splitted in an array.
     * @param isAddition a boolean variable, it's true when the user wants to apply addition on
     *                   the charSet and false when the user wants to apply removing on the charSet.
     */
    private void addRemoveHelper(String[] splittedLine, boolean isAddition)
            throws ArrayIndexOutOfBoundsException{
            String[] splitRange = splittedLine[1].split("(?<=.)");
            if(splittedLine[1].length()==1){
                addRemoveOneChar(splittedLine, isAddition);
            }
            else if(splittedLine[1].equals("all")){
                addRemoveAll(isAddition);
            }
            else if (splittedLine[1].equals("space")) {
                addRemoveSpace(isAddition);
            }
            else if(splitRange[1].equals("-")) {
                try{
                    checkRange(isAddition, splitRange);
                }catch (ArrayIndexOutOfBoundsException e){
                    if (isAddition) {
                        System.out.println("Did not add due to incorrect format");
                    } else {
                        System.out.println("Did not remove due to incorrect format");
                    }
                }
            }
            else{
                if (isAddition) {
                    System.out.println("Did not add due to incorrect format");
                } else {
                    System.out.println("Did not remove due to incorrect format");
                }
            }
    }

    /**
     * adds or removes one char from the charSet.
     * @param splittedLine the line that we got from the user splitted in an array.
     * @param isAddition a boolean variable, it's true when the user wants to apply addition on
     *                   the charSet and false when the user wants to apply removing on the charSet.
     */
    private void addRemoveOneChar(String[] splittedLine, boolean isAddition) {
        if (isAddition) {
            subImgCharMatcher.addChar(splittedLine[1].charAt(0));
        } else {
            subImgCharMatcher.removeChar(splittedLine[1].charAt(0));
        }
    }

    /**
     * removes or adds the space according to the boolean variable.
     * @param isAddition a boolean variable, it's true when the user wants to apply addition on
     *      *                   the charSet and false when the user wants to apply removing on the charSet.
     */
    private void addRemoveSpace(boolean isAddition) {
        if (isAddition) {
            subImgCharMatcher.addChar(' ');
        } else {
            subImgCharMatcher.removeChar(' ');
        }
    }

    /**
     * removes or adds all the characters according to the boolean variable.
     * @param isAddition a boolean variable, it's true when the user wants to apply addition on
     *      *                   the charSet and false when the user wants to apply removing on the charSet.
     */
    private void addRemoveAll(boolean isAddition) {
        for (int i = MIN_CHAR_INDEX; i < MAX_CHAR_INDEX; i++) {
            char ch = (char) i;
            if (isAddition) {
                subImgCharMatcher.addChar(ch);
            } else {
                subImgCharMatcher.removeChar(ch);
            }
        }
    }

    /**
     * this function checks the range of ASCII characters that the user wants to add to the charSet.
     * @param isAddition a boolean variable, it's true when the user wants to apply addition on
     *                   the charSet and false when the user wants to apply removing on the charSet.
     * @param splittedLine the line that we got from the user splitted in an array.
     */
    private void checkRange(boolean isAddition, String[] splittedLine)
            throws ArrayIndexOutOfBoundsException {
        if (splittedLine[0].charAt(0) < splittedLine[2].charAt(0)){
            for (char ch = splittedLine[0].charAt(0); ch <= splittedLine[2].charAt(0); ch++){
                if (isAddition) {
                    subImgCharMatcher.addChar(ch);
                } else {
                    subImgCharMatcher.removeChar(ch);
                }
            }
        }
        else if(splittedLine[0].charAt(0) > splittedLine[2].charAt(0)){
            for (char ch = splittedLine[2].charAt(0); ch <= splittedLine[0].charAt(0); ch++){
                if (isAddition) {
                    subImgCharMatcher.addChar(ch);
                } else {
                    subImgCharMatcher.removeChar(ch);
                }
            }
        }
        else{
            if (isAddition) {
                subImgCharMatcher.addChar(splittedLine[0].charAt(0));
            } else {
                subImgCharMatcher.removeChar(splittedLine[0].charAt(0));
            }
        }
    }

    /**
     * this method exit the program with code 0
     */
    private void exit(){
        System.exit(0);
    }

}
