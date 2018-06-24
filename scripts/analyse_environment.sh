#!/bin/bash

set -e

TOOL_DIR="$( cd "$(dirname "$0")/.." ; pwd -P )"

echo ""
echo "Tool directory: ${TOOL_DIR}"
echo ""

source ${TOOL_DIR}/scripts/env_application.sh

echo ""
echo "Run: ${RUN_JAVA}"
echo ""
${JAVA_EXECUTABLE} -version
echo ""

echo ""
echo "Working directory: ${DIST_DIR}"
echo ""