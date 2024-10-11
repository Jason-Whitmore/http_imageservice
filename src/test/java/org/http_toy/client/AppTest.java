package org.http_toy.client;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.http_toy.Utility;

import java.util.List;
import java.util.ArrayList;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
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
        
    }

    @Test
    public void testImageToByteArray(){

    }

    @Test
    public void testGetImageDimensions1(){

    }

    @Test
    public void testGetImageDimensions2(){

    }

    @Test
    public void testByteArrayToImageData(){
        
    }

}
