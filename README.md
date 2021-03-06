Java(Android) ArsenalPay API SDK
=========

[Arsenal Media LLC](http://www.arsenalmedia.ru/index.php/en)  
[ArsenalPay processing server](https://arsenalpay.ru)

Java(Android) ArsenalPay API SDK is software development kit for 
fast simple and seamless integration your Java(Android) application with processing server of ArsenalPay.

Functions of API
----

- Mobile commerce.


JDK Version Requirements
----

1.7

Dependencies
----

[Simple XML framework](http://simple.sourceforge.net)  
[Apache Commons Lang](http://commons.apache.org/proper/commons-lang/)  
[JUnit](http://junit.org/)  
[Mockito](http://mockito.org/)

Install
----

Clone repo and import module in your Gradle-based project to start using ArsenalPay-API.  
Or you can open terminal in sdk folder and enter: "gradle clean","gradle fatjar" for receive JAR with all dependencies.

Logs
----
To view the logs of SDK, edit filter for log message: "ArsenalPayAPI-SDK"

Merchant Credentials
----
First of all, if you wish to start using ArsenalPay-SDK for real, you should get real MerchantCredentials. For this, go to **[ArsenalPay support page](https://arsenalpay.ru/index.html)** and send your request.
After some time, you'll get e-mail message from our manager, which will help you.
If you want to test our API, you can use test MerchantCredentials.

Request params
----
1) **ID** - Merchant ID which can be obtain from the payment service provider ArsenalPay  
*Test ID = "9987"*  
2) **Secret** - Merchant Password which can be obtain from the payment service provider ArsenalPay  
*Test Secret = "1234567890"*  
3) **PayerID** - Phone number of the client (9\*\* \*\*\* \*\*\*)  
4) **RecipientID** - Account number of the seller  
5) **Amount** - 10-15000 RUR  
6) **Currency** - Code of currency (ISO 4217)  

Usage
----
**RequestPayment and CheckPaymentStatus methods. Example code (Java)**:

```java 

//Creating your own connection configuration
HttpUrlConfiguration configuration = new HttpUrlConfiguration();
        configuration.setConnectionTimeout(CONNECTION_TIMEOUT);
        configuration.setReadTimeout(READ_TIMEOUT);

        ApiCommandsFacade apiCommandsFacade = new ApiCommandsFacadeImpl(new HttpUrlConnectionImpl(configuration),
                new MerchantCredentials("9987", "1234567890")
        );
        //or (with default configuration)
        /*ApiCommandsFacade apiCommandsFacade = new ApiCommandsFacadeImpl(
            new MerchantCredentials("9987", "1234567890")*/
 
PaymentRequest paymentRequest = new PaymentRequest.MobileBuilder()
  .payerId(9140001111L)
  .recipientId(123456789L)
  .amount(125D)
  .currency("RUR")
  .build();
 
// Payment Response
PaymentResponse paymentResponse = apiCommandsFacade.requestPayment(paymentRequest)
 
// Payment Status Response
PaymentStatusResponse paymentStatusResponse = apiCommandsFacade.checkPaymentStatus(new PaymentStatusRequest(paymentResponse.getTransactionId())
);
```      

See more details in JavaDoc.

**Android example**
----
Android example is located in a folder exampleApp.  
It's a simple demonstration how this SDK can be used in Android application. 
