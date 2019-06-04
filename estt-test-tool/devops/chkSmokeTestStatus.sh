#!/bin/bash

statusFilePath=${STATUS_FILE_PATH}

if [ -f $statusFilePath/status.txt ]; then
 # grep command with -c will give the number of lines having "ERROR ->" word. if having more than 1 then exit with 1
 command=`grep -c 'ERROR ->' $statusFilePath/status.txt` 
 if [ "$command" -ge 1 ]; then
  echo "Found ERROR(S) smoke testing, please check $statusFilePath/status.txt file for more detail."
  exit 1
 fi
else 
 echo "There is some issue while executing smoke test tool during creation of status.txt. please check tool logs..!!"
 exit 1 
fi
exit 0
