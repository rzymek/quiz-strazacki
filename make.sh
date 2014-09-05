#!/bin/bash
find . -name *.png | cut -d/ -f3- |sort| awk -f src/dir2json.awk > tests.json
awk '/^@/ { file=substr($0,2); while(getline < file) { print } ;next} {print}' src/template.html > index.html
rm tests.json
