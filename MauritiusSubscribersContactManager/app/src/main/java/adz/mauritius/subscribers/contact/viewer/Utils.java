package adz.mauritius.subscribers.contact.viewer;

import android.widget.ImageView;

import java.util.HashMap;
import java.util.regex.Pattern;

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
 * Created on 1/1/2016.
 */
public class Utils {

    private static HashMap<ContactItem.Operator, String> operatorRegexMap = null;
    //private static String TAG = "Utils";

    public static ContactItem.Operator getOperatorFromNumber(String phoneNumber) {
        if (operatorRegexMap == null) initialiseOperatorRegexMap();

        String phoneNumberCorrected = phoneNumber.replaceAll(" ", "").replaceAll("-", "").replaceAll("\\+", "");

        for (ContactItem.Operator op : operatorRegexMap.keySet()) {
            //Log.i(TAG, "getOperatorFromNumber check regex match for: " + op + ", for number: " + phoneNumber);
            if (checkForMatch(phoneNumberCorrected, operatorRegexMap.get(op))) return op;
        }

        return checkForMatch(phoneNumberCorrected, "(230)?([0-9]{7}|5[0-9]{7})") ? ContactItem.Operator.UNRECOGNISED_MAURITIUS : ContactItem.Operator.OTHER;
    }

    private static boolean checkForMatch(String phoneNumber, String regex) {
        return Pattern.compile(regex).matcher(phoneNumber).matches();
    }

    private static void initialiseOperatorRegexMap() {
        // REMOVED ALL hyphens AND spaces
        //ORANGE, MTML, EMTEL, MT
        operatorRegexMap = new HashMap<>();

        operatorRegexMap.put(ContactItem.Operator.MT, "(230)?(((2|4|6)[0-9]{6})|(5471|814|831|832)[0-9]{4})");
        String suffix = "(230)?5"; // all mobile numbers starts with and optional 230 followed by 5.
        operatorRegexMap.put(ContactItem.Operator.ORANGE, suffix + "((25|82|90|91|92|94)|7[056789])[0-9]{5}|87[5-8][0-9]{4}");
        operatorRegexMap.put(ContactItem.Operator.EMTEL, suffix + "(42[12389]|47[2-9]|49[0-9]|85[016]|(7[1234]|93|97|98)[0-9])[0-9]{4}");
        operatorRegexMap.put(ContactItem.Operator.MTML, suffix + "((29|86|95|96)[0-9]|871)[0-9]{4}");

    }

    public static void setOperatorViewImageResource(ContactItem.Operator operator, ImageView operatorView) {
        switch (operator) {
            case ORANGE:
                operatorView.setImageResource(R.drawable.contact_item_orange);
                break;
            case EMTEL:
                operatorView.setImageResource(R.drawable.contact_item_emtel);
                break;
            case MTML:
                operatorView.setImageResource(R.drawable.contact_item_mtml);
                break;
            case ELLIPSIS:
                operatorView.setImageResource(R.drawable.contact_item_ellipsis);
                break;
            case MT:
                operatorView.setImageResource(R.drawable.contact_item_mt);
                break;
            case UNRECOGNISED_MAURITIUS:
                operatorView.setImageResource(R.drawable.contact_item_unrecognised);
                break;
            case OTHER:
                operatorView.setImageResource(R.drawable.contact_item_other);
                break;
            default:
                operatorView.setImageResource(R.drawable.contact_item_empty);
        }
    }

}
