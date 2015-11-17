/*
 * Copyright 2015 www.vibin.it (vic.choy@gmail.com, victor.cui@vibin.it) 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.tushar.tittle_tattle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Spinner;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Locale;



public class CountryMaster {

	public static final String TAG = "CountryMaster";
	
	private static CountryMaster sInstance = null;
	private Context mContext = null;
	private String[] mCountryList;
	
	private ArrayList<Country> mCountries = new ArrayList<>();

	private CountryMaster(Context context) {
		mContext = context;
		Resources res = mContext.getResources();

		// builds country data from json
		InputStream is = res.openRawResource(R.raw.countries);
		Writer writer = new StringWriter();
		char[] buffer = new char[1024];
		try {
		    Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		    int n;
		    while ((n = reader.read(buffer)) != -1) {
		        writer.write(buffer, 0, n);
		    }
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		    try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String jsonString = writer.toString();
		JSONArray json = new JSONArray();
		try {
			json = new JSONArray(jsonString);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		mCountryList = new String[json.length()];
		for (int i = 0; i < json.length(); i++) {
			JSONObject node = json.optJSONObject(i);
			if (node != null) {
				Country country = new Country();
				country.mCountryIso = node.optString("iso");
				country.mDialPrefix = node.optString("tel");
				country.mCountryName = getCountryName(node.optString("iso"));
				mCountries.add(country);
				mCountryList[i] = country.mCountryIso;
			}
		}
	}

	public static CountryMaster getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new CountryMaster(context);
		}
		return sInstance;
	}

	/**
	 * Returns all {@link Country} objects as an ArrayList<Country>
	 *
	 * @return mCountries {@link CountryMaster#mCountries}
	 */
	public ArrayList<com.example.tushar.tittle_tattle.Country> getCountries() {
		return mCountries;
	}

	/**
	 * <b>If you are using a SpinnerAdapter!<b> You can feed this payload into {@link SpinnerAdapter} constructor.
	 *
	 * @return mCountryList {@link CountryMaster#mCountryList}
	 */
	public String[] getCountriesAsArray() {
		return mCountryList;
	}

	/**
	 * Again, helpful if you are using {@link Spinner#setSelection(int)}, to set the default country.
	 *
	 * @param position    Integer
	 * @return country	{@link Country}
	 */
	public com.example.tushar.tittle_tattle.Country getCountryByPosition(int position) {
		Country country = mCountries.get(position);
		//Log.d(TAG, country.mCountryIso);
		return country;
	}



	/**
	 * Returns a {@link Country} holder object by ISO code 2-char
	 *
	 * @param isoCode    String
	 * @return country {@link Country}
	 */
	public Country getCountryByIso(String isoCode) {
		Country country = null;
		int i = 0;
		int n = mCountries.size();
		while (i < n) {
			country = mCountries.get(i++);
			if (country.mCountryIso.equals(isoCode)) {
				break;
			}
		}
		return country;
	}



	/**
	 * Fetches the image resource id from drawables
	 *
	 * @param isoCode		String
	 * @return imageResId	Integer
	 */
	@SuppressLint("DefaultLocale")
	public int getCountryFlagImageResource(String isoCode) {
		String imageName = isoCode.trim().toLowerCase();
		int imageResId = mContext.getResources().getIdentifier("drawable/" + imageName, null, mContext.getPackageName());
		return imageResId;
	}

	/**
	 * Returns the device's locale iso code
	 *
	 * @return String
	 */
	public String getDefaultCountryIso() {
		Locale locale = Locale.getDefault();
		return locale.getCountry();
	}

	/**
	 * Returns the full name of the country
	 *
	 * @param isoCode	String
	 * @return 			String
	 */
	private String getCountryName(String isoCode) {
		Locale locale = new Locale(Locale.getDefault().getDisplayLanguage(), isoCode);
        return locale.getDisplayCountry().trim();
	}





}
