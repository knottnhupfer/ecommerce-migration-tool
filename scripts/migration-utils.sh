#!/bin/bash

set -e

TOOL_DIR="$( cd "$(dirname "$0")/.." ; pwd -P )"
source ${TOOL_DIR}/scripts/env_application.sh


CONFIG_FILE_DIR=${TOOL_DIR}/config/illuminazione
CONFIG_FILE="file:${CONFIG_FILE_DIR}/migration-config.yaml"


# application variables
UTILS_JAR_NAME=ecommerce-utils-1.0.0-SNAPSHOT.jar
UTILS_TARGET_DIR=app/ecommerce-utils/target/
UTILS_JAR=${DIST_DIR}/${UTILS_JAR_NAME}


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

case "$1" in
  create-led-brands)
    printInfo "Create brands for illuminazione-a-led brands ..."
    ${RUN_JAVA} -jar ${UTILS_JAR} --action=create-led-brands --migration.configuration=${CONFIG_FILE}
    ;;
  generate-merged-product-ids)
    printInfo "Generate merged product ids mapping (both in source system) ..."
    ${RUN_JAVA} -jar ${UTILS_JAR} --action=products-mapping --migration.configuration=${CONFIG_FILE}
    ;;
  migrate-products)
    printInfo "Migrate products ..."
    ${RUN_JAVA} -jar ${UTILS_JAR} --action=products-migrate --migration.configuration=${CONFIG_FILE}
    ;;
  migrate-product-images)
    printInfo "Migrate product images ..."
    ${RUN_JAVA} -jar ${UTILS_JAR} --action=products-image-migrate --migration.configuration=${CONFIG_FILE}
    ;;
  migrate-product-tier-prices)
    printInfo "Migrate main product tier prices ..."
    ${RUN_JAVA} -jar ${UTILS_JAR} --action=products-tier-prices-migrate --migration.configuration=${CONFIG_FILE}
    ;;
  *)
    echo ""
    echo " [WARN] No command specified!"
    echo ""
    echo " # ########################################################################"
    echo " # Prepare mapping files before products migration:"
    echo ""
    echo "   Usage: ${0}   generate-merged-product-ids | migrate-products | migrate-product-images | create-led-brands | migrate-product-tier-prices"
    echo ""
    ;;
esac