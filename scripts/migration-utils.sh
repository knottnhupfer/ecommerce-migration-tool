#!/bin/bash

set -e

INFO=" [INFO]"

DIST_DIR_NAME="/dist_new"

TOOL_DIR="$( cd "$(dirname "$0")/.." ; pwd -P )"
DIST_DIR=${TOOL_DIR}${DIST_DIR_NAME}

CONFIG_FILE_DIR=${TOOL_DIR}/config/illuminazione
CONFIG_FILE=${CONFIG_FILE_DIR}/migration-config.yaml

# application variables
UTILS_JAR_NAME=ecommerce-utils-1.0.0-SNAPSHOT.jar
UTILS_TARGET_DIR=app/ecommerce-utils/target/
UTILS_JAR=${DIST_DIR}/${UTILS_JAR_NAME}

# disable it if not JAVA 9 or higher
JAVA_OPTIONS="--add-modules java.xml.bind"

function printInfo {
  echo ""
	echo "${INFO} $1"
}


# prepare application ============================
# -----------------------------------------------
if [ ! -f ${UTILS_JAR} ]; then
  echo ""
  echo "${INFO} Create directory ${DIST_DIR}"
  mkdir -p ${DIST_DIR}

  echo "${INFO} Setup application jar file ${UTILS_JAR}"
  cp ${UTILS_TARGET_DIR}/${UTILS_JAR_NAME} ${DIST_DIR}/
fi

echo ""
echo "${INFO} Using configuration file ${CONFIG_FILE}"
echo ""

# migrate categories to destination system
# migrate products to destination system
# migrate products images to destination system


case "$1" in
  create-led-brands)
    printInfo "Create brands for illuminazione-a-led brands ..."
    java ${JAVA_OPTIONS} -jar ${UTILS_JAR} --action=create-led-brands --config=${CONFIG_FILE}
    ;;
  generate-merged-product-ids)
    printInfo "Generate merged product ids mapping (both in source system) ..."
    java ${JAVA_OPTIONS} -jar ${UTILS_JAR} --action=products-mapping --config=${CONFIG_FILE}
    ;;
  migrate-products)
    printInfo "Migrate product ..."
    java ${JAVA_OPTIONS} -jar ${UTILS_JAR} --action=products-migrate --config=${CONFIG_FILE}
    ;;
  upload-images)
    printInfo "Upload images to destination system ..."
    java ${JAVA_OPTIONS} -jar ${UTILS_JAR} --action=products-image-migrate --config=${CONFIG_FILE}
    ;;
  migrate-product-tier-prices)
    printInfo "Migrate main product tier prices ..."
    java ${JAVA_OPTIONS} -jar ${UTILS_JAR} --action=products-tier-prices-migrate --config=${CONFIG_FILE}
    ;;
  *)
    echo ""
    echo " [WARN] No command specified!"
    echo ""
    echo " # ########################################################################"
    echo " # Prepare mapping files before products migration:"
    echo ""
    echo "   Usage: ${0}   generate-merged-product-ids | migrate-products | upload-images | create-led-brands | migrate-product-tier-prices"
    echo ""
    ;;
esac
