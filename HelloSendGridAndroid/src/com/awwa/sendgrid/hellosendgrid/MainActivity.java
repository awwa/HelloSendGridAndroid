package com.awwa.sendgrid.hellosendgrid;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.widget.Toast;

import com.awwa.sendgrid.hellosendgrid.SendGridAsyncTask.FinishListener;

public class MainActivity extends PreferenceActivity implements OnPreferenceClickListener, FinishListener {

    private Preference prefSendSmtp;
    private Preference prefWebApi;
    private Preference prefPrintBounces;
    private Preference prefPrintUnsubscribes;
    private Preference prefAddUnsubscribes;
    private Preference prefDeleteUnsubscribes;
    private Preference prefPrintStatistics;
    private Preference prefPrintProfile;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        addPreferencesFromResource(R.xml.preference_main);
        
        prefSendSmtp = (Preference) getPreferenceScreen()
                .findPreference("pref_key_send_smtp");
        prefSendSmtp.setOnPreferenceClickListener(this);
        
        prefWebApi = (Preference) getPreferenceScreen()
                .findPreference("pref_key_web_api");
        prefWebApi.setOnPreferenceClickListener(this);

        prefPrintBounces = (Preference) getPreferenceScreen()
                .findPreference("pref_key_print_bounces");
        prefPrintBounces.setOnPreferenceClickListener(this);

        prefPrintUnsubscribes = (Preference) getPreferenceScreen()
                .findPreference("pref_key_print_unsubscribes");
        prefPrintUnsubscribes.setOnPreferenceClickListener(this);

        prefAddUnsubscribes = (Preference) getPreferenceScreen()
                .findPreference("pref_key_add_unsubscribes");
        prefAddUnsubscribes.setOnPreferenceClickListener(this);

        prefDeleteUnsubscribes = (Preference) getPreferenceScreen()
                .findPreference("pref_key_delete_unsubscribes");
        prefDeleteUnsubscribes.setOnPreferenceClickListener(this);

        prefPrintStatistics = (Preference) getPreferenceScreen()
                .findPreference("pref_key_print_statistics");
        prefPrintStatistics.setOnPreferenceClickListener(this);

        prefPrintProfile = (Preference) getPreferenceScreen()
                .findPreference("pref_key_print_profile");
        prefPrintProfile.setOnPreferenceClickListener(this);
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        
        SendGridAsyncTask task = new SendGridAsyncTask(this);
        CommandType type = CommandType.SendSMTP;

        // Send SMTP
        if ( preference == prefSendSmtp )
            type = CommandType.SendSMTP;
        
        // WebAPI
        if ( preference == prefWebApi )
            type = CommandType.WebAPI;

        // PrintBounces
        if ( preference == prefPrintBounces ) 
            type = CommandType.PrintBounces;

        // PrintUnsubscribes
        if ( preference == prefPrintUnsubscribes ) 
            type = CommandType.PrintUnsubscribes;

        // AddUnsubscribes
        if ( preference == prefAddUnsubscribes ) 
            type = CommandType.AddUnsubscribes;

        //DeleteUnsubscribes
        if ( preference == prefDeleteUnsubscribes ) 
            type = CommandType.DeleteUnsubscribes;

        // PrintStatistics
        if ( preference == prefPrintStatistics ) 
            type = CommandType.PrintStatistics;

        // PrintProfile
        if ( preference == prefPrintProfile ) 
            type = CommandType.PrintProfile;

        CommandType[] params = {type};
        task.execute( params );
        
        return false;
    }

    @Override
    public void OnFinish(String result) {
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
    }

}
