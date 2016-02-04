package adz.mauritius.subscribers.contact.viewer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

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
 * Created on 1/1/2016.
 */
public class ContactPhoneNumberListAdaptor extends BaseAdapter {

    private ArrayList<String> mPhoneNumbers = new ArrayList<>();

    private Context mContext;

    public ContactPhoneNumberListAdaptor(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mPhoneNumbers.size();
    }

    @Override
    public Object getItem(int position) {
        return mPhoneNumbers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout layout = (RelativeLayout) convertView;

        ViewHolder viewHolder;
        if (layout == null) {
            layout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.contact_number_item, null);

            viewHolder = new ViewHolder();
            viewHolder.contactPhoneNumberView = (TextView) layout.findViewById(R.id.contact_details_phone_number);
            viewHolder.operatorView = (ImageView) layout.findViewById(R.id.contact_details_operator);
            viewHolder.sendSmsView = (ImageView) layout.findViewById(R.id.contact_details_sendsms);

            layout.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) layout.getTag();
        }

        final String phoneNumber = mPhoneNumbers.get(position);

        viewHolder.contactPhoneNumberView.setText(phoneNumber);

        Utils.setOperatorViewImageResource(Utils.getOperatorFromNumber(phoneNumber), viewHolder.operatorView);

        viewHolder.sendSmsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:" + phoneNumber));
                sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(sendIntent);
            }
        });

        return layout;
    }

//    private String formatMauritianNumber(String phoneNumber) {
//        phoneNumber = phoneNumber.replaceAll(" ", "").replaceAll("\\+", "").replaceAll("-","");
//        if (phoneNumber.length() == 8)
//        {
//            // mobile number
//            if (phoneNumber.startsWith("5"))
//            {
//                phoneNumber = 5 + phoneNumber.substring(1,4) + " " + phoneNumber.substring(4);
//            }
//            else
//            {
//                phoneNumber = phoneNumber.substring(0,1) + phoneNumber.substring(2,3) + " " + phoneNumber.substring(4,5) + phoneNumber.substring(6,7);
//            }
//        } else if (phoneNumber.length() == 7)
//        {
//            phoneNumber = phoneNumber.substring(0,2) + " " + phoneNumber.substring(3);
//        } else if (phoneNumber.startsWith("\\+?230"))
//        {
//            phoneNumber = phoneNumber.startsWith("+230")? phoneNumber.substring(4,6) + " " + phoneNumber.substring(7) : phoneNumber.substring(3,5) + " " + phoneNumber.substring(6);
//            phoneNumber = "+230" + phoneNumber;
//        }
//        return phoneNumber;
//    }

    public void setDataset(ArrayList<String> dataset) {
        this.mPhoneNumbers = dataset;
    }

    static class ViewHolder {
        TextView contactPhoneNumberView;
        ImageView operatorView;
        ImageView sendSmsView;
    }
}
