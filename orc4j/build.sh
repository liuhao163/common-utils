#!/bin/bash

mvn clean package

ret=$?
if [ $ret -ne 0 ]; then
  echo -n "===== maven package failed ${ret} ====="
  exit $ret
else
  echo -n "===== maven package  successfully! ====="
fi

# clean
rm -rf terget
rm -rf output

# gen
mkdir output

cp target/*.jar output
cp *.dylib output
cp *.so output
#cp *.dll output

echo -n "enter output run jar"
