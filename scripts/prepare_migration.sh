#!/bin/bash

set -e

INFO=" [INFO]"


APP_DIR=/home/david/workspace_alphaconcept/migration_tool
DIST_DIR=${APP_DIR}/dist_new

SRC_CONFIG=config/

# application variables
UTILS_JAR_NAME=ecommerce-utils-1.0.0-SNAPSHOT.jar
UTILS_TARGET_DIR=app/ecommerce-utils/target/
UTILS_JAR=${DIST_DIR}/${UTILS_JAR_NAME}


function printInfo {
  echo ""
	echo "${INFO} $1"
}


# prepare application ============================
# -----------------------------------------------
CONFIG_DIR=${DIST_DIR}/configs
if [ ! -f ${UTILS_JAR} ]; then
  echo ""
  echo "${INFO} Create directory ${DIST_DIR}"
  mkdir -p ${DIST_DIR}

  echo "${INFO} Setup application jar file ${UTILS_JAR}"
  cp ${UTILS_TARGET_DIR}/${UTILS_JAR_NAME} ${DIST_DIR}/

	mkdir -p ${CONFIG_DIR}
	cp ${SRC_CONFIG}/migration-config.yaml ${CONFIG_DIR}
  echo "${INFO} Successfully copied and setup application configuration ..."
  echo ""
fi


# migrate categories to destination system
# migrate products to destination system
# migrate products images to destination system


case "$1" in
  generate-merged-product-ids)
    printInfo "Generate merged product ids mapping (both in source system) ..."
    java -jar ${UTILS_JAR} --action=products-mapping --config=/home/david/workspace_alphaconcept/migration_tool/config/migration-config.yaml
    ;;
  *)
    echo ""
    echo " [WARN] No command specified!"
    echo ""
    echo " # ########################################################################"
    echo " # Prepare mapping files before products migration:"
    echo ""
    echo "   Usage: ${0} generate-merged-product-ids"
    echo ""
    ;;
esac
