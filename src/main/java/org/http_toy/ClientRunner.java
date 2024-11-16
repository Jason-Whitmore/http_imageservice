package org.http_toy;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Arrays;
import java.util.Base64;

/**
 * 
 *
 */
public class ClientRunner 
{
    //Compile/run this code with: mvn compile exec:java -Dexec.mainClass=org.http_toy.ClientRunner -Dexec.args="stats olympic.jpg http://localhost:8080/"

    public static void main( String[] args )
    {
        //Determine which command is being invoked.
        //stats, gray, red, blue, green all require an image as an arg (total 2)
        //gray, red, blue, green commands also require a return image argument (total 3)
        //help does not require any input
        //Address needs to look like: http://localhost:8080/


        if(args.length == 1 || args.length == 2){
            ClientRunner.handleHelp();
        } else if(args.length == 3){
            //Could be stats
            if(args[0].equals("stats")){
                String imagePath = args[1];
                String address = args[2];

                ClientRunner.handleStats(imagePath, address);
            } else {
                ClientRunner.handleUnknown();
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

            } else {
                ClientRunner.handleUnknown();
            }

        } else {
            //No valid command
            ClientRunner.handleUnknown();
        }
    }

    /**
     * Prints a specific message to standard out when the command is unknown.
     */
    private static void handleUnknown(){
        System.out.println("Not a valid command. Try running this program with the 'help' argument to see available options.");
    }

    /**
     * Prints information about the various commands to standard out. Information includes syntax and examples.
     */
    private static void handleHelp(){
        StringBuilder sb = new StringBuilder();
        sb.append("Commands:\n\n");

        sb.append("Stats:\n");
        sb.append("Usage: ClientRunner stats [image path] [address]\n");
        sb.append("Example: ClientRunner stats image.png http://localhost:8080/\n");
        sb.append("Description: Server will respond with statistics about the image that was sent, including image size,\n");
        sb.append("mean color, and most commonly occurring color.\n\n");

        sb.append("Gray:\n");
        sb.append("Usage: ClientRunner gray [image path] [address] [output image path]\n");
        sb.append("Example: ClientRunner gray image.png http://localhost:8080/ gray_image.png\n");
        sb.append("Description: Server will respond with the request image converted into grayscale. This output image will be\n");
        sb.append("saved to the specified location.\n\n");

        sb.append("Red:\n");
        sb.append("Usage: ClientRunner red [image path] [address] [output image path]\n");
        sb.append("Example: ClientRunner red image.png http://localhost:8080/ output.png\n");
        sb.append("Description: Server will respond with the request image's red channel isolated. This output image will be\n");
        sb.append("saved to the specified location. The output image will be in grayscale, with white areas representing strong\n");
        sb.append("red signals, and black areas with weak red signals.\n\n");

        sb.append("Green:\n");
        sb.append("Usage: ClientRunner green [image path] [address] [output image path]\n");
        sb.append("Example: ClientRunner green image.png http://localhost:8080/ output.png\n");
        sb.append("Description: Server will respond with the request image's green channel isolated. This output image will be\n");
        sb.append("saved to the specified location. The output image will be in grayscale, with white areas representing strong\n");
        sb.append("green signals, and black areas with weak green signals.\n\n");

        sb.append("Blue:\n");
        sb.append("Usage: ClientRunner blue [image path] [address] [output image path]\n");
        sb.append("Example: ClientRunner blue image.png http://localhost:8080/ output.png\n");
        sb.append("Description: Server will respond with the request image's blue channel isolated. This output image will be\n");
        sb.append("saved to the specified location. The output image will be in grayscale, with white areas representing strong\n");
        sb.append("blue signals, and black areas with weak blue signals.");


        System.out.println(sb.toString());
    }

    /**
     * Handles the color commands.
     * @param inputImagePath The file path of the input image.
     * @param serverAddress The server address. This should include the last "/" at the end.
     * @param outputImagePath The file path where the output image should be written.
     * @param color The color that will be isolated in the output image.
     */
    private static void handleColor(String inputImagePath, String serverAddress, String outputImagePath, String color){
        int[][][] imageData = Utility.loadImageFromDisk(inputImagePath);

        int[][][] outputImageData = ClientRunner.sendAndReceiveImage(imageData, serverAddress + color + "/");

        if(outputImageData == null){
            System.out.println("Server could not respond with output image. Try again.");
        }


        Utility.saveImageToDisk(outputImageData, outputImagePath);
    }

    /**
     * Handles the stat command.
     * @param inputImagePath The file path of the input image.
     * @param serverAddress The server address. This should include the last "/" character at the end.
     */
    private static void handleStats(String inputImagePath, String serverAddress){
        int[][][] imageData = Utility.loadImageFromDisk(inputImagePath);

        int numRows = imageData.length;
        int numCols = imageData[0].length;
        int numChannels = imageData[0][0].length;

        //Serialize image data
        byte[] byteImageData = Utility.imageToByteArray(imageData);

        HttpResponse<String> response = ClientRunner.sendImagePost(byteImageData, numRows, numCols, numChannels, serverAddress + "stats/");

        String body = response.body();

        if(response == null || response.body() == null){
            System.out.println("Server could not respond with image stats. Try again.");
        }

        System.out.println(body);
        
    }

    /**
     * Sends an image via HTTP request.
     * @param imageDataBytes The image to be sent, represented as an array of bytes.
     * @param numRows The number of rows in the image.
     * @param numCols The number of cols in the image.
     * @param numChannels The number of channels in the image.
     * @param serverAddress The server address to send the image to. Should include the final "/" character at the end.
     * @return The HTTP response
     */
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

    /**
     * Sends and receives image via HTTP
     * @param imageData The image to send
     * @param serverAddress The server address to send the image to. Should include the last "/" character at the end.
     * @return The received image from the server.
     */
    private static int[][][] sendAndReceiveImage(int[][][] imageData, String serverAddress){
        //Serialize image data
        byte[] imageDataBytes = Utility.imageToByteArray(imageData);

        int numRows = imageData.length;
        int numCols = imageData[0].length;
        int numChannels = imageData[0][0].length;

        HttpResponse<String> response = ClientRunner.sendImagePost(imageDataBytes, numRows, numCols, numChannels, serverAddress);

        if(response == null || response.headers() == null || response.body() == null){
            return null;
        }

        HttpHeaders headers = response.headers();

        int[] dimensions = Utility.getImageDimensions(headers);
        String body = response.body();


        if(dimensions != null && dimensions.length == 3){
            numRows = dimensions[0];
            numCols = dimensions[1];
            numChannels = dimensions[2];
        }

        

        byte[] byteImageData = Base64.getDecoder().decode(body);

        int[][][] returnImage = Utility.byteArrayToImageData(byteImageData, numRows, numCols, numChannels);

        return returnImage;
    }

    
}
