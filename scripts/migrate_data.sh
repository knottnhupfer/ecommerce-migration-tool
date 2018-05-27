#!/bin/bash

set -e

INFO=" [INFO]"


APP_DIR=/home/david/workspace_alphaconcept/migration_tool
DIST_DIR=${APP_DIR}/dist_new

SRC_CONFIG=config/

# application variables
#APP_JAR_NAME=ecommerce-utils-1.0.0-SNAPSHOT.jar
#APP_TARGET_DIR=app/ecommerce-utils/target
APP_JAR_NAME=ecommerce-migration-tool-1.0.0-SNAPSHOT.jar
APP_TARGET_DIR=app/ecommerce-migration-tool/target
APP_JAR=${DIST_DIR}/${APP_JAR_NAME}


function printInfo {
  echo ""
	echo "${INFO} $1"
}


# prepare application ============================
# -----------------------------------------------
CONFIG_DIR=${DIST_DIR}/configs
if [ ! -f ${APP_JAR} ]; then

  echo ""
  echo "${INFO} Cleanup/Remove directory ${DIST_DIR}"
  rm -rf ${DIST_DIR}
  mkdir -p ${DIST_DIR}

  echo "${INFO} Setup application jar file ${APP_JAR}"
  cp ${APP_TARGET_DIR}/${APP_JAR_NAME} ${DIST_DIR}/

	rm -rf ${CONFIG_DIR}
	mkdir -p ${CONFIG_DIR}
	cp ${SRC_CONFIG}/migration-config.yaml ${CONFIG_DIR}
  echo "${INFO} Successfully copied and setup application configuration ..."
  echo ""
fi



# migrate categories ============================
# -----------------------------------------------
# java ecommerce-utils-1.0.0-SNAPSHOT.jar --merge-products --config=<PATH-TO-CONFIGURATION>/migration-config.yaml


# migrate products ==============================
# -----------------------------------------------



# migrate categories to destination system
# migrate products to destination system
# migrate products images to destination system


case "$1" in
  merge-categories)
    printInfo "Merge categories ..."
		java -jar ${APP_JAR} --merge-category --config=/home/david/workspace_alphaconcept/migration_tool/config/migration-config.yaml
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
    echo "  Usage: ${0} merge-categories | merge-products | merge-product-images"
    echo ""
    ;;
esac
