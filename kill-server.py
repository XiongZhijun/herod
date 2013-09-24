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

for pid in re.findall(r'^\w+\s+(\d+)', output, re.M):
    print(pid)
    print(execCmd('kill ' + pid))
