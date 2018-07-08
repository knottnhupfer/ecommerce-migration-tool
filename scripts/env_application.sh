# sets environment for ecommerce migration tool

INFO=" [INFO]"
INFO=" [ERROR]"

function printInfo {
  echo ""
	echo "${INFO} $1"
}

# JAVA related variables
# disable it if not JAVA 9 or higher
JAVA_EXECUTABLE="java"
JAVA_OPTIONS="--add-modules java.xml.bind"
RUN_JAVA="${JAVA_EXECUTABLE} ${JAVA_OPTIONS}"

# Configuration related variables
DIST_DIR_NAME="/dist_new"
DIST_DIR=${TOOL_DIR}${DIST_DIR_NAME}

APP_DIR_NAME="/app"
SOURCE_PROJECT_DIR=${TOOL_DIR}${APP_DIR_NAME}

CONFIG_FILE_DIR=${TOOL_DIR}/config/illuminazione
CONFIG_FILE="file:${CONFIG_FILE_DIR}/migration-config.yaml"
CONFIG_FILE_PARAM="--migration.configuration=${CONFIG_FILE}"