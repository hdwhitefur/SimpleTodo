package edu.unc.hdwhite.simpletodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
//YoYOYO
public class MainActivity extends AppCompatActivity {
    ArrayList<String> items = new ArrayList<>();
    ArrayAdapter<String> itemAdapter;
    ListView lvItems;
//second try
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView)findViewById(R.id.lvItems);
        itemAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemAdapter);
        items.add("Test1");
        items.add("Test2");
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
        try {
            URL url = new URL("https://www.googleapis.com/books/v1/volumes?q=" + query);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            //readStream(in);
            urlConnection.disconnect();
        } catch(Exception e) {
            //http://developer.android.com/training/basics/network-ops/connecting.html
            //http://developer.android.com/reference/java/net/HttpURLConnection.html
        }
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
}
