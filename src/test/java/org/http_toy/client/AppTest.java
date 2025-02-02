package org.http_toy.client;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;

import org.http_toy.Utility;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

/**
 * Unit tests for HTTP ImageService
 */
public class AppTest 
{
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void testRGBtoGrayscale(){
        int[][][] rgb = new int[1][1][3];
        rgb[0][0][0] = 6;
        rgb[0][0][1] = 10;
        rgb[0][0][2] = 5;

        int[][][] grayscale = Utility.convertRGBImageToGrayscaleImage(rgb);

        assertTrue(grayscale[0][0][0] == 7);
    }

    @Test
    public void testMostCommonColor(){
        int[][][] image = new int[1][5][];
        image[0][0] = new int[]{1};
        image[0][1] = new int[]{3};
        image[0][2] = new int[]{2};
        image[0][3] = new int[]{4};
        image[0][4] = new int[]{1};

        int[] commonColor = Utility.getMostCommonColor(image);

        int n = commonColor[0];

        assertTrue(n == 1);
    }

    @Test
    public void testMeanColor(){
        int[][][] image = new int[1][5][];
        image[0][0] = new int[]{10};
        image[0][1] = new int[]{5};
        image[0][2] = new int[]{5};
        image[0][3] = new int[]{10};
        image[0][4] = new int[]{5};

        int[] commonColor = Utility.getMeanColor(image);

        int n = commonColor[0];

        assertTrue(n == 7);
    }

    @Test
    public void testArrayToList(){
        int[] input = new int[]{0, 1, 2};

        List<Integer> l = Utility.arrayToList(input);

        assertTrue(l.get(0) == 0);
        assertTrue(l.get(1) == 1);
        assertTrue(l.get(2) == 2);
    }

    @Test
    public void testListToArray(){
        List<Integer> l = new ArrayList<>();

        l.add(0);
        l.add(1);
        l.add(2);

        int[] array = Utility.listToArray(l);

        assertTrue(array[0] == 0);
        assertTrue(array[1] == 1);
        assertTrue(array[2] == 2);
    }

    @Test
    public void testIsolateChannel(){
        int[][][] image = new int[1][1][3];
        image[0][0][0] = 100;
        image[0][0][1] = 200;
        image[0][0][2] = 300;

        int[][][] isolatedImage = Utility.isolateChannel(image, 1);

        assertTrue(isolatedImage[0][0][0] == 200);
    }

    @Test
    public void testImageToByteArray(){
        //Initializing image to be completely black (all zero entries)

        int[][][] image = new int[2][2][3];

        byte[] b = Utility.imageToByteArray(image);
        byte[] target = new byte[b.length];

        assertArrayEquals(target, b);
    }

    @Test
    public void testGetImageDimensions1(){
        Map<String, String> map = new HashMap<>();
        map.put("num_rows", "2");
        map.put("num_cols", "2");
        map.put("num_channels", "3");

        int[] targetDims = new int[]{2,2,3};

        int[] dims = Utility.getImageDimensions(map);

        assertArrayEquals(targetDims, dims);
    }

    @Test
    public void testGetImageDimensions2(){
        int[][][] imageData = new int[2][2][3];
        byte[] imageDataBytes = Utility.imageToByteArray(imageData);
        String imageDataString = Base64.getEncoder().withoutPadding().encodeToString(imageDataBytes);

        String URIString = "http://localhost:8080";
        try {
            
            HttpRequest.Builder builder = HttpRequest.newBuilder().uri(new URI(URIString)).POST(BodyPublishers.ofString(imageDataString));

            builder.header("num_rows", 2 + "");
            builder.header("num_cols", 2 + "");
            builder.header("num_channels", 3 + "");

            HttpRequest req = builder.build();
            int[] output = Utility.getImageDimensions(req.headers());

            int[] target = new int[]{2, 2, 3};

            assertArrayEquals(output, target);
        } catch (Exception e) {
            assertTrue(false);
        }
        
    }

    @Test
    public void testByteArrayToImageData(){
        
        byte[] b = new byte[12];
        int[][][] image = Utility.byteArrayToImageData(b, 2, 2, 3);
        int[][][] target = new int[2][2][3];

        assertArrayEquals(image, target);
    }

}
