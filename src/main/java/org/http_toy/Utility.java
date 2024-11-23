package org.http_toy;

import java.util.Map;
import java.util.HashMap;

import java.util.List;
import java.util.ArrayList;

import java.math.BigInteger;
import java.net.http.HttpHeaders;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * This class contains helpful static methods for use in the rest of the project.
 */
public class Utility {
    
    /**
     * Converts an RGB input image into a grayscale image.
     * @param rgbImage The RGB image to convert. The third dimension must be of length 3 (for 3 channels).
     * @return The grayscale output image. The third dimension has a length of 1.
     */
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

    /**
     * Gets the most common color in the image input. This is like a color "mode"
     * @param imgData The input image. Can have any amount of channels.
     * @return The most common color. Each element is the signal of the channel.
     */
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
        List<Integer> bestColor = new ArrayList<>();

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

    /**
     * Gets the mean color from the image.
     * @param imgData The image to get the mean color from
     * @return The mean color with each channel in the range [0, 255]
     */
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

    /**
     * Converts an integer array into a list of Integers
     * @param nums The integer array to convert.
     * @return The converted numbers in a List
     */
    public static List<Integer> arrayToList(int[] nums){
        List<Integer> l = new ArrayList<>();

        for(int i = 0; i < nums.length; i++){
            l.add(nums[i]);
        }

        return l;
    }

    /**
     * Converts a List of Integers into an integer array
     * @param l The list to convert
     * @return The integer array
     */
    public static int[] listToArray(List<Integer> l){
        int[] array = new int[l.size()];

        for(int i = 0; i < l.size(); i++){
            array[i] = l.get(i);
        }

        return array;
    }

    /**
     * Isolates a channel in an image. All other channels are removed in the returned image.
     * @param imgData The input image.
     * @param channel The channel to be isolated. 0, 1, 2 for red, green, blue, respectively.
     * @return A new image with the isolated channel
     */
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

    /**
     * Loads a file from disk and returns it as a 3d array
     * @param filePath The filepath of the image on disk.
     * @return The image as a 3d array
     */
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


        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                int rgb = image.getRGB(c, r);

                //set red
                imageData[r][c][0] = (rgb & 0xff0000) >> 16;

                //set green
                imageData[r][c][1] = (rgb & 0x00ff00) >> 8;

                //set blue
                imageData[r][c][2] = (rgb & 0x0000ff);
            }
        }
        

        return imageData;
    }

    /**
     * Saves the image to a location on disk.
     * @param imageData The image to save
     * @param filePath The filepath to where the image will be saved.
     */
    public static void saveImageToDisk(int[][][] imageData, String filePath){
        int numRows = imageData.length;
        int numCols = imageData[0].length;
        int numChannels = imageData[0][0].length;

        BufferedImage bi = new BufferedImage(numCols, numRows, ColorSpace.TYPE_RGB);

        for(int r = 0; r < numRows; r++){
            for(int c = 0; c < numCols; c++){

                int color = imageData[r][c][0];

                if(numChannels == 3){
                    color <<= 8;
                    color += imageData[r][c][1];

                    color <<= 8;
                    color += imageData[r][c][2];
                } else {
                    color <<= 8;
                    color += imageData[r][c][0];

                    color <<= 8;
                    color += imageData[r][c][0];
                }
                

                bi.setRGB(c, r, color);
            }
        }

        try{
            File f = new File(filePath);

            String fileType = filePath.substring(filePath.indexOf(".") + 1);

            ImageIO.write(bi, fileType, f);
        } catch(IOException e){
            System.err.println("Exception occured when trying to save image to file in Utility.saveImageToDisk()");
            System.err.println(e.toString());
        }
    }

    /**
     * Converts an image (3d array) into a 1d array of bytes
     * @param imageData The image to convert
     * @return The 1d array of bytes. This does not contain any of the image width, height, or number of channels
     */
    public static byte[] imageToByteArray(int[][][] imageData){
        int numRows = imageData.length;
        int numCols = imageData[0].length;
        int numChannels = imageData[0][0].length;

        int bytesPerChannel = 4;

        byte[] byteArray = new byte[numRows * numCols * numChannels * bytesPerChannel];

        int index = 0;

        for(int r = 0; r < numRows; r++){
            for(int c = 0; c < numCols; c++){
                for(int channel = 0; channel < numChannels; channel++){
                    byteArray[index] = (byte)imageData[r][c][channel];

                    index++;
                }
            }
        }

        return byteArray;
    }

    /**
     * Gets the image dimensions (width, height, and number of channels) from a HTTP header
     * @param headers The HTTP header that contains image dimensions
     * @return The dimensions of the image. The ordering is: num rows, num cols, num channels
     */
    public static int[] getImageDimensions(HttpHeaders headers){

        Map<String, List<String>> headerMap = headers.map();
        Map<String, String> map = new HashMap<>();

        for(String key: headerMap.keySet()){
            if(headerMap.get(key).size() > 0){
                map.put(key, headerMap.get(key).get(0));
            }
            
        }

        return Utility.getImageDimensions(map);
    }

    /**
     * Gets the image dimensions (width, height, and number of channels) from a HTTP header
     * @param map The map that contains image dimensions
     * @return The dimensions of the image. The ordering is: num rows, num cols, num channels
     */
    public static int[] getImageDimensions(Map<String, String> map){
        int numRows = -1;
        int numCols = -1;
        int numChannels = -1;
        if(map == null){
            return null;
        }


        if(map.containsKey("num_rows")){
            numRows = Integer.parseInt(map.get("num_rows"));
        }

        if(map.containsKey("num_cols")){
            numCols = Integer.parseInt(map.get("num_cols"));
        }

        if(map.containsKey("num_channels")){
            numChannels = Integer.parseInt(map.get("num_channels"));
        }

        if(numRows == -1 || numCols == -1 || numChannels == -1){
            return null; 
        }

        int[] r = new int[3];

        r[0] = numRows;
        r[1] = numCols;
        r[2] = numChannels;

        return r;
    }

    /**
     * Converts a byte array and image metadata into an image
     * @param data The bytes containing the pixel information of an image
     * @param numRows The number of rows in the image
     * @param numCols The number of columns in the image
     * @param numChannels The number of channels in the image
     * @return The image as a 3d array
     */
    public static int[][][] byteArrayToImageData(byte[] data, int numRows, int numCols, int numChannels){
        int[][][] imageData = new int[numRows][numCols][numChannels];

        int index = 0;

        for(int r = 0; r < numRows; r++){
            for(int c = 0; c < numCols; c++){
                for(int channel = 0; channel < numChannels; channel++){
                    imageData[r][c][channel] = (int)data[index];
                    index++;
                }
            }
        }


        return imageData;
    }


}
