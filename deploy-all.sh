#!/bin/bash
PRG="$0"
while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

PRGDIR=`dirname "$PRG"`
base_dir=$PRGDIR
echo ${base_dir}

projects=(herod-common herod-communication-common herod-communication-server herod-event herod-order herod-order-web)
ci=herod-order-ci
for project in ${projects[*]}
do
    cd ${base_dir}/${project}
    ant publish-local
done

cd ${base_dir}/${ci}
ant deploy-all

cp -R ~/configs/** $base_dir/runtime/server/bear-appserver/