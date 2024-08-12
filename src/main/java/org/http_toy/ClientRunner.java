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
                String greyImagePath = args[3];

                ClientRunner.handleGrey(imagePath, address, greyImagePath);

            } else if(args[0].equals("red")){
                //handle red
                String imagePath = args[1];
                String address = args[2];
                String redImagePath = args[3];

                ClientRunner.handleRed(imagePath, address, redImagePath);

            } else if(args[0].equals("blue")){
                //handle blue
                String imagePath = args[1];
                String address = args[2];
                String blueImagePath = args[3];

                ClientRunner.handleBlue(imagePath, address, blueImagePath);

            } else if(args[0].equals("green")){
                //handle green
                String imagePath = args[1];
                String address = args[2];
                String greenImagePath = args[3];

                ClientRunner.handleGreen(imagePath, address, greenImagePath);

            }

        } else {
            //No valid command
            System.out.println("Not a valid command. Try 'imageclient help' to see available options.");
        }
    }


    private static void handleGrey(String imagePath, String serverAddress, String greyImagePath){


        //HttpResponse<String> response = ClientRunner.sendImagePost(imageDataBytes, numRows, numCols, numChannels, serverAddress + "grey/");

        //String responseString = response.body();

        //HttpHeaders header = response.headers();



        
    }

    private static void handleRed(String imagePath, String serverAddress, String redImagePath){
        int[][][] imageData = Utility.loadImageFromDisk(imagePath);

        //Convert imageData into a hex string.
        String hexImageData = Utility.imageDataToHexString(imageData);

        //Create the http client
        HttpClient client = HttpClient.newHttpClient();

        try{

            //Create the request
            //Change this to Get? How to add to
            HttpRequest request = HttpRequest.newBuilder().uri(new URI(serverAddress + "red/")).POST(BodyPublishers.ofString(hexImageData)).build();

            //Send to server, recieve response.
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());


            String responseString = response.body();
            
            int[][][] redImageData = Utility.hexStringToImageData(responseString);

            //Image constructed. Now save to the given location
            Utility.saveImageToDisk(redImageData, redImagePath);

            

        } catch(IOException e){
            System.err.println("IOException caught: " + e.toString());
            System.err.println(Arrays.toString(e.getStackTrace()));
        }catch(Exception e){
            System.err.println("Exception caught: " + e.toString());
        }
    }

    private static void handleGreen(String imagePath, String serverAddress, String greenImagePath){
        int[][][] imageData = Utility.loadImageFromDisk(imagePath);

        //Convert imageData into a hex string.
        String hexImageData = Utility.imageDataToHexString(imageData);

        //Create the http client
        HttpClient client = HttpClient.newHttpClient();

        try{

            //Create the request
            //Change this to Get? How to add to
            HttpRequest request = HttpRequest.newBuilder().uri(new URI(serverAddress + "green/")).POST(BodyPublishers.ofString(hexImageData)).build();

            //Send to server, recieve response.
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());


            String responseString = response.body();
            
            int[][][] greenImageData = Utility.hexStringToImageData(responseString);

            //Image constructed. Now save to the given location
            Utility.saveImageToDisk(greenImageData, greenImagePath);

            

        } catch(IOException e){
            System.err.println("IOException caught: " + e.toString());
            System.err.println(Arrays.toString(e.getStackTrace()));
        }catch(Exception e){
            System.err.println("Exception caught: " + e.toString());
        }
    }

    private static void handleBlue(String imagePath, String serverAddress, String blueImagePath){

        
        int[][][] imageData = Utility.loadImageFromDisk(imagePath);
        

        //Convert imageData into a hex string.
        String hexImageData = Utility.imageDataToHexString(imageData);

        

        //Create the http client
        HttpClient client = HttpClient.newHttpClient();

        try{

            //Create the request
            //Change this to Get? How to add to
            HttpRequest request = HttpRequest.newBuilder().uri(new URI(serverAddress + "blue/")).POST(BodyPublishers.ofString(hexImageData)).build();

            //Send to server, recieve response.
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());


            String responseString = response.body();

            responseString = responseString.replace("[Image]", "");
            
            int[][][] blueImageData = Utility.hexStringToImageData(responseString);

            //Image constructed. Now save to the given location
            Utility.saveImageToDisk(blueImageData, blueImagePath);

            

        } catch(IOException e){
            System.err.println("IOException caught: " + e.toString());
            System.err.println(Arrays.toString(e.getStackTrace()));
        }catch(Exception e){
            System.err.println("Exception caught: " + e.toString());
        }
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
