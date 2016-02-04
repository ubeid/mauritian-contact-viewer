package adz.mauritius.subscribers.contact.viewer;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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
 *
 * Created on 4/1/2016.
 */
public class ContactDetailsFragment extends Fragment {

    private TextView mContactName;
    private ContactPhoneNumberListAdaptor mContactPhoneNumberListAdaptor;
    private Activity mContainer;
    private ContactItem mContactItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    public void showContact(ContactItem mContactItem) {
        this.mContactItem = mContactItem;
        mContactName.setText(mContactItem.getContactName());
        mContactPhoneNumberListAdaptor.setDataset(mContactItem.getPhoneNumbers());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mContainer = this.getActivity();
        return inflater.inflate(R.layout.contact_details_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContactName = (TextView) mContainer.findViewById(R.id.contact_details_contact_name);
        ListView mPhoneNumberList = (ListView) mContainer.findViewById(R.id.contact_details_phone_number_list);
        mContactPhoneNumberListAdaptor = new ContactPhoneNumberListAdaptor(mContainer.getApplicationContext());
        showContact(ContactItem.readIntent(mContainer.getIntent()));

        mPhoneNumberList.setAdapter(mContactPhoneNumberListAdaptor);

        mPhoneNumberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String number = (String) mContactPhoneNumberListAdaptor.getItem(position);
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));

                startActivity(callIntent);
            }
        });

        mContainer.findViewById(R.id.contact_details_view_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI,mContactItem.getContactId());
                intent.setData(uri);
                mContainer.startActivity(intent);
            }
        });

    }
}
