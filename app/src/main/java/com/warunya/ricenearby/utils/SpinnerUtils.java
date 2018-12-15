package com.warunya.ricenearby.utils;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.warunya.ricenearby.R;


public class SpinnerUtils {

    public static void setSpinner(final Context context, Spinner spinner, int arrayList, final boolean hasHint) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
//                if (position == 0) {
                ((TextView) v.findViewById(android.R.id.text1)).setTextColor(
                        context.getResources().getColor((position == 0 && hasHint) ?
                                R.color.color_gray : R.color.color_gray_dark));
                ((TextView) v.findViewById(android.R.id.text1)).setTextSize(TypedValue.COMPLEX_UNIT_PT, 7);

//                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
//                    ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(0)); //"Hint to be displayed"
//                }
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount(); // [-1] you don't display last item. It is used as hint.
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        String[] spinnerList = context.getResources().getStringArray(arrayList);
        for (String item : spinnerList) {
            adapter.add(item);
        }
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

    }
}
