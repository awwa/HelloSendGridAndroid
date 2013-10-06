package com.awwa.sendgrid.hellosendgrid;

import java.io.IOException;

import javax.mail.MessagingException;

import jp.co.flect.sendgrid.SendGridException;
import android.os.AsyncTask;

public class SendGridAsyncTask extends AsyncTask<CommandType, Void, String> {
    @SuppressWarnings("unused")
    private static final String TAG = SendGridAsyncTask.class.getSimpleName();
    private final SendGridAsyncTask self = this;
    
    private FinishListener mListener;
    
    public SendGridAsyncTask(FinishListener listener) {
        mListener = listener;
    }
    
    @Override
    protected String doInBackground(CommandType... params) {

        String result = "Fail";
        CommandType type = params[0];
        HelloSendGrid instance = new HelloSendGrid();
        try {
            switch (type)
            {
            case SendSMTP:
                result = instance.sendSMTP();
                break;
            case WebAPI:
                result = instance.sendWebAPI();
                break;
            case PrintBounces:
                result = instance.printBounces();
                break;
            case PrintUnsubscribes:
                result = instance.printUnsubscribes();
                break;
            case AddUnsubscribes:
                result = instance.addUnsubscribes();
                break;
            case DeleteUnsubscribes:
                result = instance.deleteUnsubscribes();
                break;
            case PrintStatistics:
                result = instance.printStatistics();
                break;
            case PrintProfile:
                result = instance.printProfile();
                break;
            }
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SendGridException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    
    @Override
    protected void onPostExecute(String result) {
        if (mListener != null) {
            mListener.OnFinish(result);
        }
    }
    
    public interface FinishListener {
        public void OnFinish(String result);
    }
}
