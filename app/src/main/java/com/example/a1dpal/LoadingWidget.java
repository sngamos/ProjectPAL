package com.example.a1dpal;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;

public class LoadingWidget extends Dialog {

    private ImageView loadingImage;
    private TextView loadingText;

    public LoadingWidget(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loading_dialog);

        // Make sure the dialog background is transparent to show the rounded corners
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // Set the content view and adjust window parameters
        setContentView(R.layout.loading_dialog);
        WindowManager.LayoutParams windowParams = getWindow().getAttributes();
        windowParams.gravity = Gravity.CENTER; // Set dialog position to center
        windowParams.dimAmount = 0.5f; // Optional: Add a dim behind the dialog
        getWindow().setAttributes(windowParams);
        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);


        loadingImage = findViewById(R.id.loadingImage);
        loadingText = findViewById(R.id.loadingText);

        // Load the GIF using Glide
        Glide.with(context).asGif().load(R.drawable.loading).into(loadingImage);

        // Prevent the dialog from being dismissed by back press or touch
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    public void start() {
        show();
        loadingText.setVisibility(View.VISIBLE);
    }

    public void end() {
        dismiss();
        loadingText.setVisibility(View.GONE);
    }
}
