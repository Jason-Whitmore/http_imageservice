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
    public void testImageSavingMethods(){
        int[][][] img = new int[500][150][3];
        Utility.populateArrayRandom(img, 0, 255);

        String imgHexString = Utility.imageDataToHexString(img);

        int[][][] imgReconstructed = Utility.hexStringToImageData(imgHexString);

        assertTrue(Utility.isEqual(img, imgReconstructed));
    }

    @Test
    public void testbinaryStringToInteger1(){
        //Create a simple binary string
        //00001001 binary is 9 decimal
        String s = "00001001";

        int answer = Utility.binaryStringToInteger(s);

        assertTrue(answer == 9);
    }

    @Test
    public void testbinaryStringToIntegerArray2(){
        //Create a simple binary string
        //00001001 binary is 9 decimal
        //00101101 binary is 1 + 4 + 8 + 32 = 45
        String s = "00001001" + "00101101";

        int[] intArray = Utility.binaryStringToIntegerArray(s);

        assertTrue(Utility.isEqual(intArray, new int[]{9, 45}));
    }

    @Test
    public void testintegerToHex1(){
        int a = 10;

        assertTrue(Utility.integerToHex(a) == 'a');
    }

    @Test
    public void testintegerToHex2(){
        int a = 9;

        assertTrue(Utility.integerToHex(a) == '9');
    }

    @Test
    public void testintegerToHex3(){
        int a = 15;

        assertTrue(Utility.integerToHex(a) == 'f');
    }

    @Test
    public void testhexToInteger1(){
        char hex = 'a';

        assertTrue(Utility.hexToInteger(hex) == 10);
    }

    @Test
    public void testhexToInteger2(){
        char hex = '5';

        assertTrue(Utility.hexToInteger(hex) == 5);
    }

    @Test
    public void testhexToInteger3(){
        char hex = 'c';

        assertTrue(Utility.hexToInteger(hex) == 12);
    }

    @Test
    public void testbinaryStringToHexString1(){
        //Should convert to fa
        String s = "0000111100001010";

        String answer = "0f0a";

        String output = Utility.binaryStringToHexString(s);

        assertTrue(output.equals(answer));
    }

    @Test
    public void testhexStringToBinaryString1(){
        //Binary string should convert to 11111010
        String s = "0f0a";

        String answer = "0000111100001010";

        String output = Utility.hexStringToBinaryString(s);

        assertTrue(output.equals(answer));
    }

    @Test
    public void testintegerToBinaryString1(){
        int input = 0;

        String answer = "0000";

        String output = Utility.integerToBinaryString(input, 4);

        assertTrue(output.equals(answer));
    }

    @Test
    public void testintegerToBinaryString2(){
        int input = 2;

        String answer = "0010";

        String output = Utility.integerToBinaryString(input, 4);

        assertTrue(output.equals(answer));
    }

    @Test
    public void testintegerToBinaryString3(){
        int input = 15;

        String answer = "1111";

        String output = Utility.integerToBinaryString(input, 4);

        assertTrue(output.equals(answer));
    }

    @Test
    public void testintegerToBinaryString4(){
        int input = 255;

        String answer = "11111111";

        String output = Utility.integerToBinaryString(input, 8);

        assertTrue(output.equals(answer));
    }

    @Test
    public void testgetMostCommonColor1(){

        //Array of all zeros, most common "color" is [0,0,0]
        int[][][] input = new int[10][10][3];

        int[] answer = new int[]{0,0,0,100};

        int[] output = Utility.getMostCommonColor(input);

        assertTrue(Utility.isEqual(answer, output));
    }

    @Test
    public void testgetMeanColor1(){
        int[][][] imgData = new int[3][1][1];

        imgData[0][0][0] = 1;
        imgData[1][0][0] = 2;
        imgData[2][0][0] = 3;

        int[] answer = new int[]{2};

        int[] output = Utility.getMeanColor(imgData);

        assertTrue(Utility.isEqual(output, answer));
    }

    @Test
    public void testgetMostCommonColor2(){

        int[][][] input = new int[10][10][3];

        Utility.populateArray(input, 2);

        int[] answer = new int[]{2,2,2, 100};

        int[] output = Utility.getMostCommonColor(input);

        assertTrue(Utility.isEqual(answer, output));
    }

    @Test
    public void testarrayToList1(){
        int[] input = new int[]{1,2,3,4};

        List<Integer> answer = new ArrayList<>();
        answer.add(1);
        answer.add(2);
        answer.add(3);
        answer.add(4);

        List<Integer> output = Utility.arrayToList(input);

        assertTrue(answer.equals(output));
    }


    @Test
    public void testlistToArray1(){
        List<Integer> input = new ArrayList<>();
        input.add(5);
        input.add(4);
        input.add(3);

        int[] answer = new int[]{5, 4, 3};

        int[] output = Utility.listToArray(input);

        assertTrue(Utility.isEqual(answer, output));
    }

    @Test
    public void testisolateChannel1(){
        int[][][] input = new int[10][10][3];
        Utility.populateArray(input, 2);

        int[][][] answer = new int[10][10][1];
        Utility.populateArray(answer, 2);

        int[][][] output = Utility.isolateChannel(input, 0);

        assertTrue(Utility.isEqual(answer, output));

    }

    @Test
    public void testisolateChannel2(){
        int[][][] input = new int[1][1][3];
        input[0][0][0] = 0;
        input[0][0][1] = 1;
        input[0][0][2] = 2;

        int[][][] answer = new int[1][1][1];
        answer[0][0][0] = 1;

        int[][][] output = Utility.isolateChannel(input, 1);

        assertTrue(Utility.isEqual(answer, output));
    }

    @Test
    public void testgetSubarray1(){
        int[] input = new int[]{0,1,2,3};

        int[] answer = new int[]{1,2};

        int[] output = Utility.getSubarray(input, 1, 2);

        assertTrue(Utility.isEqual(answer, output));
    }
}
