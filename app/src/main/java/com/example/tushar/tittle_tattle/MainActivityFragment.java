package com.example.tushar.tittle_tattle;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.zip.Inflater;

import com.example.tushar.tittle_tattle.adapters.SpinnerAdapter1;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private EditText mPhoneNumber;
    private Button mSubmitButton;
    private Boolean mIsLoginRequest=false;
    private RestClient rst;
    private EditText ed;
    public MainActivityFragment() {
   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        RelativeLayout abc= (RelativeLayout)inflater.inflate(R.layout.fragment_main,null,false);
        Context mContext=getActivity().getApplicationContext();
        CountryMaster cm = CountryMaster.getInstance(getContext());
        ArrayList<Country> countries = cm.getCountries();
        String [] countriesArray= cm.getCountriesAsArray();
        String countryIsoCode = cm.getDefaultCountryIso();
        Country country = cm.getCountryByIso(countryIsoCode);
        ArrayList<ItemData> list=new ArrayList<>();
        for(int i=0;i<countries.size();i++){
            Country country11=countries.get(i);

            String isocode=country11.mCountryIso;
            int id=cm.getCountryFlagImageResource(country11.mCountryIso);
            list.add(new ItemData(isocode,id));

        }


        mPhoneNumber=(EditText)abc.findViewById(R.id.PhoneEditText);
        mSubmitButton=(Button)abc.findViewById(R.id.submit_button);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    genrateHttpLoginrequest();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View abc=inflater.inflate(R.layout.otp_input_dailog, null);
                //sendSms();
               AlertDialog al= new AlertDialog.Builder(getView().getContext())
                        .setView(abc)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                try {
                                    ed=(EditText)abc.findViewById(R.id.otp_enter_box);
                                    verifyOtpRequest();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                Toast.makeText(getView().getContext(), "Cancel =)",
                                        Toast.LENGTH_LONG).show();

                            }
                        })
                        .setTitle(R.string.otp)
                        .show();
            }
        });

       /* list.add(new ItemData("Khr",R.drawable.khr));
        list.add(new ItemData("Usd",R.drawable.usd));
        list.add(new ItemData("Jpy",R.drawable.jpy));
        list.add(new ItemData("Aud",R.drawable.aud));
       */
       Spinner sp=(Spinner)abc.findViewById(R.id.spinner_countries);
       SpinnerAdapter1 adapter=new SpinnerAdapter1(getActivity(),R.layout.spinner_layout,R.id.txt,list);
        sp.setAdapter(adapter);

        //Spinner spinner = (Spinner) abc.findViewById(R.id.spinner_countries);
        sp.setOnItemSelectedListener(this);
      //  ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.item_spinner_countries, countries);
        ///SpinnerAdapter1 adapter1 = new SpinnerAdapter1(getContext(),);
        //adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setAdapter(adapter1);

        TextView tvCountryPrefix = (TextView) abc.findViewById(R.id.tv_dialprefix);
        tvCountryPrefix.setText("+" + country.mDialPrefix);

        return abc;
    }

    private void verifyOtpRequest() throws Exception {
        String url="http://192.168.43.187:8080/login/verify";
       // Inflater a=new Inflater();
      //  View OtpView=a.inflate(R.layout.otp_input_dailog,)
        EditText enterOtpBox=(EditText)getActivity().findViewById(R.id.otp_enter_box);
        String OtpBoxText=ed.getText().toString();
       // String OtpBoxText="1234";
        //JSONObject obj = new JSONObject();
        //obj.put("mobileNo", "9889311967");
       /* ArrayList<NameValuePair> header=new ArrayList<NameValuePair>();
        ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        nameValuePair.add(new BasicNameValuePair("otp", enterOtp.toString()));
        ArrayList<NameValuePair> arrayList= new ArrayList<NameValuePair>();
       */
       // RestClient rst=new RestClient();
        rst.verifyOtpRequest(url,OtpBoxText);

    }

    private void genrateHttpLoginrequest() throws Exception {
        setIsLoginRequest(true);
        String url="http://192.168.43.187:8080/login";
        //JSONObject obj = new JSONObject();
        //obj.put("mobileNo", "9889311967");
        ArrayList<NameValuePair> header=new ArrayList<NameValuePair>();
        ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        nameValuePair.add(new BasicNameValuePair("mobileNo", "9889311987"));
        ArrayList<NameValuePair> arrayList= new ArrayList<NameValuePair>();
        rst=new RestClient();
        rst.ExecuteLoginRequst(url, header, nameValuePair);
    }

    private void sendSms() {
        String phoneNumber=mPhoneNumber.getText().toString();
        String OtpNumber="12345";
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, OtpNumber, null, null);
            Toast.makeText(getContext(), "SMS sent.", Toast.LENGTH_LONG).show();
        }

        catch (Exception e) {
            Toast.makeText(getContext(), "SMS failed, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

   /* @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.otp_input_dailog, null))
                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        Toast.makeText(getView().getContext(), "OKAY =)",
                                Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getView().getContext(), "CANCEL =)",
                                Toast.LENGTH_LONG).show();
                    }
                });
        return builder.create();
    }
*/
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        CountryMaster cm = CountryMaster.getInstance(getActivity());
        Country country = cm.getCountryByPosition(position);
        TextView tvCountryPrefix = (TextView) getActivity().findViewById(R.id.tv_dialprefix);
        tvCountryPrefix.setText("+" + country.mDialPrefix);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void setIsLoginRequest(boolean isLoginRequest) {
        this.mIsLoginRequest= isLoginRequest;
    }

    public Boolean getIsLoginRequest() {
        return mIsLoginRequest;
    }
}
