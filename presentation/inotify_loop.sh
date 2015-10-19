#!/bin/sh

# On gentoo, to install inotifywait:
#   emerge -av sys-fs/inotify-tools

# First build everything
make clean all
# Automatically build on changes in source directory on Linux
while inotifywait `find . -iname '*.md' -o -iname '*.css' -o -iname '*.js'` ; do make all ; done
