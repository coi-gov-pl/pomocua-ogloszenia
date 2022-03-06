#!/bin/sh
if [ "$EXPORT_KEYCLOAK" == "1" ]; then
    echo '==========================================='
    echo ''
    echo ''
    echo 'EXPORT STARTED'
    echo ''
    echo ''
    echo '==========================================='
    timeout 120s /opt/jboss/tools/docker-entrypoint.sh -Dkeycloak.migration.action=export -Dkeycloak.migration.provider=singleFile -Dkeycloak.migration.file=/tmp/export.json -Dkeycloak.migration.userExportStrategy=REALM_FILE -Djboss.bind.address=0.0.0.0 -Dkeycloak.profile.feature.declarative_user_profile=enabled
    echo '==========================================='
    echo ''
    echo ''
    echo 'EXPORT FINISHED'
    echo ''
    echo ''
    echo '==========================================='
else
    if [ "$REINIT_KEYCLOAK" == "1" ]; then
        echo '==========================================='
        echo ''
        echo ''
        echo "WILL OVERRIDE CONFIG!!! Press Ctrl+C in 5s to stop ..."
        echo ''
        echo ''
        echo '==========================================='
        sleep 5
        sed "s#https://local.pomagamukrainie.gov.pl#$IAM_HOSTNAME#g" /tmp/import.json > /tmp/import_fixed.json
        sed -i "s#IAM_SSL_REQUIRED#$IAM_SSL_REQUIRED#g" /tmp/import_fixed.json
        /opt/jboss/tools/docker-entrypoint.sh -Dkeycloak.migration.action=import -Dkeycloak.migration.provider=singleFile -Dkeycloak.migration.file=/tmp/import_fixed.json -Dkeycloak.migration.strategy=OVERWRITE_EXISTING -Djboss.bind.address=0.0.0.0 -Dkeycloak.profile.feature.declarative_user_profile=enabled
    else
        /opt/jboss/tools/docker-entrypoint.sh -Djboss.bind.address=0.0.0.0 -Dkeycloak.profile.feature.declarative_user_profile=enabled $@
    fi
fi
