package org.http_toy;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

        int[][][] imgData = Utility.hexStringToImageData(imgDataString);
        
        int rows = imgData.length;
        sb.append("Number of rows: " + rows + "\n");

        int cols = imgData[0].length;
        sb.append("Number of columns: " + cols + "\n");

        int[] mostCommonColor = Utility.getMostCommonColor(imgData);

        sb.append("Most common color:\n");
        
        for(int i = 0; i < mostCommonColor.length - 1; i++){
            sb.append("Channel " + i + ": " + mostCommonColor[i] + "\n");
        }

        sb.append("Occurences: " + mostCommonColor[mostCommonColor.length - 1] + "\n");

        return sb.toString();
    }

    @GetMapping("/grayscale/{imgDataString}")
    private String getGrayscale(@PathVariable String imgDataString){
        int[][][] imgData = Utility.hexStringToImageData(imgDataString);

        int[][][] grayImgData = Utility.convertRGBImageToGrayscaleImage(imgData);

        String grayImgDataString = Utility.imageDataToHexString(grayImgData);

        return "[Image]" + grayImgDataString;
    }

    @GetMapping("/red/{imgDataString}")
    private String getRedChannels(@PathVariable String imgDataString){
        int[][][] imgData = Utility.hexStringToImageData(imgDataString);

        int[][][] redImgData = Utility.isolateChannel(imgData, 0);

        String redImgDataString = Utility.imageDataToHexString(redImgData);

        return "[Image]" + redImgDataString;
    }

    @GetMapping("/green/{imgDataString}")
    private String getGreenChannels(@PathVariable String imgDataString){
        int[][][] imgData = Utility.hexStringToImageData(imgDataString);

        int[][][] greenImgData = Utility.isolateChannel(imgData, 1);

        String greenImgDataString = Utility.imageDataToHexString(greenImgData);

        return "[Image]" + greenImgDataString;
    }

    @GetMapping("/blue/{imgDataString}")
    private String getBlueChannels(@PathVariable String imgDataString){
        int[][][] imgData = Utility.hexStringToImageData(imgDataString);

        int[][][] blueImgData = Utility.isolateChannel(imgData, 2);

        String blueImgDataString = Utility.imageDataToHexString(blueImgData);

        return "[Image]" + blueImgDataString;
    }
}
