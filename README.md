## Iridis Color Server

This project is a jsonprc service to expose the Iridis project colorlib to external sources.

## Install
use mvn to install, since the server creates a wallet file which is also used by the tests, skip tests when you bulid it for the first time.
`mvn -Dmaven.test.failure.ignore=true` should do the trick

## Settings
By default a file properties file will be created in the same folder when run for the first time (cclibsrv.properties)
current default settings are:
walletName = cclibcolor.wallet
network = testnst

if working with mainnet make sure to change the network setting.

## API Reference
Currently exposes three endpoints : 

    boolean isColor(String txHash, int outindex);
    double getColorValue(String txHash, int outindex);
    String getColorValueEx(String colordef,String txHash, int outindex);

Where the getColorValueEx returns a serialized JSON that defines the color and if the output is spent or not.