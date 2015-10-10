package edu.unc.hdwhite.simpletodo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
//YoYOYO
//what the FUCK
public class MainActivity extends AppCompatActivity {
    ArrayList<String> items = new ArrayList<>();
    ArrayAdapter<String> itemAdapter;
    ListView lvItems;
    String output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView)findViewById(R.id.lvItems);
        itemAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemAdapter);
        setupListViewListener();
    }

    public void onAddItem(View v){
        EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemAdapter.add(itemText);
        etNewItem.setText("");
    }

    public void onSearch(View v){
        String query = ((EditText)findViewById(R.id.etNewItem)).getText().toString();
        output = "FAILURE";
        new WebpageDownload().execute("http://www.google.com");
        Log.d("SUCCESS", output);
    }

    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        items.remove(pos);
                        itemAdapter.notifyDataSetChanged();
                        return true;
                    }
                }
        );
    }

    private class WebpageDownload extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return downloadUrl(urls[0]);
        }

        private String downloadUrl(String urlText) {
            InputStream in = null;
            int len = 500;
            try {
                URL url = new URL(urlText);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                Log.d("Debug", "Opened connection");
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                Log.d("Debug", "Set paramas");
                conn.connect();
                Log.d("Debug", "Connected");
                int response = conn.getResponseCode();
                Log.d("Debug", "The response is: " + response);
                in = conn.getInputStream();
                output = readIt(in, len);
                Log.d("Debug", "Read is: " + output);
                return output;
            } catch (Exception e) {
                Log.d("Debug", "Caught an exception");
                e.printStackTrace();
            } finally {
                if(in != null) {
                    try {in.close();}
                    catch (Exception e){}
                }
            }
            return "ERROR";
        }

        private String readIt(InputStream stream, int len) throws IOException {
            Log.d("Debug", "Made it to the read");
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }
    }
}