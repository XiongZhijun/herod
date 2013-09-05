#!/bin/bash
base_dir=$ZRH_HEROD_HOME
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