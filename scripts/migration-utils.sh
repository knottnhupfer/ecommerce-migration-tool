#!/bin/bash

set -e

TOOL_DIR="$( cd "$(dirname "$0")/.." ; pwd -P )"
source ${TOOL_DIR}/scripts/env_application.sh


# application variables
UTILS_JAR_NAME=ecommerce-utils-1.0.0-SNAPSHOT.jar
UTILS_TARGET_DIR=app/ecommerce-utils/target/
UTILS_JAR=${DIST_DIR}/${UTILS_JAR_NAME}


# prepare application ============================
# ------------------------------------------------
if [ ! -f ${UTILS_JAR} ]; then

  echo ""
  echo "${ERROR} Unable to find jar ${UTILS_JAR}"
  exit 9
fi


echo ""
echo "${INFO} Using configuration file ${CONFIG_FILE}"
echo ""

case "$1" in
  create-led-brands)
    printInfo "Create brands for illuminazione-a-led brands ..."
    ${RUN_JAVA} -jar ${UTILS_JAR} --action=create-led-brands ${CONFIG_FILE_PARAM}
    ;;
  generate-merged-product-ids)
    printInfo "Generate merged product ids mapping (both in source system) ..."
    ${RUN_JAVA} -jar ${UTILS_JAR} --action=products-mapping ${CONFIG_FILE_PARAM}
    ;;
  migrate-products)
    printInfo "Migrate products ..."
    ${RUN_JAVA} -jar ${UTILS_JAR} --action=products-migrate ${CONFIG_FILE_PARAM}
    ;;
  migrate-product-images)
    printInfo "Migrate product images ..."
    ${RUN_JAVA} -jar ${UTILS_JAR} --action=products-image-migrate ${CONFIG_FILE_PARAM}
    ;;
  migrate-product-tier-prices)
    printInfo "Migrate main product tier prices ..."
    ${RUN_JAVA} -jar ${UTILS_JAR} --action=products-tier-prices-migrate ${CONFIG_FILE_PARAM}
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