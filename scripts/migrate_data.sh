#!/bin/bash

set -e

INFO=" [INFO]"


APP_DIR=/home/david/workspace_alphaconcept/migration_tool
DIST_DIR=${APP_DIR}/dist_new

SRC_CONFIG=config/

# application variables
APP_JAR_NAME=ecommerce-migration-tool-1.0.0-SNAPSHOT.jar
APP_TARGET_DIR=app/ecommerce-migration-tool/target
APP_JAR=${DIST_DIR}/${APP_JAR_NAME}


TOOL_DIR="$( cd "$(dirname "$0")/.." ; pwd -P )"
CONFIG_FILE_DIR=${TOOL_DIR}/config/illuminazione
CONFIG_FILE_PATH=${CONFIG_FILE_DIR}/migration-config.yaml


function printInfo {
  echo ""
	echo "${INFO} $1"
}


# prepare application ============================
# -----------------------------------------------
CONFIG_DIR=${DIST_DIR}/configs
if [ ! -f ${APP_JAR} ]; then

  echo ""
  echo "${INFO} Create directory ${DIST_DIR}"
  mkdir -p ${DIST_DIR}

  echo "${INFO} Setup application jar file ${APP_JAR}"
  cp ${APP_TARGET_DIR}/${APP_JAR_NAME} ${DIST_DIR}/

	mkdir -p ${CONFIG_DIR}
	cp ${SRC_CONFIG}/migration-config.yaml ${CONFIG_DIR}
  echo "${INFO} Successfully copied and setup application configuration ..."
  echo ""
fi


# migrate products ==============================
# -----------------------------------------------



# migrate categories to destination system
# migrate products to destination system
# migrate products images to destination system


case "$1" in
  merge-categories)
    printInfo "Merge categories ..."
		java -jar ${APP_JAR} --merge-category --config=${CONFIG_FILE_PATH}
    ;;
  merge-products)
    printInfo "Merge products ..."
		;;
  merge-product-images)
    printInfo "Merge product images ..."
    ;;
  *)
    echo ""
    echo " [WARN] No command specified!"
    echo ""
    echo " # ########################################################################"
    echo " # Main command for migration:"
    echo ""
    echo "   Usage: ${0} merge-categories | merge-products | merge-product-images"
    echo ""
    ;;
esac
