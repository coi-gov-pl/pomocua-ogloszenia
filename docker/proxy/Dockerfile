FROM nginx:1.21.6

COPY nginx.conf /etc/nginx/nginx.conf
ARG AUTH_HOSTNAME=local.pomagamukrainie.gov.pl

RUN cd /etc/nginx; openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout auth.key -out auth.crt -subj "/CN=${AUTH_HOSTNAME}/O=COI/C=PL";
RUN sed -i "s#server_name pomagamukrainie.gov.pl;#server_name $AUTH_HOSTNAME;#g" /etc/nginx/nginx.conf

ARG ALLOWED_X_FORWARDED_FOR=''
RUN sed -i "s#ALLOWED_X_FORWARDED_FOR#$ALLOWED_X_FORWARDED_FOR#g" /etc/nginx/nginx.conf
