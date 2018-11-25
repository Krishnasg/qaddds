package com.google.android.gms.samples.vision.barcodereader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.samples.vision.barcodereader.api.PostAdd;
import com.google.android.gms.samples.vision.barcodereader.api.ServiceGenerator;
import com.google.android.gms.samples.vision.barcodereader.api.requests.postadd.PostAddResponse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int PICK_IMAGE = 1;
    private EditText postText;
    private EditText urlText;
    private SharedPreferences sharedPreferences;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        findViewById(R.id.advertiseButton).setOnClickListener(this);
        findViewById(R.id.textView).setOnClickListener(this);
        findViewById(R.id.advertiseImage).setOnClickListener(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        postText = findViewById(R.id.editPostText);
        urlText = findViewById(R.id.editUrl);
        view = findViewById(R.id.textUrl);

        urlText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    Network.setUrl(s.toString());
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        });
        String url = sharedPreferences.getString("url", "");
        Network.setUrl(url);
        //ServiceGenerator.setUrl(url);
        // http://192.168.1.9:3008/
    }

    @Override public void onDestroy() {
        super.onDestroy();

        // Set the url
        if ( urlText.getText()!= null ) sharedPreferences.edit().putString("url",urlText.getText().toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.advertiseButton:
                sendPostAddData(postText.getText().toString(), null);
                break;
            case R.id.textView:
                view.setVisibility(view.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                break;
            case R.id.advertiseImage:
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);
                break;
        }
    }


    /**
     * User login
     */
    public void sendPostAddData(String text, String imageFilePath) {

        // use the FileUtils to get the actual file by uri
        List<MultipartBody.Part> postAddFiles = new ArrayList();
        if ( imageFilePath != null && !imageFilePath.isEmpty() ) {
            RequestBody requestFile;
            File file = new File(imageFilePath);
            if ( file.exists() ) {
                requestFile =
                        RequestBody.create(
                                MediaType.parse(FileUtils.getMimeType(file)),
                                file
                        );
                postAddFiles.add(MultipartBody.Part.createFormData("imageFile", file.getName(), requestFile));
            }
        }

        // User login with emailID and Password
        PostAdd postAddClient = ServiceGenerator.createService(PostAdd.class);
        Call<PostAddResponse> call = postAddClient.postAdd("some data", text, postAddFiles);

        call.enqueue(new Callback<PostAddResponse>() {
            @Override
            public void onResponse(Call<PostAddResponse> call, Response<PostAddResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PostActivity.this, response.body().getMessage(),  Toast.LENGTH_SHORT).show();
                    goToPostSucessActivity();
                } else {

                }
            }

            @Override
            public void onFailure(Call<PostAddResponse> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(PostActivity.this,  "Seems to have internet connection problem", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PostActivity.this, "Unknown error.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                // Get the url from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // Get the path from the Uri
                    String path = getPathFromURI(selectedImageUri);
                    sendPostAddData("", path);
                    // Log.i(TAG, "Image Path : " + path);
                }
            }
        }
    }

    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private void goToPostSucessActivity() {
        Intent intent = new Intent(this, PostSucessActivity.class);
        startActivity(intent);
    }
}
