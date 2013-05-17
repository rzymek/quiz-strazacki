#!/bin/bash
set -e
cd `dirname $0`
mvn -f plugin/pom.xml install
mvn -f app/pom.xml clean war:exploded appengine:update $*  -Pprod
