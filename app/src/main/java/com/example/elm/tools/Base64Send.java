package com.example.elm.tools;

import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Base64Send extends AsyncTask<String, Void, String> {

    private String head, body;

    @Override
    protected String doInBackground(String... file) {
        this.head = file[0];
        this.body = file[1];


        return null;
    }
}
