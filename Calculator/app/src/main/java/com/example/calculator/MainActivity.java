package com.example.calculator;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
  EditText one ,two;
  Spinner option;
  String operation="";
  int answer=0;
  FrameLayout ans;
    TextView correct,wrong;
    String savedata="http://labconnect.co.ug/calcu/savedata.php";
    String getdata="http://labconnect.co.ug/calcu/getdata.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        one =(EditText)findViewById(R.id.editText);
        two =(EditText)findViewById(R.id.editText2);
        ans=(FrameLayout)findViewById(R.id.ans) ;
        Button calc = (Button) findViewById(R.id.button);
        option = (Spinner) findViewById(R.id.spinner);
        correct=(TextView)findViewById(R.id.correct);
        wrong=(TextView)findViewById(R.id.wrong);
        ans.setVisibility(View.GONE);
        final ArrayAdapter usertype = ArrayAdapter.createFromResource(this, R.array.operation_arrays, android.R.layout.simple_spinner_dropdown_item);
        option.setAdapter(usertype);
        option.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                operation = (String) option.getSelectedItem();
                switch (operation) {
                    case "ADD":
                      //  startfee.setVisibility(View.VISIBLE);
                        operation = "add";
                        answer=Integer.parseInt(one.getText().toString())+Integer.parseInt(two.getText().toString());
                        break;

                    case "MULTIPLY":
                       // startfee.setVisibility(View.GONE);
                       // startfee.setText("0");
                        operation = "multiply";
                        answer=Integer.parseInt(one.getText().toString())*Integer.parseInt(two.getText().toString());

                        break;
                    case "SUBTRACT":
                        // startfee.setVisibility(View.GONE);
                        // startfee.setText("0");
                        operation = "subtract";
                        answer=Integer.parseInt(one.getText().toString())-Integer.parseInt(two.getText().toString());

                        break;
                    case "DIVIDE":
                        // startfee.setVisibility(View.GONE);
                        // startfee.setText("0");
                        operation = "divide";
                        answer=Integer.parseInt(one.getText().toString())/Integer.parseInt(two.getText().toString());

                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                operation = "";
            }
        });
        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ans.setVisibility(View.VISIBLE);
            }
        });
    }
    public void uploaduserimage(){

        final String none = one.getText().toString().trim();
        final String ntwo = two.getText().toString().trim();


        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, savedata, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // pDialog.dismiss();
                Log.i("Myresponse",""+response);
                getAnswer();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  pDialog.dismiss();
                Log.i("Mysmart",""+error);
                error.printStackTrace();
                Toast.makeText(MainActivity.this, "Upload NOT sucessiful", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();


                param.put("ans", String.valueOf(answer));
                param.put("one", none);
                param.put("two", ntwo);



                return param;
            }
        };

        requestQueue.add(stringRequest);


    }

    private void getAnswer() {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getdata, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonobjects = new JSONObject(response);
                    JSONArray jsonarray = jsonobjects.getJSONArray("users");
                    JSONObject jsonObject = jsonarray.getJSONObject(0);



                    final int uid = jsonObject.getInt("id");
                    String one= jsonObject.getString("one");
                    String two = jsonObject.getString("two");
                    String ans = jsonObject.getString("ans");
                    correct.setText("Number One :"+ one +"\n"+"Number Two :"+ two +"\n"+"Response :"+ ans +"\n"+"Expected Response :"+ ans +"\n"+"Passed :  YES");
                    wrong.setText("Number One :"+ one +"\n"+"Number Two :"+ two +"\n"+"Response :"+ two+""+ ans +"\n"+"Expected Response :"+ ans +"\n"+"Passed :  NO");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Something went wrong",Toast.LENGTH_LONG).show();
                error.printStackTrace(); error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();
               // parameters.put("username", String.valueOf(MainAcActivity.user_id));
                return parameters;
            }
        };
        queue.add(stringRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
