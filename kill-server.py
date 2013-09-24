# FileName kill-server.py

import os
import sys
import re

def execCmd(cmd):
    r = os.popen(cmd)
    text = r.read()
    r.close()
    return text

output = execCmd('ps aux | grep java')
print(output)
pattern = re.compile(r'^\w+\s+(\d+)')

search = pattern.search(output)
for pid in search.groups():
    print('kill ' + pid)
    print(execCmd('kill ' + pid))
