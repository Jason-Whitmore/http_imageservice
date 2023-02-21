package org.http_toy;

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
        int[][][] imageData = Utility.loadImageFromDisk(imagePath);

        //Convert imageData into a hex string.
        String hexImageData = Utility.imageDataToHexString(imageData);

        //Create the http client
        HttpClient client = HttpClient.newHttpClient();

        try{

            //Create the request
            //Change this to Get? How to add to
            HttpRequest request = HttpRequest.newBuilder().uri(new URI(serverAddress + "grey/")).POST(BodyPublishers.ofString(hexImageData)).build();

            //Send to server, recieve response.
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());


            String responseString = response.body();
            
            int[][][] grayImageData = Utility.hexStringToImageData(responseString);

            //Image constructed. Now save to the given location
            Utility.saveImageToDisk(grayImageData, greyImagePath);

            

        } catch(IOException e){
            System.err.println("IOException caught: " + e.toString());
            System.err.println(Arrays.toString(e.getStackTrace()));
        }catch(Exception e){
            System.err.println("Exception caught: " + e.toString());
        }
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
