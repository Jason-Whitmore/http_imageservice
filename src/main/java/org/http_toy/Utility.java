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

            System.out.println("File type: " + fileType);

            ImageIO.write(bi, fileType, f);
        } catch(IOException e){
            System.err.println("Exception occured when trying to save image to file in Utility.saveImageToDisk()");
            System.err.println(e.toString());
        }
    }

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
