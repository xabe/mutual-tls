#Generate certificados auto selfsigned


```
machine=$(hostname)
env=dev
openssl req -utf8 -sha256 -newkey rsa:4096 -keyout $machine.key -nodes -out $machine.csr -subj "/O=Services/OU=API/OU=PKI/OU=$env/OU=$app/CN=$machine"
openssl req -new -x509  -days 3652 -key $machine.key -out selfsigned.crt
```

Export PKCS12 and Generate keystore

```
openssl pkcs12 -export -name $machine -in selfsigned.crt -inkey $machine.key -out $machine.p12

keytool -importkeystore -destkeystore $machine.jks -srckeystore $machine.p12 -srcstoretype pkcs12 -destalias $machine
```

Show certificado

```
keytool -v -list -keystore $machine.jks
```

#Generate client

```
openssl pkcs12 -export -in ../certs-server/selfsigned.crt -inkey ../certs-server/$machine.key -out client.p12 -name "Whatever"

keytool -genkey -dname "cn=CLIENT" -alias truststorekey -keyalg RSA -keystore ./client-truststore.jks -keypass whatever -storepass whatever
keytool -import -keystore ./client-truststore.jks -file selfsigned.crt -alias myca
```

Import dnie

```
openssl x509 -in ACRAIZ-SHA2.crt -inform DER -out ACRAIZ-SHA2-PEM.crt -outform PEM
keytool -import -keystore client-truststore.jks -file ACRAIZ-SHA2-PEM.crt
```

#Generate curl

```
openssl s_client -connect localhost:8443
generate file con content -----BEGIN CERTIFICATE----- and -----END CERTIFICATE-----


openssl pkcs12 -in client.p12 -out file.key.pem -nocerts -nodes
openssl pkcs12 -in client.p12 -out file.crt.pem -clcerts -nokeys

curl -E ./file.crt.pem --key ./file.key.pem https://localhost:8443/v1/status -k
curl --cacert ./ca.pem --cert ./file.crt.pem --key ./file.key.pem https://localhost:8443/v1/status -k
```