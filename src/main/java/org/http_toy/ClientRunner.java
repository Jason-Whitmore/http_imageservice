package org.http_toy;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.integration.IntegrationProperties.RSocket.Client;

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
        //gray, red, blue, green commands also require a return image argument (total 3)
        //help does not require any input
        //Address needs to look like: http://localhost:8080

        
        System.out.println("Args length: " + args.length);


        if(args.length == 1){
            //Could be help
            if(args[0].equals("help")){

            }
        } else if(args.length == 2){

            
        } else if(args.length == 3){
            
            
            
            //Could be stats
            if(args[0].equals("stats")){
                String imagePath = args[1];
                String address = args[2];

                System.out.println("Image path: " + imagePath);

                ClientRunner.handleStats(imagePath, address);
            }

        } else if(args.length == 4){
            //Could be gray, red, blue, green

            if(args[0].equals("gray") || args[0].equals("grey")){
                //handle grey
                String imagePath = args[1];
                String address = args[2];
                String grayImagePath = args[3];

                ClientRunner.handleColor(imagePath, address, grayImagePath, "gray");

            } else if(args[0].equals("red")){
                //handle red
                String imagePath = args[1];
                String address = args[2];
                String redImagePath = args[3];

                ClientRunner.handleColor(imagePath, address, redImagePath, "red");

            } else if(args[0].equals("blue")){
                //handle blue
                String imagePath = args[1];
                String address = args[2];
                String blueImagePath = args[3];

                ClientRunner.handleColor(imagePath, address, blueImagePath, "blue");

            } else if(args[0].equals("green")){
                //handle green
                String imagePath = args[1];
                String address = args[2];
                String greenImagePath = args[3];

                ClientRunner.handleColor(imagePath, address, greenImagePath, "green");

            }

        } else {
            //No valid command
            System.out.println("Not a valid command. Try 'imageclient help' to see available options.");
        }
    }

    private static void handleColor(String imagePath, String serverAddress, String outputImagePath, String color){
        int[][][] imageData = Utility.loadImageFromDisk(imagePath);

        int[][][] blueImageData = ClientRunner.sendAndRecieveImage(imageData, serverAddress + color + "/");

        Utility.saveImageToDisk(blueImageData, outputImagePath);
    }


    private static void handleStats(String imagePath, String serverAddress){
        int[][][] imageData = Utility.loadImageFromDisk(imagePath);

        int numRows = imageData.length;
        int numCols = imageData[0].length;
        int numChannels = imageData[0][0].length;

        //Serialize image data
        byte[] byteImageData = Utility.imageToByteArray(imageData);

        HttpResponse<String> response = ClientRunner.sendImagePost(byteImageData, numRows, numCols, numChannels, serverAddress + "stats/");

        String body = response.body();

        System.out.println(body);
        
    }

    private static HttpResponse<String> sendImagePost(byte[] imageDataBytes, int numRows, int numCols, int numChannels, String serverAddress){
        //Create the http client
        HttpClient client = HttpClient.newHttpClient();

        try{

            //Create the request
            String imageDataString = Base64.getEncoder().withoutPadding().encodeToString(imageDataBytes);
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(new URI(serverAddress)).POST(BodyPublishers.ofString(imageDataString));

            requestBuilder.header("num_rows", numRows + "");
            requestBuilder.header("num_cols", numCols + "");
            requestBuilder.header("num_channels", numChannels + "");

            //Send to server, recieve response.
            HttpResponse<String> response = client.send(requestBuilder.build(), BodyHandlers.ofString());
            return response;

            

        } catch(IOException e){
            System.err.println("IOException caught: " + e.toString());
            System.err.println(Arrays.toString(e.getStackTrace()));
        } catch(Exception e){
            System.err.println("Exception caught: " + e.toString());
            System.err.println(Arrays.toString(e.getStackTrace()));
        }

        return null;
    }

    private static int[][][] sendAndRecieveImage(int[][][] imageData, String serverAddress){
        //Serialize image data
        byte[] imageDataBytes = Utility.imageToByteArray(imageData);

        int numRows = imageData.length;
        int numCols = imageData[0].length;
        int numChannels = imageData[0][0].length;

        HttpResponse<String> response = ClientRunner.sendImagePost(imageDataBytes, numRows, numCols, numChannels, serverAddress);

        HttpHeaders headers = response.headers();

        int[] dimensions = Utility.getImageDimensions(headers);


        if(dimensions != null && dimensions.length == 3){
            numRows = dimensions[0];
            numCols = dimensions[1];
            numChannels = dimensions[2];
        }

        String body = response.body();

        byte[] byteImageData = Base64.getDecoder().decode(body);

        int[][][] returnImage = Utility.byteArrayToImageData(byteImageData, numRows, numCols, numChannels);

        return returnImage;
    }

    
}
