package com.me.sunshine.custom;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.me.sunshine.R;
import com.me.sunshine.database.AppDatabase;
import com.me.sunshine.database.dao.CityDao;
import com.me.sunshine.database.entities.City;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

/**
 * Created by Esraa on 8/23/2017.
 */

public class CityDialogPreference extends DialogPreference {
    private Context mContext;
    private EditText etSearch;
    private ListView listView;
    private CityDao cityDao;
    private LinearLayout loadingView;
    private AVLoadingIndicatorView loadingIndicator;
    private OnCityChangeListener mCityChangeListener;

    public CityDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        setDialogLayoutResource(R.layout.city_dialog_preference);
        setDialogTitle("");
        setPositiveButtonText("");
        setNegativeButtonText("");
        setDefaultValue(mContext.getString(R.string.pref_loc_default));
    }

    public void setOnCityChangeListener(OnCityChangeListener cityChangeListener) {
        mCityChangeListener = cityChangeListener;
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        super.onSetInitialValue(restorePersistedValue, defaultValue);

        if (!restorePersistedValue) {
            // Set default value if no one exists
            persistString((String) defaultValue);
        }
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        etSearch = view.findViewById(R.id.et_search);
        Button btnSearch = view.findViewById(R.id.btn_search);
        listView = view.findViewById(R.id.search_list);
        loadingView = view.findViewById(R.id.loading_view);
        loadingIndicator = view.findViewById(R.id.loading_indicator);

        cityDao = AppDatabase.getInstance(mContext).cityDao();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoadingIndicator();
                new LoadSearchResultsTask().execute(etSearch.getText().toString());
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                City selectedCity = (City) listView.getItemAtPosition(position);
                persistString(selectedCity.getName());
                setSummary(selectedCity.getName());
                mCityChangeListener.onCitySelected(selectedCity);

                getDialog().dismiss();
            }
        });
    }

    private void showLoadingIndicator() {
        loadingView.setVisibility(View.VISIBLE);
        loadingIndicator.show();
    }

    private void hideLoadingIndicator() {
        loadingView.setVisibility(View.GONE);
        loadingIndicator.hide();
    }

    public interface OnCityChangeListener {
        void onCitySelected(City city);
    }

    private class LoadSearchResultsTask extends AsyncTask<String, Integer, List<City>> {
        @Override
        protected List<City> doInBackground(String... params) {
            return cityDao.findByCityName(params[0]);
        }

        @Override
        protected void onPostExecute(List<City> cities) {
            hideLoadingIndicator();
            listView.setAdapter(new ArrayAdapter<>(mContext,
                    android.R.layout.simple_list_item_1, cities.toArray()));
        }
    }
}
