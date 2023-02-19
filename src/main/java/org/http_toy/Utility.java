package org.http_toy;

import java.util.Map;
import java.util.HashMap;

import java.util.List;
import java.util.ArrayList;

import java.math.BigInteger;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Utility {
    

    public static int[][][] convertRGBImageToGrayscaleImage(int[][][] rgbImage){
        int rows = rgbImage.length;
        int cols = rgbImage[0].length;

        int[][][] grayImage = new int[rows][cols][1];

        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                int red = rgbImage[r][c][0];
                int green = rgbImage[r][c][1];
                int blue = rgbImage[r][c][2];

                grayImage[r][c][0] = (red + green + blue) / 3;
            }
        }


        return grayImage;
    }


    public static String imageDataToHexString(int[][][] imageData){
        int[] flatImageData = Utility.imageToFlatImage(imageData);

        StringBuilder sb = new StringBuilder();

        int rows = flatImageData[0];
        String rowsBinary = Utility.integerToBinaryString(rows, 32);
        String rowsHex = Utility.binaryStringToHexString(rowsBinary);
        sb.append(rowsHex);

        int cols = flatImageData[1];
        String colsBinary = Utility.integerToBinaryString(cols, 32);
        String colsHex = Utility.binaryStringToHexString(colsBinary);
        sb.append(colsHex);


        int channels = flatImageData[2];
        String channelsBinary = Utility.integerToBinaryString(channels, 32);
        String channelsHex = Utility.binaryStringToHexString(channelsBinary);
        sb.append(channelsHex);

        int[] pixelData = Utility.getSubarray(flatImageData, 3, flatImageData.length - 1);



        String binaryStringPixelData = Utility.integerArrayToBinaryString(pixelData);

        String hexStringPixelData = Utility.binaryStringToHexString(binaryStringPixelData);

        sb.append(hexStringPixelData);

        return sb.toString();
    }

    public static int[][][] hexStringToImageData(String hexString){
        String binaryString = Utility.hexStringToBinaryString(hexString);

        String rowString = binaryString.substring(0, 32);
        int rows = Utility.binaryStringToInteger(rowString);

        String colString = binaryString.substring(32, 64);
        int cols = Utility.binaryStringToInteger(colString);

        String channelString = binaryString.substring(64, 96);
        int channels = Utility.binaryStringToInteger(channelString);

        binaryString = binaryString.substring(96);

        int[] flatImage = Utility.binaryStringToIntegerArray(binaryString);

        int[][][] image = Utility.flatImageToImage(flatImage, rows, cols, channels);

        return image;
    }

    public static String integerArrayToBinaryString(int[] array){
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < array.length; i++){
            
            //Using 8 bits for integers in range [0, 255]
            sb.append(Utility.integerToBinaryString(array[i], 8));
        }
        return sb.toString();
    }

    public static int[] binaryStringToIntegerArray(String binaryString){
        int[] array = new int[binaryString.length() / 8];

        for(int i = 0; i < binaryString.length(); i += 8){
            String byteString = binaryString.substring(i, i + 8);

            array[i / 8] = Utility.binaryStringToInteger(byteString);
        }

        return array;
    }

    public static int[][][] flatImageToImage(int[] flatImage, int rows, int cols, int channels){

        int[][][] image = new int[rows][cols][channels];
        int index = 0;

        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                for(int channel = 0; channel < channels; channel++){
                    image[r][c][channel] = flatImage[index];
                    index++;
                }
            }
        }

        return image;
    }

    public static int[] imageToFlatImage(int[][][] image){
        int length = image.length * image[0].length * image[0][0].length;

        //Create flat array, include dimension size in header
        int[] r = new int[3 + length];
        r[0] = image.length;
        r[1] = image[0].length;
        r[2] = image[0][0].length;

        int[] flatImageData = Utility.flatten(image);

        for(int i = 0; i < flatImageData.length; i++){
            r[i + 3] = flatImageData[i];
        }

        return r;
    }

    public static int[] flattenArray(int[][][] array){
        int imageSize = array.length * array[0].length * array[0][0].length;

        int[] r = new int[imageSize];

        int index = 0;

        for(int i = 0; i < array.length; i++){
            for(int j = 0; j < array[0].length; j++){
                for(int k = 0; k < array[0][0].length; k++){
                    r[index] = array[i][j][k];
                    index++;
                }
            }
        }

        return r;
    }

    public static String hexStringToBinaryString(String hexString){
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < hexString.length(); i++){
            char hexDigit = hexString.charAt(i);

            int number = Utility.hexToInteger(hexDigit);

            //Using 4 bits for integers in range[0, 15]
            String binary = Utility.integerToBinaryString(number, 4);

            sb.append(binary);
        }

        return sb.toString();
    }


    public static String integerToBinaryString(int num, int bits){

        char[] c = new char[bits];

        //Initial population
        for(int i = 0; i < c.length; i++){
            c[i] = '0';
        }

        int temp = num;
        int index = c.length - 1;

        while(temp > 0){
            if(index < 0){
                System.err.println("Bad index. num = " + num);
            }

            if(temp % 2 == 0){
                c[index] = '0';
            } else {
                c[index] = '1';
            }

            temp /= 2;
            index--;
        }
        
        
        return new String(c);
    }

    public static int[] flatten(int[][][] array){
        int length = array.length * array[0].length * array[0][0].length;

        int[] ret = new int[length];

        int i = 0;

        for(int r = 0; r < array.length; r++){
            for(int c = 0; c < array[r].length; c++){
                for(int k = 0; k < array[r][c].length; k++){
                    ret[i] = array[r][c][k];
                    i++;
                }
            }
        }

        return ret;
    }

    public static String binaryStringToHexString(String binaryString){
        StringBuilder sb = new StringBuilder();

        int numBits = 4;

        for(int i = 0; i < binaryString.length(); i+=numBits){

            //Isolate nibble
            String segment = binaryString.substring(i, i + numBits);

            int num = Utility.binaryStringToInteger(segment);

            char hex = Utility.integerToHex(num);

            sb.append(hex);
        }


        return sb.toString();
    }

    public static int binaryStringToInteger(String binaryString){
        int r = 0;

        char[] c = binaryString.toCharArray();

        int exponent = 0;
        for(int i = c.length - 1; i >= 0; i--){
            if(c[i] == '1'){
                r += (1 << exponent);
            }

            exponent++;
        }

        return r;
    }

    public static char integerToHex(int num){
        if(num <= 9){
            return (char)((int)'0' + num);
        } else {
            return (char)((int)'a' + num - 10);
        }
    }

    public static int hexToInteger(char digit){
        int ascii = (int)digit;

        if(ascii <= ((int)'9') && ascii >= ((int)'0')){
            return (int) (ascii - ((int)'0'));
        } else {
            return ((int) (ascii - ((int)'a'))) + 10;
        }
    }

    public static int[] getMostCommonColor(int[][][] imgData){
        Map<List<Integer>, Integer> colorMap = new HashMap<>();

        //Count colors
        for(int r = 0; r < imgData.length; r++){
            for(int c = 0; c < imgData[r].length; c++){
                List<Integer> colors = Utility.arrayToList(imgData[r][c]);


                //Add to map or increment
                if(colorMap.containsKey(colors)){
                    colorMap.put(colors, colorMap.get(colors) + 1);
                } else {
                    colorMap.put(colors, 1);
                }
            }
        }

        //Select most commonly occuring
        int max = 0;
        List<Integer> bestColor = null;

        for(List<Integer> color : colorMap.keySet()){
            if(colorMap.get(color) > max){
                bestColor = color;
                max = colorMap.get(color);
            }    
        }

        //Add occurance count onto the end
        bestColor.add(max);

        //Max found. Convert back to array

        return Utility.listToArray(bestColor);
    }

    public static int[] getMeanColor(int[][][] imgData){
        int channels = imgData[0][0].length;

        BigInteger[] channelSums = new BigInteger[channels];

        for(int i = 0; i < channelSums.length; i++){
            channelSums[i] = BigInteger.ZERO;
        }

        for(int r = 0; r < imgData.length; r++){
            for(int c = 0; c < imgData[r].length; c++){
                for(int ch = 0; ch < imgData[r][c].length; ch++){
                    channelSums[ch] = channelSums[ch].add(BigInteger.valueOf(imgData[r][c][ch]));
                }
            }
        }

        //Place results into return array
        int[] r = new int[channels];

        int denominator = imgData.length * imgData[0].length;

        for(int i = 0; i < r.length; i++){
            r[i] = (channelSums[i].divide(BigInteger.valueOf(denominator))).intValue();
        }

        return r;
    }

    public static List<Integer> arrayToList(int[] nums){
        List<Integer> l = new ArrayList<>();

        for(int i = 0; i < nums.length; i++){
            l.add(nums[i]);
        }

        return l;
    }

    public static int[] listToArray(List<Integer> l){
        int[] array = new int[l.size()];

        for(int i = 0; i < l.size(); i++){
            array[i] = l.get(i);
        }

        return array;
    }

    public static int[][][] isolateChannel(int[][][] imgData, int channel){
        int rows = imgData.length;
        int cols = imgData[0].length;

        int[][][] ret = new int[rows][cols][1];

        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                ret[r][c][0] = imgData[r][c][channel];
            }
        }

        return ret;
    }

    public static int getRandomInteger(int lowerBound, int upperBound){
        int delta = upperBound - lowerBound;

        return lowerBound + (int)(Math.random() * delta);
    }

    public static int[] getRandomArray(int size, int lowerBound, int upperBound){
        int[] r = new int[size];

        for(int i = 0; i < r.length; i++){
            r[i] = Utility.getRandomInteger(lowerBound, upperBound);
        }
        return r;
    }

    public static void populateArray(int[] array, int num){

        for(int i = 0; i < array.length; i++){
            array[i] = num;
        }

    }

    public static void populateArray(int[][] array, int num){

        for(int i = 0; i < array.length; i++){
            Utility.populateArray(array[i], num);
        }

    }

    public static void populateArray(int[][][] array, int num){

        for(int i = 0; i < array.length; i++){
            Utility.populateArray(array[i], num);
        }

    }

    public static void populateArrayRandom(int[] array, int lowerBound, int upperBound){
        for(int i = 0; i < array.length; i++){
            array[i] = Utility.getRandomInteger(lowerBound, upperBound);
        }
    }

    public static void populateArrayRandom(int[][] array, int lowerBound, int upperBound){
        for(int i = 0; i < array.length; i++){
            for(int j = 0; j < array[i].length; j++){
                array[i][j] = Utility.getRandomInteger(lowerBound, upperBound);
            }
        }
    }

    public static void populateArrayRandom(int[][][] array, int lowerBound, int upperBound){
        for(int i = 0; i < array.length; i++){
            for(int j = 0; j < array[i].length; j++){
                for(int k = 0; k < array[i][j].length; k++){
                    array[i][j][k] = Utility.getRandomInteger(lowerBound, upperBound);
                }
            }
        }
    }

    public static boolean isEqual(int[] a, int[] b){
        if(a == null && b == null){
            return true;
        }

        if((a == null && b != null) || (a != null && b == null)){
            return false;
        }

        if(a.length != b.length){
            return false;
        }

        for(int i = 0; i < a.length; i++){
            if(a[i] != b[i]){
                return false;
            }
        }

        return true;
    }

    public static boolean isEqual(int[][] a, int[][] b){

        for(int i = 0; i < a.length; i++){
            if(!isEqual(a[i], b[i])){
                return false;
            }
        }

        return true;
    }

    public static boolean isEqual(int[][][] a, int[][][] b){

        for(int i = 0; i < a.length; i++){
            if(!isEqual(a[i], b[i])){
                return false;
            }
        }

        return true;
    }

    public static int[] duplicateArray(int[] a){
        int[] r = new int[a.length];

        for(int i = 0; i < r.length; i++){
            r[i] = a[i];
        }

        return r;
    }

    public static int[][] duplicateArray(int[][] a){
        int[][] r = new int[a.length][];

        for(int i = 0; i < a.length; i++){
            r[i] = Utility.duplicateArray(a[i]);
        }

        return r;
    }

    public static int[][][] duplicateArray(int[][][] a){
        int[][][] r = new int[a.length][][];

        for(int i = 0; i < a.length; i++){
            r[i] = Utility.duplicateArray(a[i]);
        }

        return r;
    }

    public static int[] getSubarray(int[] array, int start, int end){
        int n = end - start + 1;

        int[] r = new int[n];

        for(int i = start; i - start < r.length; i++){
            r[i - start] = array[i];
        }

        return r;
    }

    public static int[][][] loadImageFromDisk(String filePath){
        BufferedImage image = null;
        try{

            File f = new File(filePath);

            image = ImageIO.read(f);

        } catch (javax.imageio.IIOException e){
            System.err.println(e.toString());
            return null;
        } catch(Exception e){
            System.err.println(e.toString());
            return null;
        }

        

        int rows = image.getHeight();
        int cols = image.getWidth();
        int channels = image.getColorModel().getColorSpace().getNumComponents();

        if(channels != 3){
            System.err.println("Input image must have three channels");
            return null;
        }

        int[][][] imageData = new int[rows][cols][channels];

        //Todo: issue somewhere here. imageData[r][c] contains all of the same numbers, unintentional grayscale?


        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                int rgb = image.getRGB(c, r);

                //set red
                imageData[r][c][0] = rgb & 0xff;

                //set blue
                imageData[r][c][1] = (rgb & 0xff00) >> 8;

                //set green
                imageData[r][c][2] = (rgb & 0xff0000) >> 16;
            }
        }
        

        return imageData;
    }

    public static void saveImageToDisk(int[][][] imageData, String filePath){
        int numRows = imageData.length;
        int numCols = imageData[0].length;

        BufferedImage bi = new BufferedImage(numCols, numRows, ColorSpace.TYPE_RGB);

        for(int r = 0; r < numRows; r++){
            for(int c = 0; c < numCols; c++){
                int color = imageData[r][c][0];

                color <<= 8;
                color += imageData[r][c][1];

                color <<= 8;
                color += imageData[r][c][2];

                bi.setRGB(c, r, color);
            }
        }

        try{
            File f = new File(filePath);

            ImageIO.write(bi, filePath, f);
        } catch(IOException e){
            System.err.println("Exception occured when trying to save image to file in Utility.saveImageToDisk()");
            System.err.println(e.toString());
        }
    }


}
