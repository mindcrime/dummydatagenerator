#!/bin/sh

echo "Dummy Data Generator"
loc_jars=$(echo ./lib/*.jar | sed 's/ /:/g')':'

# echo $loc_jars

java -cp $loc_jars datagen.DataGenMain $1 $2 $3 $4 $5 $6 $7
