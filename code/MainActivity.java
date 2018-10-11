package sensor.bhargav.com.sensordemo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.RunnableFuture;
import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {
    TextView parking1,parking2,parking3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parking1=(TextView)findViewById(R.id.p1);
        parking2=(TextView)findViewById(R.id.p2);
        parking3=(TextView)findViewById(R.id.p3);
        final String url="http://soilmoisture.hol.es/bhargav/carget.php";
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        final RequestQueue mRequestQueue=new RequestQueue(cache,network);
        mRequestQueue.start();
        final android.os.Handler handler=new android.os.Handler();
        Runnable updateParking=new Runnable() {
            @Override
            public void run() {
                StringRequest serviceRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray ja;
                        try {
                            ja=new JSONArray(response);
                            JSONObject parkingObject1=ja.getJSONObject(0);
                            JSONObject parkingObject2=ja.getJSONObject(1);
                            JSONObject parkingObject3=ja.getJSONObject(2);
                            int parkingValue1=parkingObject1.getInt("senser_data");
                            int parkingValue2=parkingObject2.getInt("senser_data");
                            int parkingValue3=parkingObject3.getInt("senser_data");
                            System.out.println("PARKING1:"+parkingValue1);
                            if(parkingValue1==1){
                                parking1.setBackgroundColor(Color.parseColor("#04e461"));
                            }else if(parkingValue1==0){
                                parking1.setBackgroundColor(Color.parseColor("#00ffd4"));
                            }
                            if(parkingValue2==1){
                                parking2.setBackgroundColor(Color.parseColor("#04e461"));
                            }else if(parkingValue2==0){
                                parking2.setBackgroundColor(Color.parseColor("#00ffd4"));
                            }
                            if(parkingValue3==1){
                                parking3.setBackgroundColor(Color.parseColor("#04e461"));
                            }else if(parkingValue3==0){
                                parking3.setBackgroundColor(Color.parseColor("#00ffd4"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }
                );
                mRequestQueue.add(serviceRequest);

                //Change Here If You Want Currently It Refresh Every 2 Sec
                handler.postDelayed(this,2000);
            }
        };
        //Change Here If You Want Currently It Refresh Every 2 Sec
        handler.postDelayed(updateParking,2000);
    }
}
