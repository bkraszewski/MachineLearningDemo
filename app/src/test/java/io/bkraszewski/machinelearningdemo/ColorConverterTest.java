package io.bkraszewski.machinelearningdemo;

import android.graphics.Color;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ColorConverterTest {

    private static final double ACCEPTED_DELTA = 0.1d;

    @Test
    public void shouldConvertAllBlackPixels() {
        int[] blackPixels = new int[4];
        for (int a = 0; a < blackPixels.length; a++) {
            blackPixels[a] = Color.BLACK;
        }

        float[] tfInput = ColorConverter.convertToTfFormat(blackPixels);
        assertEquals(blackPixels.length, tfInput.length);
        for (int a = 0; a < tfInput.length; a++) {
            assertEquals(1.0f, tfInput[a], ACCEPTED_DELTA);
        }
    }

    @Test
    public void shouldConvertAllWhitePixels() {
        int[] blackPixels = new int[4];
        for (int a = 0; a < blackPixels.length; a++) {
            blackPixels[a] = Color.WHITE;
        }

        float[] tfInput = ColorConverter.convertToTfFormat(blackPixels);
        assertEquals(blackPixels.length, tfInput.length);
        for (int a = 0; a < tfInput.length; a++) {
            assertEquals(0.0f, tfInput[a], ACCEPTED_DELTA);
        }
    }

    @Test
    public void shouldTreatTransparentAsWhite() {
        int[] blackPixels = new int[4];
        for (int a = 0; a < blackPixels.length; a++) {
            blackPixels[a] = Color.TRANSPARENT;
        }

        float[] tfInput = ColorConverter.convertToTfFormat(blackPixels);
        assertEquals(blackPixels.length, tfInput.length);
        for (int a = 0; a < tfInput.length; a++) {
            assertEquals(0.0f, tfInput[a], ACCEPTED_DELTA);
        }
    }
}
