FROM quay.io/keycloak/keycloak:15.0.2

ENV IAM_SSL_REQUIRED=''
ENV REINIT_KEYCLOAK=0
ENV EXPORT_KEYCLOAK=0

USER root

ADD import.json /tmp/
ADD entrypoint.sh /opt/jboss/tools/
ADD startup-scripts /opt/jboss/startup-scripts/
RUN cat /opt/jboss/tools/entrypoint.sh | tr -d '\r'  > /opt/jboss/tools/entrypoint2.sh; chmod +x /opt/jboss/tools/entrypoint2.sh; mv -f /opt/jboss/tools/entrypoint2.sh /opt/jboss/tools/entrypoint.sh

USER jboss

ENTRYPOINT /opt/jboss/tools/entrypoint.sh
