FROM alpine:3.20 AS certs
RUN apk add --no-cache openssl
ARG SSL_CN=localhost
RUN mkdir -p /certs && \
    openssl req -x509 -nodes -days 3650 -newkey rsa:2048 \
    -keyout /certs/tls.key \
    -out /certs/tls.crt \
    -subj "/C=ES/ST=Spain/L=Madrid/O=Grupo2/OU=Dev/CN=${SSL_CN}" \
    -addext "subjectAltName=DNS:${SSL_CN},DNS:localhost,IP:127.0.0.1"

FROM nginx:1.27-alpine
RUN mkdir -p /etc/nginx/ssl
COPY default.conf /etc/nginx/conf.d/default.conf
COPY --from=certs /certs/tls.crt /etc/nginx/ssl/tls.crt
COPY --from=certs /certs/tls.key /etc/nginx/ssl/tls.key
EXPOSE 443
