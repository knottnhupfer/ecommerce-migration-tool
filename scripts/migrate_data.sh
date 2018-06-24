#!/bin/bash

set -e

TOOL_DIR="$( cd "$(dirname "$0")/.." ; pwd -P )"
source ${TOOL_DIR}/scripts/env_application.sh

# application variables
APP_JAR_NAME=ecommerce-migration-tool-1.0.0-SNAPSHOT.jar
APP_TARGET_DIR=app/ecommerce-migration-tool/target
APP_JAR=${DIST_DIR}/${APP_JAR_NAME}


CONFIG_FILE_DIR=${TOOL_DIR}/config/illuminazione
CONFIG_FILE_PATH=${CONFIG_FILE_DIR}/migration-config.yaml


# prepare application ============================
# ------------------------------------------------
if [ ! -f ${APP_JAR} ]; then

  echo ""
  echo "${INFO} Create directory ${DIST_DIR}"
  mkdir -p ${DIST_DIR}

  echo "${INFO} Setup application jar file ${APP_JAR}"
  cp ${APP_TARGET_DIR}/${APP_JAR_NAME} ${DIST_DIR}/
fi


# migrate categories =============================
# ------------------------------------------------

case "$1" in
  migrate-categories)
    printInfo "Merge categories ..."
		${RUN_JAVA} -jar ${APP_JAR} --merge-category --config=${CONFIG_FILE_PATH}
    ;;
  *)
    echo ""
    echo " [WARN] No command specified!"
    echo ""
    echo " # ########################################################################"
    echo " # Main command for migration:"
    echo ""
    echo "   Usage: ${0} migrate-categories"
    echo ""
    ;;
esac
