package adz.mauritius.subscribers.contact.viewer;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

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
 * Created on 3/1/2016.
 */
public class AllContactsFragment extends Fragment {

    private ProgressDialogFragment mProgressDialogFragment = ProgressDialogFragment.newInstance();
    private ContactNameListAdaptor mContactNameListAdaptor;
    private Activity mContainer;
    private boolean executed;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mContainer = this.getActivity();
        if (!executed) mProgressDialogFragment.show(this.getFragmentManager(), "");
        return inflater.inflate(R.layout.all_contacts_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListView mContactsList = (ListView) mContainer.findViewById(R.id.all_contacts_list_view);

        if (mContactNameListAdaptor == null) {
            mContactNameListAdaptor = new ContactNameListAdaptor(mContainer.getApplicationContext());
            // Sets the adapter for the ListView
        }
        mContactsList.setAdapter(mContactNameListAdaptor);

        mContactsList.setEmptyView(mContainer.getLayoutInflater().inflate(R.layout.contact_list_item_no_contacts, null));
        ((ViewGroup) mContactsList.getParent()).addView(mContactsList.getEmptyView());

        mContactsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent nIntent = new Intent(mContainer.getApplicationContext(), ContactDetailsActivity.class);
                ((ContactItem) mContactNameListAdaptor.getItem(position)).packageIntent(nIntent);
                InputMethodManager imm = (InputMethodManager) mContainer.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                startActivity(nIntent);
            }
        });

        final EditText editText = (EditText) mContainer.findViewById(R.id.all_contacts_search_box);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mContactNameListAdaptor.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ImageView clearFilter = (ImageView) mContainer.findViewById(R.id.all_contacts_search_box_clear);
        clearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });

        if (mContactNameListAdaptor.isEmpty()) {
            AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();
            asyncTaskRunner.execute();
        } else {
            mProgressDialogFragment.dismiss();
        }

    }

    private void setOperatorIcons(ContactItem contactItem) {
        if (!contactItem.getPhoneNumbers().isEmpty()) {
            if (contactItem.getPhoneNumbers().size() > 3) {
                contactItem.setOperatorOne(ContactItem.Operator.ELLIPSIS);
            } else {
                if (contactItem.getPhoneNumbers().size() >= 1) {
                    contactItem.setOperatorOne(Utils.getOperatorFromNumber(contactItem.getPhoneNumbers().get(0)));
                    if (contactItem.getPhoneNumbers().size() >= 2) {
                        contactItem.setOperatorTwo(Utils.getOperatorFromNumber(contactItem.getPhoneNumbers().get(1)));
                        if (contactItem.getPhoneNumbers().size() == 3) {
                            contactItem.setOperatorThree(Utils.getOperatorFromNumber(contactItem.getPhoneNumbers().get(2)));
                        }
                    }
                }
            }
        } else {
            contactItem.setOperatorOne(ContactItem.Operator.DEFAULT);
            contactItem.setOperatorTwo(ContactItem.Operator.DEFAULT);
            contactItem.setOperatorThree(ContactItem.Operator.DEFAULT);
        }

    }

    public static class ProgressDialogFragment extends DialogFragment {

        public static ProgressDialogFragment newInstance() {
            return new ProgressDialogFragment();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading contacts...");
            dialog.setIndeterminate(true);
            return dialog;
        }
    }

    private class AsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... p) {

            String selection_phone_number_present = ContactsContract.Contacts.HAS_PHONE_NUMBER + " = '1'";

            ContentResolver contentResolver = mContainer.getContentResolver();
            Cursor cur = contentResolver.query(
                    ContactsContract.Contacts.CONTENT_URI,
                    null,
                    selection_phone_number_present,
                    null,
                    "upper(" + ContactsContract.Contacts.DISPLAY_NAME + ") ASC");


            if (cur.getCount() > 0) {
                ContactItem lastContact = new ContactItem("*!&@#^@*!&#^@*#&^@!#*&!@#^*!&#@^", "sdsdsd");
                while (cur.moveToNext()) {
                    String cid = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    ContactItem contactItem = new ContactItem(name,cid);
                    Cursor pCur = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{cid}, null);

                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        boolean foundMatch = false;
                        for (String phoneNumber : contactItem.getPhoneNumbers()) {
                            if (PhoneNumberUtils.compare(phoneNo, phoneNumber)) {
                                foundMatch = true;
                                break;
                            }
                        }
                        if (!foundMatch) contactItem.getPhoneNumbers().add(phoneNo);
                    }
                    if (!contactItem.getPhoneNumbers().isEmpty()) {
                        if (lastContact.getContactName().toLowerCase().equals(contactItem.getContactName().toLowerCase())) {
                            for (String phoneNumber : contactItem.getPhoneNumbers()) {
                                boolean foundMatch = false;
                                for (String lastContactPhoneNumber : lastContact.getPhoneNumbers()) {
                                    if (PhoneNumberUtils.compare(lastContactPhoneNumber, phoneNumber)) {
                                        foundMatch = true;
                                        break;
                                    }
                                }
                                if (!foundMatch) lastContact.getPhoneNumbers().add(phoneNumber);
                            }
                            setOperatorIcons(lastContact);
                        } else {
                            setOperatorIcons(contactItem);
                            mContactNameListAdaptor.add(contactItem);
                            lastContact = contactItem;
                        }
                    }
                    pCur.close();
                }

            }
            cur.close();

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mContactNameListAdaptor.clear();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            executed = true;
            mContactNameListAdaptor.notifyDataSetChanged();
            mProgressDialogFragment.dismiss();
        }
    }
}
