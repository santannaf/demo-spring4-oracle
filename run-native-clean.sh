#!/usr/bin/env bash
set -euo pipefail

BIN="./build/native/nativeCompile/app"

#exec env -i \
#  PATH="$PATH" \
#  HOME="$HOME" \
#  LANG="pt_BR.UTF-8" \
#  LC_ALL="pt_BR.UTF-8" \
#  TERM="${TERM:-xterm-256color}" \
#  "$BIN"

exec env -i \
  LANG="pt_BR.UTF-8" \
  LC_ALL="pt_BR.UTF-8" \
  "$BIN"