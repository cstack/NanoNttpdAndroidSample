package com.example.nanohttpdandroidsample;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

public class MainActivity extends Activity {

    private static final int PORT = 8080;
    private MyHTTPD mServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        TextView textIpaddr = (TextView) findViewById(R.id.ipaddr);
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        final String formatedIpAddress = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        textIpaddr.setText("Please access! http://" + formatedIpAddress + ":" + PORT);

        try {
            mServer = new MyHTTPD();
            mServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class MyHTTPD extends NanoHTTPD {
        public MyHTTPD() throws IOException {
            super(PORT);
        }

        @Override
        public Response serve(String uri, Method method, Map<String, String> header, Map<String,
                String> parms, Map<String, String> files) {
            System.out.println(method + " '" + uri + "' ");

            String msg = "<html><body><h1>Hello server</h1>\n";
            if (parms.get("username") == null)
                msg +=
                        "<form action='?' method='get'>\n" +
                                "  <p>Your name: <input type='text' name='username'></p>\n" +
                                "</form>\n";
            else
                msg += "<p>Hello, " + parms.get("username") + "!</p>";

            msg += "</body></html>\n";

            return new NanoHTTPD.Response(msg);
        }
    }
}
