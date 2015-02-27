package com.example.arsenalpaytest;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.ConnectException;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.arsenalpay.api.exception.ArsenalPayApiException;
import ru.arsenalpay.api.facade.ApiCommandsFacade;
import ru.arsenalpay.api.facade.impl.ApiCommandsFacadeImpl;
import ru.arsenalpay.api.merchant.MerchantCredentials;
import ru.arsenalpay.api.request.PaymentRequest;
import ru.arsenalpay.api.request.PaymentStatusRequest;
import ru.arsenalpay.api.response.PaymentResponse;
import ru.arsenalpay.api.response.PaymentStatusResponse;

import static org.apache.commons.lang.StringUtils.isBlank;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public EditText payerID;
    public EditText recipientID;
    public EditText amount;
    public Button execute;
    public Button checkStatus;
    public TextView status;
    public CheckBox testParams;
    public boolean testMode;
    public ProgressBar progressBar;

    public ApiCommandsFacade testApiCommandsFacade;

    Long transactionID;

    SendRequestPayment sendRequestPayment;
    CheckStatusPayment checkStatusPayment;

    Logger log = Logger.getLogger(MainActivity.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        payerID = (EditText) findViewById(R.id.payerID);
        recipientID = (EditText) findViewById(R.id.recipientID);
        amount = (EditText) findViewById(R.id.amount);
        execute = (Button) findViewById(R.id.execute);
        checkStatus = (Button) findViewById(R.id.check_status);
        status = (TextView) findViewById(R.id.status);
        testParams = (CheckBox) findViewById(R.id.test_params);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        testMode = false;
        transactionID = null;

        execute.setOnClickListener(this);
        checkStatus.setOnClickListener(this);
        testParams.setOnCheckedChangeListener(this);
        checkStatus.setEnabled(false);

        testApiCommandsFacade = new ApiCommandsFacadeImpl(
                new MerchantCredentials("9987", "1234567890")
        );

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.execute: {

                PaymentRequest paymentRequest;

                progressBar.setVisibility(View.VISIBLE);
                execute.setEnabled(false);
                checkStatus.setEnabled(false);

                if (testMode) {
                    paymentRequest = new PaymentRequest.MobileBuilder()
                            .payerId(9140001111L)
                            .recipientId(147852369L)
                            .amount(10D)
                            .currency("RUR")
                            .comment("Java-SDK-Test")
                            .build();
                } else {
                    String payerID = this.payerID.getText().toString();
                    String recipientID = this.recipientID.getText().toString();
                    String amount = this.amount.getText().toString();


                    if (isBlank(payerID) || isBlank(recipientID) || isBlank(amount)) {
                        status.setText("Some params are empty");
                        execute.setEnabled(true);
                        if (transactionID != null) {
                            checkStatus.setEnabled(true);
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                        break;
                    }

                    paymentRequest = new PaymentRequest.MobileBuilder()
                            .payerId(Long.valueOf(payerID))
                            .recipientId(Long.valueOf(recipientID))
                            .amount(Double.valueOf(amount))
                            .currency("RUR")
                            .build();
                }

                sendRequestPayment = new SendRequestPayment();
                sendRequestPayment.execute(paymentRequest);
            }
            break;
            case R.id.check_status: {
                execute.setEnabled(false);
                checkStatus.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                if (transactionID != null) {
                    checkStatusPayment = new CheckStatusPayment();
                    checkStatusPayment.execute(new PaymentStatusRequest(transactionID));
                } else {
                    status.setText("Sorry, but payment was not produced.");
                    execute.setEnabled(true);
                    checkStatus.setEnabled(true);
                }
            }
            break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            testMode = true;
            payerID.setEnabled(false);
            recipientID.setEnabled(false);
            amount.setEnabled(false);
        }
        if (!isChecked) {
            testMode = false;
            payerID.setEnabled(true);
            recipientID.setEnabled(true);
            amount.setEnabled(true);
        }
    }

    private class SendRequestPayment extends AsyncTask<PaymentRequest, Void, PaymentResponse> {

        @Override
        protected PaymentResponse doInBackground(PaymentRequest... params) {
            PaymentResponse paymentResponse = null;
            try {
                paymentResponse = testApiCommandsFacade.requestPayment(params[0]);
            } catch (ArsenalPayApiException e) {
                log.log(Level.SEVERE, "ArsenalpayAPI-SDK", e);
            }
            return paymentResponse;
        }

        @Override
        protected void onPostExecute(PaymentResponse paymentResponse) {
            if (paymentResponse != null) {
                try {
                    transactionID = paymentResponse.getTransactionId();
                    status.setText(paymentResponse.getMessage());
                } catch (Exception e) {
                }
            } else {
                status.setText("Sorry, but payment was not produced. Check your internet connection and try again.");
            }
            execute.setEnabled(true);

            progressBar.setEnabled(true);
            progressBar.setVisibility(View.INVISIBLE);
            if (paymentResponse != null) {
                checkStatus.setEnabled(true);
            }
        }

    }

    private class CheckStatusPayment extends AsyncTask<PaymentStatusRequest, Void, PaymentStatusResponse> {

        @Override
        protected PaymentStatusResponse doInBackground(PaymentStatusRequest... params) {
            PaymentStatusResponse paymentStatusResponse = null;
            try {
                paymentStatusResponse = testApiCommandsFacade.checkPaymentStatus(params[0]);
            } catch (ArsenalPayApiException e) {
            }
            return paymentStatusResponse;
        }

        @Override
        protected void onPostExecute(PaymentStatusResponse paymentStatusResponse) {
            if (paymentStatusResponse != null) {
                try {
                    status.setText(paymentStatusResponse.getMessage().toString());
                } catch (Exception e) {
                }
            } else {
                status.setText("Sorry, but status check is failed. Check your internet connection and try again.");
            }
            execute.setEnabled(true);
            checkStatus.setEnabled(true);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
