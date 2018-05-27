#!/bin/bash

set -e

pushd app
mvn clean package -DskipTests
popd

find ./dist_new/ -name "*jar" -exec rm {} \;