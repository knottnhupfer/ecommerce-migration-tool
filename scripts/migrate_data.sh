#!/bin/bash

set -e

TOOL_DIR="$( cd "$(dirname "$0")/.." ; pwd -P )"
source ${TOOL_DIR}/scripts/env_application.sh


# application variables
APP_JAR_NAME=ecommerce-migration-tool-1.0.0-SNAPSHOT.jar
APP_TARGET_DIR=app/ecommerce-migration-tool/target
APP_JAR=${DIST_DIR}/${APP_JAR_NAME}


# prepare application ============================
# ------------------------------------------------
if [ ! -f ${APP_JAR} ]; then

  echo ""
  echo "${ERROR} Unable to find jar ${APP_JAR}"
  exit 9
fi


# migrate categories =============================
# ------------------------------------------------

case "$1" in
  migrate-categories)
    printInfo "Merge categories ..."
		${RUN_JAVA} -jar ${APP_JAR} --merge-category --config=${CONFIG_FILE}
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
