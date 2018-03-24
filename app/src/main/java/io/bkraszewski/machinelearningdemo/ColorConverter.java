package io.bkraszewski.machinelearningdemo;

import android.graphics.Color;

public class ColorConverter {
    public static float[] convertToTfFormat(int[] argbPixels) {
        float[] ret = new float[argbPixels.length];
        for (int i = 0; i < argbPixels.length; i++) {
            int aargbPixel = argbPixels[i];
            int alpha = (aargbPixel >> 24) & 0xff;
            int r = (aargbPixel >> 16) & 0xff;
            int g = (aargbPixel >> 8) & 0xff;
            int b = aargbPixel & 0xff;

            if (alpha == 0) {
                ret[i] = 0.0f;
                continue;
            }

            int avg = (r + g + b) / 3;

            float grayscaled = avg / 255.0f;
            grayscaled = grayscaled * (alpha / 255.0f);

            ret[i] = 1.0f - grayscaled;
        }

        return ret;
    }

    public static int tfToPixel(float retPixel) {
        int gray = (int) (255 * retPixel);
        gray = 255 - gray;
        return Color.argb(255, gray, gray, gray);
    }
}
