#!/bin/bash
# shellcheck disable=SC2002
TMP=$(cat build.gradle | grep -m2 -Po '(?<=(version)).*($)' | tail -n1);
#major=$(echo $TMP | cut -d. -f1);
#minor=$(echo $TMP | cut -d. -f2);
#patch=$(echo $TMP | cut -d. -f3);
#OLD=$major\.$minor\.$patch
#patch=$((patch+1));
#NEW=$major\.$minor\.$patch;
sed -i -- "s/$TMP/ '$1'/g" build.gradle;
echo $1;

# NEVER GONNA GIVE YOU AUP
