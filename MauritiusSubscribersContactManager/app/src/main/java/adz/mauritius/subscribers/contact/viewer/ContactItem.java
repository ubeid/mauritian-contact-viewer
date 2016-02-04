package adz.mauritius.subscribers.contact.viewer;

import android.content.Intent;

import java.util.ArrayList;
import java.util.Collections;

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
 * Created on 31/12/2015.
 */
public class ContactItem {

    public static String CONTACT_ID = "contact_id";
    public static String CONTACT_NAME = "contact_name";
    public static String CONTACT_PHONE_NUMBERS = "contact_phone_numbers";

    private String mContactName;
    private String mContactId;
    private Operator mOperatorOne = Operator.DEFAULT;
    private Operator mOperatorTwo = Operator.DEFAULT;
    private Operator mOperatorThree = Operator.DEFAULT;
    private ArrayList<String> phoneNumbers = new ArrayList<>();

    public ContactItem(String contactName, String contactId) {
        this.mContactName = contactName;
        this.mContactId = contactId;
    }

    public static ContactItem readIntent(Intent intent) {
        ContactItem contactItem = new ContactItem(intent.getStringExtra(CONTACT_NAME), intent.getStringExtra(CONTACT_ID));
        ArrayList<String> phoneNumbers = new ArrayList<>();
        Collections.addAll(phoneNumbers, intent.getStringArrayExtra(CONTACT_PHONE_NUMBERS));
        contactItem.setPhoneNumbers(phoneNumbers);
        return contactItem;
    }

    public String getContactName() {
        return mContactName;
    }

    public void setContactName(String mContactName) {
        this.mContactName = mContactName;
    }

    public Operator getOperatorOne() {
        return mOperatorOne;
    }

    public void setOperatorOne(Operator mOperatorOne) {
        this.mOperatorOne = mOperatorOne;
    }

    public Operator getOperatorTwo() {
        return mOperatorTwo;
    }

    public void setOperatorTwo(Operator mOperatorTwo) {
        this.mOperatorTwo = mOperatorTwo;
    }

    public Operator getOperatorThree() {
        return mOperatorThree;
    }

    public void setOperatorThree(Operator mOperatorThree) {
        this.mOperatorThree = mOperatorThree;
    }

    public ArrayList<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(ArrayList<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    @Override
    public String toString() {
        return "ContactItem{" +
                "mContactName='" + mContactName + '\'' +
                ", mContactId='" + mContactId + '\'' +
                ", mOperatorOne=" + mOperatorOne +
                ", mOperatorTwo=" + mOperatorTwo +
                ", mOperatorThree=" + mOperatorThree +
                ", phoneNumbers=" + phoneNumbers +
                '}';
    }

    public void packageIntent(Intent intent) {
        intent.putExtra(CONTACT_NAME, mContactName);
        intent.putExtra(CONTACT_PHONE_NUMBERS, phoneNumbers.toArray(new String[phoneNumbers.size()]));
        intent.putExtra(CONTACT_ID, mContactId);
    }

    public String getContactId() {
        return this.mContactId;
    }

    public void setContactId(String contactId) {
        this.mContactId = contactId;
    }


    public enum Operator {
        DEFAULT, ORANGE, MTML, EMTEL, ELLIPSIS, MT, UNRECOGNISED_MAURITIUS, OTHER
    }

}
