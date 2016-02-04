package adz.mauritius.subscribers.contact.viewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
public class ContactNameListAdaptor extends BaseAdapter implements Filterable {

    private final Context mContext;
    private List<ContactItem> mContactItemsOriginal = new ArrayList<>();
    private List<ContactItem> mContactItemsFiltered = new ArrayList<>();
    private Filter mFilter = new ItemFilter();

    public ContactNameListAdaptor(Context context) {
        mContext = context;
    }

    public List<ContactItem> getContactItems() {
        return mContactItemsOriginal;
    }

    public void add(ContactItem contactItem) {
        mContactItemsOriginal.add(contactItem);
        mContactItemsFiltered.add(contactItem);
    }

    public void clear() {
        mContactItemsOriginal.clear();
        mContactItemsFiltered.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mContactItemsFiltered.size();
    }

    @Override
    public Object getItem(int position) {
        return mContactItemsFiltered.get(position);
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
            layout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.contact_list_item, null);
            viewHolder = new ViewHolder();

            viewHolder.contactNameView = (TextView) layout.findViewById(R.id.contact_list_item_name);
            viewHolder.operatorOneView = (ImageView) layout.findViewById(R.id.contact_list_item_operator_one);
            viewHolder.operatorTwoView = (ImageView) layout.findViewById(R.id.contact_list_item_operator_two);
            viewHolder.operatorThreeView = (ImageView) layout.findViewById(R.id.contact_list_item_operator_three);

            layout.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) layout.getTag();
        }

        final ContactItem contactItem = mContactItemsFiltered.get(position);

        viewHolder.contactNameView.setText(contactItem.getContactName());

        Utils.setOperatorViewImageResource(contactItem.getOperatorOne(), viewHolder.operatorOneView);
        Utils.setOperatorViewImageResource(contactItem.getOperatorTwo(), viewHolder.operatorTwoView);
        Utils.setOperatorViewImageResource(contactItem.getOperatorThree(), viewHolder.operatorThreeView);

        return layout;
    }


    @Override
    public Filter getFilter() {
        return mFilter;
    }

    static class ViewHolder {
        TextView contactNameView;
        ImageView operatorOneView;
        ImageView operatorTwoView;
        ImageView operatorThreeView;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final ArrayList<ContactItem> nlist = new ArrayList<>();

            for (ContactItem filterTarget : mContactItemsOriginal) {
                if (filterTarget.getContactName().toLowerCase().contains(filterString)) {
                    nlist.add(filterTarget);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mContactItemsFiltered.clear();
            mContactItemsFiltered = (ArrayList<ContactItem>) results.values;
            notifyDataSetChanged();
        }

    }

}
