package scip.app;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.AdapterView;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View.OnClickListener;

import java.util.List;

import scip.app.databasehelper.DatabaseHelper;

/*
    Display list view of couple id's from which the provider can select the couple_id they plan to counsel
 */

public class SessionSelectionActivity extends Activity {
    private ListView m_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_selection);
        m_listview = (ListView) findViewById(R.id.listView);

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        List<Long> couple_ids = db.getAllCoupleIDs();
        db.closeDB();

        String[] items = new String[] {"Item 1", "Item 2", "Item 3"};
        ArrayAdapter<Long> adapter =
                new ArrayAdapter<Long>(this, android.R.layout.simple_list_item_1, couple_ids);

        m_listview.setAdapter(adapter);

        m_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Long couple_id = (Long) parent.getItemAtPosition(position);
                Log.d("Couple id selected", String.valueOf(couple_id));

                Intent dashboard = new Intent(getApplicationContext(),DashboardActivity.class);
                dashboard.putExtra("couple_id", couple_id);

                startActivity(dashboard);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_couple_selection, menu);
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
