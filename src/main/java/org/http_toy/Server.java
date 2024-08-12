package org.http_toy;


import java.util.Base64;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Server {
    
    public Server(){
        System.out.println("Server started. Any activity will be printed on standard out.");
    }

    @PostMapping("/stats/")
    private String getImageStats(@RequestBody String imgDataString){
        System.out.println("getImageStats invoked via get mapping");

        StringBuilder sb = new StringBuilder();

        sb.append("[STRING]Image statistics:\n");

        //Display image dimensions

        int[][][] imgData = Utility.hexStringToImageData(imgDataString);
        
        int rows = imgData.length;
        sb.append("Number of rows: " + rows + "\n");

        int cols = imgData[0].length;
        sb.append("Number of columns: " + cols + "\n");

        int channels = imgData[0][0].length;
        sb.append("Number of channels: " + channels + "\n");


        //Display color mode

        int[] mostCommonColor = Utility.getMostCommonColor(imgData);

        sb.append("Most common color:\n");
        
        for(int i = 0; i < mostCommonColor.length - 1; i++){
            sb.append("Channel " + i + ": " + mostCommonColor[i] + "\n");
        }

        sb.append("Occurences: " + mostCommonColor[mostCommonColor.length - 1] + "\n\n");


        //Display color mean

        int[] meanColor = Utility.getMeanColor(imgData);

        sb.append("Mean color:\n");

        for(int i = 0; i < meanColor.length; i++){
            sb.append("Channel " + i + ": " + meanColor[i] + "\n");
        }

        return sb.toString();
    }

    @PostMapping("/grey/")
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
        headers.add("numRows", numRows + "");
        headers.add("numCols", numCols + "");
        headers.add("numChannels", 1 + "");

        return new ResponseEntity<>(Base64.getEncoder().encodeToString(imageDataBytes), headers, HttpStatus.ACCEPTED);
    }

    @PostMapping("/red/")
    private ResponseEntity<String> getRedChannels(@RequestHeader Map<String, String> header, @RequestBody String body){

        System.out.println("Processing red request...");

        return Server.getResponseColorIsolation(header, body, 0);
    }

    @PostMapping("/green/")
    private ResponseEntity<String> getGreenChannels(@RequestHeader Map<String, String> header, @RequestBody String body){
        System.out.println("Processing red request...");

        return Server.getResponseColorIsolation(header, body, 1);
    }

    @PostMapping("/blue/")
    private ResponseEntity<String> getBlueChannels(@RequestHeader Map<String, String> header, @RequestBody String body){
        System.out.println("Processing red request...");

        return Server.getResponseColorIsolation(header, body, 2);
    }

    private static ResponseEntity<String> getResponseColorIsolation(@RequestHeader Map<String, String> header, @RequestBody String body, int channel){
        int numRows = Integer.parseInt(header.get("num_rows"));
        int numCols = Integer.parseInt(header.get("num_cols"));
        int numChannels = Integer.parseInt(header.get("num_channels"));

        //Get data from body
        byte[] byteData = Base64.getDecoder().decode(body);

        //Convert to 3d array
        int[][][] imgData = Utility.byteArrayToImageData(byteData, numRows, numCols, numChannels);

        int[][][] redImgData = Utility.isolateChannel(imgData, channel);

        //Create http response
        byte[] imageDataBytes = Utility.imageToByteArray(redImgData);

        HttpHeaders headers = new HttpHeaders();
        headers.add("numRows", numRows + "");
        headers.add("numCols", numCols + "");
        headers.add("numChannels", 1 + "");

        return new ResponseEntity<>(Base64.getEncoder().encodeToString(imageDataBytes), headers, HttpStatus.ACCEPTED);
    }
}
