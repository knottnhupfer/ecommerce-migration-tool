#!/bin/bash

set -e

TOOL_DIR="$( cd "$(dirname "$0")/.." ; pwd -P )"
source ${TOOL_DIR}/scripts/env_application.sh

pushd ${TOOL_DIR}

pushd ${SOURCE_PROJECT_DIR}
mvn clean package -DskipTests
popd

find ./${DIST_DIR}/ -name "*jar" -exec rm {} \;

mkdir -p ${DIST_DIR}
find ./${SOURCE_PROJECT_DIR}/ecommerce-utils/target -name "ecommerce-utils*.jar" -exec cp {} ${DIST_DIR} \;
find ./${SOURCE_PROJECT_DIR}/ecommerce-migration-tool/target -name "ecommerce-migration-tool*.jar" -exec cp {} ${DIST_DIR} \;

popd