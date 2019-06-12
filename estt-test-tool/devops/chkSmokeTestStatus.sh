#!/bin/bash

echo "Location of estt smoke test tool program location: $1"
echo "Location of coniguration files: $2"
echo "User Id: $3"

if [ -z "$1" ]
then
  echo "Please provide first estt smoke test tool program location."
  exit 1
fi

if [ -z "$2" ]
then
 echo "Please proivde second argument location of configuration files"	
 exit
fi

if [ -z "$3" ]
then
 echo "Please provide third argument User Id";
 exit 1
fi

esttProg=$1/endpoint-smoketest-tool.jar
statusFile=$2/status_$3.txt

if [ -f $esttProg ]; then
	java -jar $esttProg $2 $3

	if [ -f $statusFile ]; then
	 # grep command with -c will give the number of lines having "ERROR ->" word. if having more than 1 then exit with 1
	 command=`grep -c 'ERROR ->' $statusFile` 
	 if [ "$command" -ge 1 ]; then
	  echo "Found ERROR(S) smoke testing, please check $statusFile file for more detail."
	  exit 1
	 fi
	else 
	 echo "There is some issue while executing smoke test tool."
	 exit 1 
	fi
else
 echo "ESTT executable jar not found at location $1"
 exit 1
fi
exit 0
