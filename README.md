Java ArsenalPay API SDK
=========

<p><a href="http://www.arsenalmedia.ru/index.php/en">Arsenal Media LLC</a></p>
<p><a href="https://arsenalpay.ru">ArsenalPay processing server</a></p>


<p>Android(Java) ArsenalPay API SDK is software development kit for 
fast simple and seamless integration your (android)java application with processing server of ArsenalPay.</p>

Version
----

1.0

JDK version requirements
----

min 1.6

Source
----

<a href="https://arsenalpay.ru/site/integration">Official integration guide page</a>

Functions of API
----

- Mobile commerce.

<b>RequestPayment method. Example code for mobile charge:</b>

```java 

apiCommandsFacade = new ApiCommandsFacadeImpl(
                new MerchantCredentials("9987", "1234567890")
        );

PaymentRequest paymentRequest = new PaymentRequest.MobileBuilder()
        .payerId(9140001111L)
        .recipientId(123456789L)
        .amount(12.5D)
        .currency("RUR")
        .comment("Java-SDK-Test")
        .build();

PaymentResponse paymentResponse = new sendRequestPayment().execute(paymentRequest).get(30, TimeUnit.SECONDS);
                    transactionID = paymentResponse.getTransactionId();
```        

<p>See more details in JavaDoc.</p>

<b>CheckPaymentStatus method. Example code:</b>

```java  

    PaymentStatusResponse paymentStatusResponse = new checkStatusPayment().execute(new PaymentStatusRequest(transactionID)).get(30, TimeUnit.SECONDS);

```

<p>See more details in JavaDoc.</p>

<b>AsyncTask examples. Example code:</b>

```java

	private class sendRequestPayment extends AsyncTask<PaymentRequest, Void, PaymentResponse> {

        @Override
        protected PaymentResponse doInBackground(PaymentRequest... params) {
            PaymentResponse paymentResponse = null;
            try {
                paymentResponse = apiCommandsFacade.requestPayment(params[0]);
            } catch (ArsenalPayApiException e) {
                e.printStackTrace();
            }
            return paymentResponse;
        }
    }
	
	private class checkStatusPayment extends AsyncTask<PaymentStatusRequest, Void, PaymentStatusResponse> {

        @Override
        protected PaymentStatusResponse doInBackground(PaymentStatusRequest... params) {
            PaymentStatusResponse paymentStatusResponse = null;
            try {
                paymentStatusResponse = apiCommandsFacade.checkPaymentStatus(params[0]);
            } catch (ArsenalPayApiException e) {
                e.printStackTrace();
            }
            return paymentStatusResponse;
        }
    }
```

