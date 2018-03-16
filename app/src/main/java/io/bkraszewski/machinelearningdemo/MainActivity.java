package io.bkraszewski.machinelearningdemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends Activity {

    private static final String LOG_TAG = "MainActivity";

    @BindView(R.id.classify)
    protected View classifyButton;

    @BindView(R.id.reset)
    protected View resetButton;

    @BindView(R.id.viewContainer)
    protected ViewGroup viewContainer;

    @BindView(R.id.drawingView)
    protected DrawingView drawingView;

    @BindView(R.id.bitmapTester)
    protected ImageView bitmapTester;

    private Executor executor = Executors.newSingleThreadExecutor();
    private Classifier classifier;

    // tensorflow input and output
    private static final int INPUT_SIZE = 28;
    private static final String INPUT_NAME = "input";
    private static final String OUTPUT_NAME = "output";

    private static final String MODEL_FILE = "file:///android_asset/expert-graph.pb";
    private static final String LABEL_FILE = "file:///android_asset/labels.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        loadModel();

        resetButton.setOnClickListener(v -> reset());
        classifyButton.setOnClickListener(v -> {
            onClassify();
        });
    }

    private void onClassify() {
        Bitmap original = drawingView.getBitmap();
        Bitmap scaled = Bitmap.createScaledBitmap(original, 28, 28, false);
        bitmapTester.setImageBitmap(scaled);
        switchPreviewVisibility();

        int width = 28;
        int[] pixels = new int[width * width];

        scaled.getPixels(pixels, 0, width, 0, 0, width, width);
        float[] retPixels = createInputPixels(pixels);

        int[] again = createSecondPreviewPixels(pixels, retPixels);

        Bitmap preview = Bitmap.createBitmap(again, width, width, Bitmap.Config.RGB_565);
        bitmapTester.setImageBitmap(preview);

        classifyData(retPixels);
    }

    private void classifyData(float[] retPixels) {
        Classification classification = classifier.recognize(retPixels);
        String result = String.format("String a %s with proc: %f", classification.getLabel(), classification.getConf());
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
    }

    private int[] createSecondPreviewPixels(int[] pixels, float[] retPixels) {
        int[] again = new int[pixels.length];
        for (int a = 0; a < pixels.length; a++) {
            again[a] = (int) retPixels[a];
        }
        return again;
    }

    private float[] createInputPixels(int[] pixels) {
        float[] retPixels = new float[pixels.length];

        for (int i = 0; i < pixels.length; ++i) {
            int pix = pixels[i];
            retPixels[i] = pix != 0 ? 255 : 1;
        }
        return retPixels;
    }

    private void switchPreviewVisibility() {
        bitmapTester.setVisibility(View.VISIBLE);
        drawingView.setVisibility(View.GONE);
    }

    private void reset() {
        drawingView.reset();
        bitmapTester.setVisibility(View.GONE);
        drawingView.setVisibility(View.VISIBLE);
    }

    private void loadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = Classifier.create(getApplicationContext().getAssets(),
                            MODEL_FILE,
                            LABEL_FILE,
                            INPUT_SIZE,
                            INPUT_NAME,
                            OUTPUT_NAME);
                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }
            }
        });
    }
}
