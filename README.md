Java(Android) ArsenalPay API SDK
=========

[Arsenal Media LLC](http://www.arsenalmedia.ru/index.php/en)
[ArsenalPay processing server](https://arsenalpay.ru)


Java(Android) ArsenalPay API SDK is software development kit for 
fast simple and seamless integration your Java(Android) application with processing server of ArsenalPay.

Source
----

[Official integration guide page](https://arsenalpay.ru/site/integration)

JDK Version Requirements
----

1.7

Dependencies
----

[Simple XML framework](http://simple.sourceforge.net)
[Apache Commons Lang](http://commons.apache.org/proper/commons-lang/)
[JUnit](http://junit.org/)
[Mockito](http://mockito.org/)

Usage
----

<p>Clone repo and import module in your Gradle-based project to start using ArsenalPay-API :+1:</p>

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
.
.
.
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
```        

<p>See more details in JavaDoc.</p>

<b>CheckPaymentStatus method. Example code:</b>

```java  

    PaymentStatusResponse paymentStatusResponse = new checkStatusPayment().execute(new PaymentStatusRequest(transactionID)).get(30, TimeUnit.SECONDS);
.
.
.
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

<p>See more details in JavaDoc.</p>