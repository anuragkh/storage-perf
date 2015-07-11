#!/usr/bin/env bash

# resolve links - $0 may be a softlink
this="${BASH_SOURCE:-$0}"
common_bin="$(cd -P -- "$(dirname -- "$this")" && pwd -P)"
script="$(basename -- "$this")"
this="$common_bin/$script"

# convert relative path to absolute path
config_bin="`dirname "$this"`"
script="`basename "$this"`"
config_bin="`cd "$config_bin"; pwd`"
this="$config_bin/$script"

export STORAGE_PREFIX="`dirname "$this"`"/..
export STORAGE_HOME="${STORAGE_PREFIX}"
export STORAGE_CONF_DIR="${STORAGE_CONF_DIR:-"$STORAGE_HOME/conf"}"
