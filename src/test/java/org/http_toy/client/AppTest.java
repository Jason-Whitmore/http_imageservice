package org.http_toy.client;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.http_toy.Utility;

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
    public void testFlattenImageMethods(){
        int[][][] image = new int[100][150][3];
        Utility.populateArrayRandom(image, 0, 255);

        int[][][] imageCopy = Utility.duplicateArray(image);

        int[] flatImage = Utility.imageToFlatImage(imageCopy);
        
        //convert back into image
        int[][][] newImage = Utility.flatImageToImage(flatImage);

        assertTrue(Utility.isEqual(image, newImage));
    }

    @Test
    public void testbinaryStringTo(){
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
}
