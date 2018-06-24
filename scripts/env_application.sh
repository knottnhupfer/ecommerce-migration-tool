# sets environment for ecommerce migration tool

INFO=" [INFO]"

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