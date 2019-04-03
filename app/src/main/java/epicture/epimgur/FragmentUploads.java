package epicture.epimgur;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;

public class FragmentUploads extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST = 100;

    public FragmentUploads() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.uploads_fragment, container, false);
        final GridView gridView = view.findViewById(R.id.gridview_uploads);
        User user = (User) Objects.requireNonNull(getActivity()).getIntent().getSerializableExtra("USER");

        // Floating upload button opening phone gallery
        FloatingActionButton fab = view.findViewById(R.id.upload_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestStoragePermission();
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                final int ACTIVITY_SELECT_IMAGE = 1234;
                startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
            }
        });


        // Get Uploaded images
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.imgur.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ImgurAPI imgurAPI = retrofit.create(ImgurAPI.class);
        Call<UploadsResponse> call = imgurAPI.getUploads("Bearer " + user.access_token);

        call.enqueue(new Callback<UploadsResponse>() {
            @Override
            public void onResponse(Call<UploadsResponse> call, Response<UploadsResponse> response) {
                if (!response.isSuccessful()) {
                    Log.e("Epimgur", response.message());
                    return;
                }
                List<String> imagesLink = new ArrayList<>();
                assert response.body() != null;
                for (Image image : response.body().data) {
                    imagesLink.add(image.link);
                }
                gridView.setAdapter(new ImageAdapter(getActivity(), imagesLink));
            }

            @Override
            public void onFailure(Call<UploadsResponse> call, Throwable t) {
                Log.e("Epimgur", t.getMessage());
            }
        });
        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 1234:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    options.inJustDecodeBounds = false;
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

                    Log.d("Epimgur", filePath);

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    String base64img = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

                    User user = (User) Objects.requireNonNull(getActivity()).getIntent().getSerializableExtra("USER");
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://api.imgur.com/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    ImgurAPI imgurAPI = retrofit.create(ImgurAPI.class);
                    Call<UploadsResponse> call = imgurAPI.uploadImage("Client-ID 762fa603b3aba63", "Bearer " + user.access_token, base64img);

                    call.enqueue(new Callback<UploadsResponse>() {
                        @Override
                        public void onResponse(Call<UploadsResponse> call, Response<UploadsResponse> response) {
                            if (!response.isSuccessful()) {
                                Log.e("Epimgur", response.message());
                                return;
                            }
                            Log.e("Epimgur", response.toString());
                        }

                        @Override
                        public void onFailure(Call<UploadsResponse> call, Throwable t) {
                            Log.e("Epimgur", t.getMessage());
                        }
                    });
                }
        }

    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST);
        }
    }

}
