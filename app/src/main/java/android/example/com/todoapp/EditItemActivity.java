package android.example.com.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import static android.example.com.todoapp.MainActivity.ITEM_POSITION;
import static android.example.com.todoapp.MainActivity.ITEM_TEXT;

public class EditItemActivity extends AppCompatActivity {
    // Declare variables
    EditText etItemText;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // Initialise variables
        etItemText = (EditText) findViewById(R.id.etEditItem);
        etItemText.setText(getIntent().getStringExtra(ITEM_TEXT));
        position = getIntent().getIntExtra(ITEM_POSITION, 0);
        getSupportActionBar().setTitle("Edit Item");
    }

    // handler for save button
    public void onSaveItem(View v) {
        Intent i = new Intent();
        Log.i("EditActivity", etItemText.getText().toString());
        i.putExtra(ITEM_TEXT, etItemText.getText().toString());
        i.putExtra(ITEM_POSITION, position);
        setResult(RESULT_OK, i);
        finish();
    }
}
