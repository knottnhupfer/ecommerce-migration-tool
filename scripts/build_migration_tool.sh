#!/bin/bash

set -e

APP_DIR="app"
DIST_DIR="dist_new"

TOOL_DIR="$( cd "$(dirname "$0")/.." ; pwd -P )"

pushd ${TOOL_DIR}

pushd ${APP_DIR}
mvn clean package -DskipTests
popd

find ./${DIST_DIR}/ -name "*jar" -exec rm {} \;

find ./${APP_DIR}/ecommerce-utils/target -name "ecommerce-utils*.jar" -exec cp {} ${DIST_DIR} \;
find ./${APP_DIR}/ecommerce-migration-tool/target -name "ecommerce-migration-tool*.jar" -exec cp {} ${DIST_DIR} \;

popd

