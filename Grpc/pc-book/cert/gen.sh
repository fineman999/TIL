rm *.pem
# 1. Generate CA's private key and self-signed certificate
openssl req -x509 -newkey rsa:4096 -days 365 -nodes -keyout ca-key.pem -out ca-cert.pem -subj "/C=KR/ST=SEUOL/L=MAPO/O=CHAN/OU=Study/CN=*.nanum.site/emailAddress=33cks1423@naver.com"

echo "CA's self-signed certificate"
openssl x509 -in ca-cert.pem -text -noout
# 2. Generate server's private key and certificate signing request (CSR)
openssl req -newkey rsa:4096 -nodes -keyout server-key.pem -out server-req.pem -subj "/C=KR/ST=SEUOL/L=MAPO/O=CHAN/OU=Study/CN=*.hello.site/emailAddress=33cks1423@naver.com"

# 3. Use CA's private key to sign server's CSR and generate server's certificate
openssl x509 -req -in server-req.pem -days 60 -CA ca-cert.pem -CAkey ca-key.pem -CAcreateserial -out server-cert.pem -extfile server-ext.cnf


echo "Server's signed certificate"
openssl x509 -in server-cert.pem -text -noout


# 4. verify server's certificate
#verify -CAfile ca-cert.pem server-cert.pem