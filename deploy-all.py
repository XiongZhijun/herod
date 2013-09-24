# FileName : deploy-all.py
import sys
import os

current_dir = sys.path[0]
print(os.getcwd())
os.chdir(current_dir)
print(os.getcwd())

os.system('git pull')

projects = ('herod-common', 'herod-communication-common', 'herod-communication-server', 'herod-event', 'herod-order', 'herod-order-web', 'herod-mobile')
ci_project = 'herod-order-ci'
for project in projects:
    project_path = (current_dir + os.sep+ project)
    cmd = 'ant -buildfile ' + project_path + os.sep + 'build.xml publish-local'
    os.system(cmd)

ci_cmd = 'ant -buildfile ' + current_dir + os.sep +  ci_project + os.sep + 'build.xml deploy-all'
os.system(ci_cmd)

os.system('cp -R ~/configs/** ' + current_dir + '/runtime/server/bear-appserver/')
