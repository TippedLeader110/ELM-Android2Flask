package com.example.elm.main_menu.ui.prediksi;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.elm.R;
import com.example.elm.databinding.FragmentDashboardBinding;
import com.example.elm.tools.Base64Send;
import com.google.android.material.snackbar.Snackbar;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class PrediksiFragment extends Fragment {

    private PrediksiViewModel prediksiViewModel;
    private FragmentDashboardBinding binding;

    ImageView head, body;
    Uri uHead, uBody;
    Boolean headActivity;
    Bitmap bHead, bBody;
    Button prediksi;
    String hasilMata, hasilBody;
    ProgressDialog progress;
    Boolean alowHead, alowBody;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Context context = getContext();

        alowHead = false;
        alowBody = false;
        View root = inflater.inflate(R.layout.fragment_prediksi, container, false);
        head = root.findViewById(R.id.p_mata_ikan_img);
        body = root.findViewById(R.id.p_tipe_ikan_img);
        headActivity = false;
        progress = new ProgressDialog(context);

        prediksi = root.findViewById(R.id.p_prediksi_start);
        checkImg();

        body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON).start(context, PrediksiFragment.this);
            }
        });

        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                headActivity = true;
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON).start(context, PrediksiFragment.this);
            }
        });

        prediksi.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {

                progress.setTitle("Loading !!(1/4)");
                progress.setMessage("Mengupload gambar...");
                progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                progress.show();

                String mmata, mbody;
                mmata = BitmapToBase64(bHead);
                mbody = BitmapToBase64(bHead);;

                progress.setTitle("Loading !!");
                progress.setMessage("Mengupload gambar...");
                RequestQueue queue = Volley.newRequestQueue(getContext());
                String url ="http://192.168.43.90:5000/mata/";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progress.setMessage("Memproses gambar...");
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response);
                                    hasilMata = jsonObject.getString("mata");
                                    hasilBody = jsonObject.getString("body");
                                    int status = Integer.valueOf(jsonObject.getString("status"));

                                    Log.w("Head", hasilMata);
                                    Log.w("Body", hasilBody);
                                    DialogResult(hasilBody, hasilMata, status);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                progress.dismiss();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        Log.w("Volley Response Error", String.valueOf(error));
                    }
                }
                ){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("mata",mmata);
                        params.put("body",mbody);
                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("Content-Type","application/x-www-form-urlencoded");
                        return params;
                    }
                };
                // Add the request to the RequestQueue.
                queue.add(stringRequest);

            }
        });

        return root;
    }

    public void checkImg(){
        if(alowBody && alowHead){
            prediksi.setEnabled(true);
        }else{
            prediksi.setEnabled(false);
        }
    }

    private void DialogResult(String ikan, String kondisi, Integer status) {
        dialog = new AlertDialog.Builder(getContext());
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.prediksi_result, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        if(status==1){
            dialog.setIcon(R.drawable.ic_baseline_block_24);
        }else{
            dialog.setIcon(R.drawable.ic_baseline_done_24);
        }

        dialog.setTitle("Hasil Prediksi");

        TextView t_ikan    = (TextView) dialogView.findViewById(R.id.prediksi_r_t1);
        TextView t_kondisi    = (TextView) dialogView.findViewById(R.id.prediksi_r_t2);

        t_ikan.setText("Jenis ikan : " + ikan);
        t_kondisi.setText("Kondisi ikan : " + kondisi);

        dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                if(headActivity){
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Uri uri = result.getUri();
                    head.setImageURI(uri);
                    uHead = uri;
                    Log.w("Uri Head", String.valueOf(uri));
                    headActivity = false;
                    try {
                        bHead = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    alowHead = true;
                    checkImg();

                }else{
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Uri uri = result.getUri();
                    body.setImageURI(uri);
                    uBody = uri;
                    Log.w("Uri Body", String.valueOf(uri));
                    headActivity = false;
                    try {
                        bBody = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    alowBody = true;
                    checkImg();
                }
            }
        }else{
            Toast.makeText(getContext(), "Pengambilan gambar dibatalkan", Toast.LENGTH_SHORT).show();
        }
    }

    public String BitmapToBase64(Bitmap bb){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bb.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}