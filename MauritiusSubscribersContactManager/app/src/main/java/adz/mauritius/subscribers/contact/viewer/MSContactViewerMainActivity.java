package adz.mauritius.subscribers.contact.viewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Copyright 2016 Ubeidullah Jamal Ahmad
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * <p/>
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * <p/>
 * See the License for the specific language governing permissions and limitations under the License.
 * <p/>
 * <p/>
 * Created on 20/12/2015.
 */
public class MSContactViewerMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contacts_main);

        setSupportActionBar((Toolbar) findViewById(R.id.app_bar));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contacts_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_legend) {
            Intent intent = new Intent(this, LegendActivity.class);
            startActivity(intent);
            return true;
        }

        if (item.getItemId() == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (((TextView) this.findViewById(R.id.all_contacts_search_box)).getText().length() > 0) {
            ((TextView) this.findViewById(R.id.all_contacts_search_box)).setText("");
        } else {
            super.onBackPressed();
        }
    }
}
