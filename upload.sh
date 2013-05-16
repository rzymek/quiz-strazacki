#!/bin/bash
cd `dirname $0`
mvn -f app/pom.xml clean package war:war appengine:update $*  -Pprod
