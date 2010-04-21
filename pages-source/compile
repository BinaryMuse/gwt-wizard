#!/bin/bash

rm -Rf ./output
# The compile script requires that the GWT-Wizard
# JavaDoc be located in ../doc for JavaDoc inclusion
if [[ -d "../doc" ]] ; then
  # Copy the JavaDoc to the static
  # directory for static compilation
  rm -Rf ./static/javadoc
  cp -r ../doc ./static/javadoc
else
  echo " WARNING: The JavaDoc could not be found in ../doc"
  echo " WARNING: Compiling site without copying JavaDoc"
fi

# Run the nanoc compiler
nanoc compile
