#!/bin/bash

set -e

INFO=" [INFO]"

DIST_DIR_NAME="/dist_new"

TOOL_DIR="$( cd "$(dirname "$0")/.." ; pwd -P )"
DIST_DIR=${TOOL_DIR}${DIST_DIR_NAME}

CONFIG_FILE_DIR=${TOOL_DIR}/config/illuminazione
CONFIG_FILE_PATH=${CONFIG_FILE_DIR}/migration-config.yaml

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

#	mkdir -p ${CONFIG_DIR}
#	cp ${SRC_CONFIG}/migration-config.yaml ${CONFIG_DIR}
#  echo "${INFO} Successfully copied and setup application configuration ..."
#  echo ""
fi

echo ""
echo "${INFO} Using configuration file ${CONFIG_FILE_PATH}"
echo ""

# migrate categories to destination system
# migrate products to destination system
# migrate products images to destination system


case "$1" in
  generate-merged-product-ids)
    printInfo "Generate merged product ids mapping (both in source system) ..."
    java -jar ${UTILS_JAR} --action=products-mapping --config=${CONFIG_FILE_PATH}
    ;;
  migrate-products)
    printInfo "Migrate product ..."
    java -jar ${UTILS_JAR} --action=products-migrate --config=${CONFIG_FILE_PATH}
    ;;
  upload-images)
    printInfo "Upload images to destination system ..."
    java -jar ${UTILS_JAR} --action=products-image-migrate --config=${CONFIG_FILE_PATH}
    ;;
  *)
    echo ""
    echo " [WARN] No command specified!"
    echo ""
    echo " # ########################################################################"
    echo " # Prepare mapping files before products migration:"
    echo ""
    echo "   Usage: ${0}   generate-merged-product-ids | migrate-products | upload-images"
    echo ""
    ;;
esac