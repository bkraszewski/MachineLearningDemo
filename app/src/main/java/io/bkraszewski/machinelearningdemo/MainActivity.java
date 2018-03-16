package io.bkraszewski.machinelearningdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.android.gms.internal.zzt.TAG;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        resetButton.setOnClickListener(v -> reset());
        classifyButton.setOnClickListener(v -> {
            Bitmap original = drawingView.getBitmap();
            Bitmap scaled = Bitmap.createScaledBitmap(original, 28, 28, false);
            bitmapTester.setImageBitmap(scaled);
            bitmapTester.setVisibility(View.VISIBLE);
            drawingView.setVisibility(View.GONE);
        });
    }

    private void reset() {
        drawingView.reset();
        bitmapTester.setVisibility(View.GONE);
        drawingView.setVisibility(View.VISIBLE);
    }
}
