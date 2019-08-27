package android.example.com.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // a numeric code to identify the edit activity
    public final static int EDIT_REQUEST_CODE = 20;
    // keys used for passing data between activities
    public final static String ITEM_TEXT = "itemText";
    public final static String ITEM_POSITION = "itemPosition";
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Read in items from file system
        readItems();
        // Initialize variables
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems = (ListView) findViewById(R.id.lvitems);
        lvItems.setAdapter(itemsAdapter);

        // Allows user to remove to-do items
        setupListViewListener();
    }

    public void onAddItem(View v) {
        EditText etNewItem = findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();

        // Add Item to to-do list
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
        Toast.makeText(getApplicationContext(), "Item added to list.", Toast.LENGTH_SHORT).show();
    }

    private void setupListViewListener() {
        Log.i("MainActivity", "Setting up listener on ListView.");

        // set up item listener for remove (long click)
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("MainActivity", "Item removed from list at: " + position);

                // Remove an item from to do list
                items.remove(position);
                itemsAdapter.notifyDataSetChanged(); // need to notify adapter that data changed
                writeItems();
                return true;
            }
        });

        // set up item listener for edit (regular click)
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // create the new activity
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                // pass the data being edited
                i.putExtra(ITEM_TEXT, items.get(position));
                i.putExtra(ITEM_POSITION, position);
                // display the activity
                startActivityForResult(i, EDIT_REQUEST_CODE);
            }
        });
    }

    // handlre results from edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("MainActivity", "data returned to main activity");
        // if the edit activity completed
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
            Log.i("MainActivity", "Result code okay");
            // extract updated item text
            String updatedItemText = data.getExtras().getString(ITEM_TEXT);

            // extract original position of edited item
            int position = data.getExtras().getInt(ITEM_POSITION);
            // update the model with the new item text at the edited position
            items.set(position, updatedItemText);
            // persist the change to model
            writeItems();
            // notify the user the operation completed ok
            Toast.makeText(this, "Item updated successfully.", Toast.LENGTH_SHORT).show();
        }
    }

    private File getDataFile() {
        return new File(getFilesDir(), "todo.txt");
    }

    private void readItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading file", e);
            items = new ArrayList<>();
        }
    }

    private void writeItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing file", e);
        }

    }


}
