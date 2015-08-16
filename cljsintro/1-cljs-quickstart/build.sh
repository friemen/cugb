#!/bin/bash
# valid commands are `watch` and `build`

command="$1"

if [ -z "$command" ];
then
    command="build"
fi

rm -rf target
java -cp lib/cljs.jar:src:resources clojure.main -i build.clj -e "($command)"
