FROM quay.io/keycloak/keycloak:15.0.2

ENV IAM_SSL_REQUIRED=''
ENV REINIT_KEYCLOAK=0
ENV EXPORT_KEYCLOAK=0

USER root

ADD . /build

ADD import.json /tmp/
ADD entrypoint.sh /opt/jboss/tools/
ADD startup-scripts /opt/jboss/startup-scripts/
RUN cat /opt/jboss/tools/entrypoint.sh | tr -d '\r'  > /opt/jboss/tools/entrypoint2.sh; chmod +x /opt/jboss/tools/entrypoint2.sh; mv -f /opt/jboss/tools/entrypoint2.sh /opt/jboss/tools/entrypoint.sh
RUN if [ -d /build/modules ]; then cp -r /build/modules/* /opt/jboss/keycloak/modules/; cd /opt/jboss/keycloak/modules/pl/gov/coi/keycloak/keycloak-plugins/main/; sed -i "s#<resource-root path=\"coi-keycloak-plugins.jar\"/>#$(ls -1 *.jar | awk '{ print "<resource-root path=\"" $0 "\"/>" }'|tr -d '\n')#g" module.xml; fi
RUN if [ ! -d /build/modules ]; then sed -i '/^.*plugin.*$/d' /opt/jboss/startup-scripts/standalone-ha-startup-configuration.cli; fi

RUN if [ -d /build/deployments ]; then mkdir -p /opt/jboss/keycloak/standalone/deployments/; cp -r /build/deployments/* /opt/jboss/keycloak/standalone/deployments/; fi

USER jboss

ENTRYPOINT /opt/jboss/tools/entrypoint.sh
