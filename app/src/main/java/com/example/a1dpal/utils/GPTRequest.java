package com.example.a1dpal.utils;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GPTRequest {
    //INSERT API KEY BELOW
    private static final String API_key = "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";
    //URL to prompt gpt (change if you want to use another model/ if openAI changes their endpoint
    private static final String Endpoint_URL = "https://api.openai.com/v1/chat/completions";
    //Model to use
    private static final String model = "gpt-3.5-turbo";
    private final OkHttpClient client = new OkHttpClient();
    private final Handler handler = new Handler(Looper.getMainLooper());

    public interface ResponseCallback {
        void onResponse(String response);
        void onFailure(String error);
    }

    public void sendGPTRequest(String prompt, ResponseCallback callback) {
        new Thread(() -> {
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}]}");
            Request request = new Request.Builder()
                    .url(Endpoint_URL)
                    .post(body)
                    .addHeader("Authorization", "Bearer " + API_key)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String responseData = response.body().string();
                System.out.println(responseData);
                JSONObject jsonResponse = new JSONObject(responseData);
                String textResponse = jsonResponse.getJSONArray("choices")
                        .getJSONObject(0) // Assuming you want the first choice
                        .getJSONObject("message")
                        .getString("content");

                // Switch back to the main thread to update the UI
                handler.post(() -> callback.onResponse(textResponse));
            } catch (Exception e) {
                e.printStackTrace();
                handler.post(() -> callback.onFailure("Request failed: " + e.getMessage()));
            }
        }).start();
    }
}