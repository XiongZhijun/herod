# FileName start-server.py

import os
import sys

current_dir = sys.path[0]
appserver_dir = os.path.join(current_dir,'runtime/server/bear-appserver/bin')

os.chdir(appserver_dir)

os.system(os.path.join(appserver_dir,'startup.sh'))
