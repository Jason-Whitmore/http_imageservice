package org.http_toy;



import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;


import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Arrays;

/**
 * Hello world!
 *
 */
public class ClientRunner 
{
    public static void main( String[] args )
    {
        //Determine which command is being invoked.
        //stats, gray, red, blue, green all require an image as an arg (total 2)
        //grayscale, red, blue, green commands also require a return image argument (total 3)
        //help does not require any input

        
        System.out.println("Args length: " + args.length);


        if(args.length == 1){
            //Could be help
            if(args[0].equals("help")){

            }
        } else if(args.length == 2){

            
        } else if(args.length == 3){
            //Could be gray, red, blue, green
            
            //Could be stats
            if(args[0].equals("stats")){
                String imagePath = args[1];
                String address = args[2];

                System.out.println("Image path: " + imagePath);

                ClientRunner.handleStats(imagePath, address);
            }

        } else {
            //No valid command
            System.out.println("Not a valid command. Try 'imageclient help' to see available options.");
        }
    }

    private static int[][][] imagePathToImageData(String imagePath){

        BufferedImage image = null;
        try{

            File f = new File(imagePath);

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

    private static void handleStats(String imagePath, String serverAddress){
        int[][][] imageData = ClientRunner.imagePathToImageData(imagePath);

        //Convert imageData into a hex string.
        String hexImageData = Utility.imageDataToHexString(imageData);

        //Create the http client
        HttpClient client = HttpClient.newHttpClient();

        

        try{
            //Create the request
            //Change this to Get? How to add to
            HttpRequest request = HttpRequest.newBuilder().uri(new URI(serverAddress + "stats/")).POST(BodyPublishers.ofString(hexImageData)).build();

            

            //Send to server, recieve response.
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            //System.out.println("Response code: " + response.statusCode());

            String responseString = "" + response.body();            
            

            //Replace the string start message
            responseString = responseString.replace("[STRING]", "");

            System.out.println(responseString);

        } catch(IOException e){
            System.err.println("IOException caught: " + e.toString());
            System.err.println(Arrays.toString(e.getStackTrace()));
        }catch(Exception e){
            System.err.println("Exception caught: " + e.toString());
        }
        
    }
}
