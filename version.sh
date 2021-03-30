#!/bin/bash
# shellcheck disable=SC2002
TMP=$(cat build.gradle | grep -m2 -Po '(?<=(version)).*($)' | tail -n1 | sed -r "s/(')//g");
major=$(echo $TMP | cut -d. -f1);
minor=$(echo $TMP | cut -d. -f2);
patch=$(echo $TMP | cut -d. -f3);
OLD=$major\.$minor\.$patch
patch=$((patch+1));
NEW=$major\.$minor\.$patch;
sed -i -- "s/$OLD/$NEW/g" build.gradle;
echo $NEW;

# NEVER GONNA GIVE YOU AUP
