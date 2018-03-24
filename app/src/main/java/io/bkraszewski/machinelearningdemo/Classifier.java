package io.bkraszewski.machinelearningdemo;

import android.content.res.AssetManager;
import android.util.Log;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Changed from https://github.com/MindorksOpenSource/AndroidTensorFlowMNISTExample/blob/master/app/src/main/java/com/mindorks/tensorflowexample/TensorFlowImageClassifier.java
 * Created by marianne-linhares on 20/04/17.
 */

public class Classifier {

    // Only returns if at least this confidence
    private static final float THRESHOLD = 0.1f;
    private static final String TAG = "Classifier";

    private TensorFlowInferenceInterface tfHelper;

    private String inputName;
    private String outputName;
    private int inputSize;

    private List<String> labels;
    private float[] output;
    private String[] outputNames;

    static private List<String> readLabels(Classifier c, AssetManager am, String fileName) throws IOException {
        BufferedReader br = null;
        br = new BufferedReader(new InputStreamReader(am.open(fileName)));

        String line;
        List<String> labels = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            labels.add(line);
        }

        br.close();
        return labels;
    }


    static public Classifier create(AssetManager assetManager, String modelPath, String labelPath,
                                    int inputSize, String inputName, String outputName)
            throws IOException {

        Classifier classifier = new Classifier();

        classifier.inputName = inputName;
        classifier.outputName = outputName;

        String labelFile = labelPath.split("file:///android_asset/")[1];
        classifier.labels = readLabels(classifier, assetManager, labelFile);

        classifier.tfHelper = new TensorFlowInferenceInterface(assetManager, modelPath);

        int numClasses = 10;

        classifier.inputSize = inputSize;
        classifier.outputNames = new String[]{outputName};

        classifier.outputName = outputName;
        classifier.output = new float[numClasses];

        return classifier;
    }

    public Classification recognize(final float[] pixels) {
        tfHelper.feed(inputName, pixels, new long[]{inputSize * inputSize});
        tfHelper.run(outputNames);
        tfHelper.fetch(outputName, output);

        // Find the best classification
        Classification ans = new Classification();
        for (int i = 0; i < output.length; ++i) {
            Log.d(TAG, String.format("Class: %s Conf: %f", labels.get(i), output[i]));
            if (output[i] > THRESHOLD && output[i] > ans.getConf()) {
                ans.update(output[i], labels.get(i));
            }
        }

        return ans;
    }
}
