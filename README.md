Java(Android) ArsenalPay API SDK
=========

[Arsenal Media LLC](http://www.arsenalmedia.ru/index.php/en)<br>
[ArsenalPay processing server](https://arsenalpay.ru)

Java(Android) ArsenalPay API SDK is software development kit for 
fast simple and seamless integration your Java(Android) application with processing server of ArsenalPay.

JDK Version Requirements
----

1.7

Dependencies
----

[Simple XML framework](http://simple.sourceforge.net)<br>
[Apache Commons Lang](http://commons.apache.org/proper/commons-lang/)<br>
[JUnit](http://junit.org/)<br>
[Mockito](http://mockito.org/)

Install
----

<p>Clone repo and import module in your Gradle-based project to start using ArsenalPay-API</p>

Logs
----
To view the logs of SDK, edit filter for log message: "ArsenalpayAPI-SDK"

Request params
----
First of all, if you wish to start using Arsenalpay-SDK for real, you should get real MerchantCredentials. For this, go to [Arsenalpay support page](https://arsenalpay.ru/support.html).

1) <strong>ID</strong> - ID продавца,получается у поставщика платежных услуг<br>
<i>Test ID = "9987"</i><br>
2) <strong>Secret</strong> - Секретный параметр, соответствующий ID, получается у поставщика платежных услуг<br>
<i>Test Secret = "1234567890"</i><br>
3) <strong>PayerID</strong> - Телефоный номер покупателя (формат: 9xx xxx xxxx), с которого будут снята указанная сумма<br>
4) <strong>RecipientID</strong> - Номер счета, на который будут переводиться денежные средства<br>
5) <strong>Amount</strong> - Переводимая сумма (до 15000 рублей)<br>
6) <strong>Currency</strong> - код валюты (ISO 4217)<br>

Functions of API
----

- Mobile commerce.

Usage
----
<b>RequestPayment and CheckPaymentStatus methods. Example code (Java):</b>

```java 

//Creating your own connection configuration
HttpUrlConfiguration configuration = new HttpUrlConfiguration();
        configuration.setConnectionTimeout(CONNECTION_TIMEOUT);
        configuration.setReadTimeout(READ_TIMEOUT);

        ApiCommandsFacade apiCommandsFacade = new ApiCommandsFacadeImpl(new HttpUrlConnectionImpl(configuration),
                new MerchantCredentials("9987", "1234567890")
        );
        //or
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
<p>See more details in JavaDoc.</p>

Android example
----
[Bitbucket](https://bitbucket.org/dejibqp/arsenalpaytest)
