package org.http_toy;


import java.util.Base64;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


/**
 * Defines the Server class which handles requests from clients and sends responses back.
 */
@RestController
public class Server {
    
    /**
     * Initializes the server.
     */
    public Server(){
        System.out.println("Server started. Any activity will be printed on standard out.");
    }

    /**
     * Handles the stats request
     * @param headers The HTTP headers containing image metadata: num_rows, num_cols, num_channels
     * @param imgDataString The image as a data string
     * @return The response to the client
     */
    @PostMapping("/stats/")
    private ResponseEntity<String> getImageStats(@RequestHeader Map<String, String> headers, @RequestBody String imgDataString){
        System.out.println("getImageStats invoked");

        StringBuilder sb = new StringBuilder();

        sb.append("Image statistics:\n");

        //Display image dimensions

        byte[] byteData = Base64.getDecoder().decode(imgDataString);

        int[] dimensions = Utility.getImageDimensions(headers);
        
        int rows = dimensions[0];
        sb.append("Number of rows: " + rows + "\n");

        int cols = dimensions[1];
        sb.append("Number of columns: " + cols + "\n");

        int channels = dimensions[2];
        sb.append("Number of channels: " + channels + "\n\n");

        int[][][] imageData = Utility.byteArrayToImageData(byteData, rows, cols, channels);


        //Display color mode

        int[] mostCommonColor = Utility.getMostCommonColor(imageData);

        sb.append("Most common color:\n");
        
        for(int i = 0; i < mostCommonColor.length - 1; i++){
            sb.append("Channel " + i + ": " + mostCommonColor[i] + "\n");
        }

        sb.append("Occurrences: " + mostCommonColor[mostCommonColor.length - 1] + "\n\n");


        //Display color mean

        int[] meanColor = Utility.getMeanColor(imageData);

        sb.append("Mean color:\n");

        for(int i = 0; i < meanColor.length - 1; i++){
            sb.append("Channel " + i + ": " + meanColor[i] + "\n");
        }

        sb.append("Channel " + (meanColor.length - 1) + ": " + meanColor[(meanColor.length - 1)]);

        return new ResponseEntity<String>(sb.toString(), new HttpHeaders(), HttpStatus.ACCEPTED);
    }

    /**
     * Handles the request to return the image as a grayscale image
     * @param header The HTTP header containing image metadata: num_rows, num_cols, num_channels
     * @param body The HTTP body containing the image data
     * @return The HTTP response to the client.
     */
    @PostMapping("/gray/")
    private ResponseEntity<String> getGrayscale(@RequestHeader Map<String, String> header, @RequestBody String body){

        System.out.println("Processing gray request...");

        //Get metadata from headers
        int numRows = Integer.parseInt(header.get("num_rows"));
        int numCols = Integer.parseInt(header.get("num_cols"));
        int numChannels = Integer.parseInt(header.get("num_channels"));

        //Get data from body
        byte[] byteData = Base64.getDecoder().decode(body);

        //Convert to 3d array
        int[][][] imgData = Utility.byteArrayToImageData(byteData, numRows, numCols, numChannels);

        //Convert to grayscale
        int[][][] grayImgData = Utility.convertRGBImageToGrayscaleImage(imgData);

        //Create http response
        byte[] imageDataBytes = Utility.imageToByteArray(grayImgData);

        HttpHeaders headers = new HttpHeaders();
        headers.add("num_rows", numRows + "");
        headers.add("num_cols", numCols + "");
        headers.add("num_channels", 1 + "");

        return new ResponseEntity<>(Base64.getEncoder().encodeToString(imageDataBytes), headers, HttpStatus.ACCEPTED);
    }

    /**
     * Handles the request to return the image with red channels isolated (black = no red, white = full red)
     * @param header The HTTP header containing image metadata: num_rows, num_cols, num_channels
     * @param body The HTTP body containing the image data
     * @return The HTTP response to the client.
     */
    @PostMapping("/red/")
    private ResponseEntity<String> getRedChannels(@RequestHeader Map<String, String> header, @RequestBody String body){

        System.out.println("Processing red request...");

        return Server.getResponseColorIsolation(header, body, 0);
    }

    /**
     * Handles the request to return the image with green channels isolated (black = no green, white = full green)
     * @param header The HTTP header containing image metadata: num_rows, num_cols, num_channels
     * @param body The HTTP body containing the image data
     * @return The HTTP response to the client.
     */
    @PostMapping("/green/")
    private ResponseEntity<String> getGreenChannels(@RequestHeader Map<String, String> header, @RequestBody String body){
        System.out.println("Processing green request...");

        return Server.getResponseColorIsolation(header, body, 1);
    }

    /**
     * Handles the request to return the image with blue channels isolated (black = no blue, white = full blue)
     * @param header The HTTP header containing image metadata: num_rows, num_cols, num_channels
     * @param body The HTTP body containing the image data
     * @return The HTTP response to the client.
     */
    @PostMapping("/blue/")
    private ResponseEntity<String> getBlueChannels(@RequestHeader Map<String, String> header, @RequestBody String body){
        System.out.println("Processing blue request...");

        return Server.getResponseColorIsolation(header, body, 2);
    }

    /**
     * Helper function for isolating a color from an image.
     * @param header The HTTP header containing image metadata: num_rows, num_cols, num_channels
     * @param body The HTTP body containing the image data.
     * @param channel The channel to isolate. 0 for red, 1 for green, 2 for blue.
     * @return The HTTP response entity.
     */
    private static ResponseEntity<String> getResponseColorIsolation(@RequestHeader Map<String, String> header, @RequestBody String body, int channel){
        int numRows = Integer.parseInt(header.get("num_rows"));
        int numCols = Integer.parseInt(header.get("num_cols"));
        int numChannels = Integer.parseInt(header.get("num_channels"));

        //Get data from body
        byte[] byteData = Base64.getDecoder().decode(body);

        //Convert to 3d array
        int[][][] imgData = Utility.byteArrayToImageData(byteData, numRows, numCols, numChannels);

        int[][][] colorImgData = Utility.isolateChannel(imgData, channel);

        //Create http response
        byte[] imageDataBytes = Utility.imageToByteArray(colorImgData);

        HttpHeaders headers = new HttpHeaders();
        headers.add("num_rows", numRows + "");
        headers.add("num_cols", numCols + "");
        headers.add("num_channels", 1 + "");

        return new ResponseEntity<>(Base64.getEncoder().encodeToString(imageDataBytes), headers, HttpStatus.ACCEPTED);
    }
}
