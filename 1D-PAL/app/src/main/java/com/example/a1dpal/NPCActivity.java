package com.example.a1dpal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a1dpal.utils.GPTRequest;
import com.example.a1dpal.utils.ImageFetcher;
import com.example.a1dpal.utils.PostAgent;
import com.example.a1dpal.utils.PromptObj;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class NPCActivity extends AppCompatActivity {
    Button btnSubmit;
    Button btnBack;
    EditText promptBox;
    ImageView imageView;
    private OkHttpClient client = new OkHttpClient();
    private WebSocket webSocket;
    private String serverAddress = "https://legal-picked-primate.ngrok-free.app";
    private String websocketAddress = serverAddress.replace("https://", "wss://");
    private String clientId = UUID.randomUUID().toString();
    private String promptId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_npc);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("image");
            if (value != null) {
                ConstraintLayout layout = findViewById(R.id.layout);
                layout.setBackgroundResource(Integer.parseInt(value));
            }
        }

        // Back to background select
        btnBack = findViewById(R.id.backBtn);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NPCActivity.this, MainActivity.class));
            }
        });

        imageView = findViewById(R.id.npcAvatar);
        btnSubmit= findViewById(R.id.submitButton);
        promptBox = findViewById(R.id.inputBox);
        connectWebSocket();
        storyState saveFile = new storyState("", "");
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String userInput = promptBox.getText().toString();

                    //Request to Stable Diffusion
                    if (webSocket != null && !userInput.isEmpty()) {
                        try {
                            String PromptID = sendInputToServer(userInput);
                            Log.d("prompt id",PromptID);
                            promptId = PromptID;
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }

                    TextView responseTextView = findViewById(R.id.responseView);
                    Log.i("saved summary", saveFile.getSummary());
                    new GPTRequest().sendGPTRequest(saveFile.inputWrapper(userInput), new GPTRequest.ResponseCallback() {
                        @Override
                        public void onResponse(String response) {
                            /*responseTextView.setText(response);*/
                            try {
                                JSONObject json = new JSONObject(response);
                                String json_story = json.getString("story");
                                String json_emotion = json.getString("ganyu_emotion");
                                String json_summary = json.getString("summary");
                                Log.i("gpt output" ,json_story + json_emotion + json_summary);
                                Log.i("summary" , json_summary);
                                saveFile.setEmotion(json_emotion);
                                saveFile.setSummary(json_summary);
                                saveFile.setStory(json_story);
                                Log.i("saveState", saveFile.getEmotion() + saveFile.getSummary());
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            responseTextView.setText(saveFile.getStory());
                            responseTextView.setVisibility(View.VISIBLE);
                            new CountDownTimer(10000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                }

                                @Override
                                public void onFinish() {
                                    responseTextView.setText("");
                                    responseTextView.setVisibility(View.INVISIBLE);
                                }
                            }.start();
                        }

                        @Override
                        public void onFailure(String error) {
                            responseTextView.setText(error);
                            responseTextView.setVisibility(View.VISIBLE);
                            new CountDownTimer(5000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                }

                                @Override
                                public void onFinish() {
                                    responseTextView.setText("");
                                    responseTextView.setVisibility(View.INVISIBLE);}
                            }.start();
                        }
                    });
                    promptBox.setText("");
                } catch (Exception e) {
                    System.out.println(e);
                }

            }
        });
    }

    private void connectWebSocket() {
        Request request = new Request.Builder().url(websocketAddress + "/ws?clientId=" + clientId).build();
        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, okhttp3.Response response) {
                super.onOpen(webSocket, response);
                Log.d("WebSocket", "Connection opened");
            }
            @Override
            public void onMessage(WebSocket webSocket, String text) {
                // Handle messages received from the server
                Log.d("WebSocket", "Receiving : " + text);
                try {
                    JSONObject jsonObject = new JSONObject(text);
                    String messageType = jsonObject.getString("type");
                    JSONObject data = jsonObject.optJSONObject("data");
                    if ("executing".equals(messageType) && data != null) {
                        boolean nodeIsNull = data.isNull("node");
                        String receivedPromptId = data.optString("prompt_id", "");
                        Log.d("recieved prompt id",receivedPromptId);
                        // Check if execution is complete
                        if (nodeIsNull && promptId.equals(receivedPromptId)) {
                            Log.d("WebSocket", "Execution done for prompt ID: " + promptId);
                            handleImageFetching(promptId);

                        }
                    }
                } catch (JSONException e) {
                    Log.e("WebSocket", "Error parsing message", e);
                }
            }
        });

        // Keep the WebSocket open
        client.dispatcher().executorService().shutdown();
    }

    private String sendInputToServer(String userInput) throws Exception {
        // Prepare your prompt as a JSON string
        PromptObj promptobj = new PromptObj("Ganyu",userInput);
        String prompt = promptobj.readJsonAndModify(this, clientId);
        Log.d("prompt",prompt);
        //Log.d("websocket prompt",promptJson);
        ExecutorService executor = Executors.newCachedThreadPool(); // Create an executor
        Future<String> future = executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                PostAgent agent = new PostAgent();
                String promptID = agent.sendPostRequest(prompt);
                return promptID;
            }
        });
        // Get the result from the Future object. Note: This call is blocking.
        Log.d("WebSocket", "Sending : " + userInput);
        return future.get();
    }
    private void handleImageFetching(String promptId) {
        // Assuming 'this' is a Context (e.g., Activity)
        ImageFetcher imageFetcher = new ImageFetcher(this, this.serverAddress);

        imageFetcher.fetchImages(promptId, new ImageFetcher.ImageFetchListener() {
            @Override
            public void onImageSaved(String imagePath) {
                // This callback will be on a background thread. To update the UI, switch to the main thread.
                runOnUiThread(() -> {
                    // Display the image in imageView
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    imageView.setImageBitmap(bitmap);
                });
            }

            @Override
            public void onError(Exception e) {
                // Handle any errors here, e.g., by showing a toast. Remember to switch to the main thread for UI operations.
                runOnUiThread(() -> {
                    Toast.makeText(NPCActivity.this, "Error fetching image: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close WebSocket connection when the activity is destroyed
        if (webSocket != null) {
            webSocket.close(1000, "Activity Destroyed");
        }
    }

}