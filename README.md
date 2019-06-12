# END POINT SMOKE TESTING TOOL - ESTT
This project contains the different independent projects. Purpose behind development of this tool to do smoke testing of REST APIs before releasing final version of your APIs. By using projects, you can make configuration files for you API end points and performs smoke testing manually or you can integrate with your DevOps CI/CD pipeline. This project is same like doing manual postman API call instead here you just have to maintain all end point configuration (WRITE ONCE AND TEST MANY TIME IN EACH ITERATION OF RELEASE). I will describe each projects and their usage. As well as I will also describe how you can setup or integrate with DevOps CI/CD pipeline. 

# Technical stack 
To develop all project for ESTT, I have used following technologies

1. Java 8
2. Spring Boot
3. Spring Core (for ESTT tool application)
4. Redis Cache

Here are different projects and their description. 

## 1. ESTT CORE (estt-core)
This project is utility project and contains the common functionality and utlity classes to support other projects like estt-config-creator-tool, endpoint-smoketest-tool and estt-dashboard. First you have to setup this project in your workspace before starting to build other projects.

### Setup steps

Before performing following command first install JDK 1.8 and maven in your system or VM. After installing maven, execute following command

```
project_directory> mvn clean install
```

This will generate jar file in local maven reposiotry. Other projects having dependency of this project which will be provided while building other projects.

## 2. ESTT Configuration Creator Tool (estt-config-creator-tool)
This project will be used to create configuration files like end points which you want to configure and error codes properties file. There are two different options to create configuration file using this tool. Like by providing manually interaction on command line or create dat file and program will read that file & generates EndPointConfig.json.

Before checking options to create configuration file, you have to setup this project in local environment. execute following command, which will generate executable jar file.

```
project_directory> mvn clean install
```

After successful execution of above command. You have to change directory to target directory or copy created executable jar at some other place.

Before starting with options. Would like to explain end point configuration and error codes property files.

#### 1. EndPointConfig.json 
This file contains all configuration of your end point to be tested. This is the detail same as you are providing in postman. You can pass "n" number of end point configuration. At the completion of program this tool will generate EndPointConfig.json file at provided location.

#### 2. ErrorCodes.properties
If your APIs returning some predefined error codes like some business exception code or database exception then you can define in these property file and and you want to mark your end point execution as failed if returns any one of them. Example, E0001 (System exception), E0002 (business Exception) etc.

### 1. CommandLine option - manual interaction.
Once executable file create under your <project_location>/target directory. Open command line, change directory to <project_location> and exceute following command

```
project_location> java -jar target\estt-config-creator-tool-0.0.1.jar <<location_where_you_want_store_generated_files>>
```

After execution of above command asked the following options:

```
Please provide end point (s) configuration details:

Enter End point configuration detail:
Please provide Sequence number in which sequence end point should be tested. (Example: 1, 2, 3, 4....):
1
```
Mandatory option.This option is to suggest endpoint-smoketest-tool that request this end point first. On press enter will ask other option:

```
Please provide Method Type (Example: GET, POST, PUT, DELETE only these fours are supported):
POST
```
Mandatory option.This option is for method type of your APIs like GET, POST, PUT, DELETE etc. Currently these are support. On enter ask other option.

```
Please provide end point URL:
http://localhost:8081/auth/request
```
Mandatory option.This option is for URL to be requested. On enter ask other option:

```
Please provide Request Headers, comma separated for multiple parameter(s) [OPTIONAL]:
Content-Type:application/json
```
OPTIONAL option. This is for request header to be passed while requesting API end point. On enter ask other option.

```
Please provide Request Parameter(s), comma separated for multiple parameter(s) [OPTIONAL]:
```
OPTIONAL option. THis is for query parameter if your API end point expecting. Then we can pass it. Even this option also replaces PATH variable if your API end point url is like, http://localhost:8080/users/{user_id}/detail. Then you have provide this detail in this option like user_id=123.

On enter ask for other option.

```
Please provide Request Body, if not giving next option please provide double enter:
{"data":"123"}

```
This option will be used if your API end point expecting some request body. Either in any format (JSON,XML) you just have to copy paste and double enter. On double enter it will ask for other option.

```
Please provide Expected Response Code [OPTIONAL]:
200
```
OPTIONAL option. If you want to match your REST API end point returns http response code same as configured. On enter ask for other option.

```
Please provide Expected Response to be match with returned response [OPTIONAL]:
```
OPTIONAL option. If you provide this option value and want to match REST API end point returns any response which has provided string. For example, If you enter success and your API endpoint response contains success in any case then you can decide your end point working fine. On enter it will ask following option.

```
WOULD YOU LIKE TO ADD MORE END POINT?(Y/N)
```

If you pass 'Y' then above options are coming again and ask for other end point configuration details. If you pass 'N' then it will ask to provide error codes if you want to provide as mentioned earlier in "ErrorCodes.properties" file section. On enter it will ask this option.

```
****************************
Please provide Error Codes, respected to your SYSTEM like Database error code, system error code etc. [OPTIONAL]:
****************************
```
Optional option. If you provide any value then it will create 'ErrorCodes.properties' file on same location where EndPointConfig.json will be created else it will not be created.


At the end of execution of program, creates following files:

##### EndPointConfig.json

```
[ {
  "sequence" : "1",
  "methodType" : "POST",
  "url" : "http://localhost:8081/auth/request",
  "requestHeaders" : "Content-Type:application/json",
  "requestBody" : "{\"data\":\"123\"}"
} ]
```

##### ErrorCodes.properties (If you have provided data in command line)

```
E0001, E00002, ST0101
```

### 2. CommandLine option - by provide datafile.

Instead of providing values manually you can provide following format file with datafile.txt naming convention and you will get EndPointConfig.json and ErrorCodes.properties files to be created at provided location. Execute command as following.

```
project_location> java -jar target\estt-config-creator-tool-0.0.1.jar <<location_of_datafile.txt>> <<location_where_you_want_store_generated_files>>
```

### datafile.txt (You must have to follow below format. As well as to enter more than configuration you have to provide enter between every configuration fo end point)

```
SQ| 1
M | POST
U | http://localhost:8081/auth/request
RH | Content-Type:application/json
RB | {"data": "1"}
ERC | 200
ER | successfully

SQ| 2
M | POST
U | http://localhost:8081/user/{userId}/detail
RH | Content-Type:application/json
RP | userId=123
ERC | 200

ENDOFREAD

ERRORCODES | SY001,SY002,EDAJ0001,EDAJ0002,EDAJ0003,EDAJ0004,ETAJ000
```

After execution of above command will generate ErrorCodeConfig.json and ErrorCode.properties like as below:

##### 1. EndPointConfig.json

```
[ {
  "sequence" : "1",
  "methodType" : "POST",
  "url" : "http://localhost:8081/auth/request",
  "requestHeaders" : "Content-Type:application/json",
  "requestBody" : "{\"data\": \"1\"}",
  "expectedResponseCode" : "200",
  "expectedResponse" : "successfully"
}, {
  "sequence" : "2",
  "methodType" : "POST",
  "url" : "http://localhost:8081/user/{userId}/detail",
  "requestHeaders" : "Content-Type:application/json",
  "requestParam" : "userId=123",
  "expectedResponseCode" : "200"
} ]
```

##### 2. ErrorCodes.properties

```
SY001,SY002,EDAJ0001,EDAJ0002,EDAJ0003,EDAJ0004,ETAJ000
```

## 3. Endpoint Smoke Test Tool (endpoint-smoketest-tool)

This project is the main component to request your REST Services end points. And process on APIs response and based on provided configuration marks as ERROR or SUCCESS to respective end point. On completion of project will create status_<<user_id>> file at provided location.

### Technical stack
1. Spring Core (To take advantages of spring framework)
2. Redis (OPTIONAL, if you want to see real time pie chart in estt dashboard then you must have to start redis service and program will store into cache)
3. Apache HTTP Client

### Setup and execution

First you have to setup and create executable jar file by executing below command:
```
project_directory> mvn clean install
```

This will generate executable jar file under <project_directory>\target. You can copy to other location in your VM or local machine

Before execututing following command, copy EndPointConfig.json and ErrorCodes.properties file at any location and provide the location with program.

```
project_directory> java -jar target\endpoint-smoketest-tool.jar <<location_of_Config_file>> <<user_id>>
```
These are mandatory arguments. First argument will be used to read EndPointConfig.json and ErrorCodes.properties file from provided path/location. Second argument will be used to create status_<user_id>.txt file and we can identify who has executed smoke testing. If same user executes program again then contents of status_<user_id>.txt replaced with new values always. At the end of execution of program 'status_<user_id>/txt' will be created at same location as we provided in first argument. 


##### NOTE: There is other OPTIONAL argument we can pass, If you want to see AWT chart/SWING real time pie chart then you can pass third argument as 'piechart'. But there is drawback like only we can see this chart in windows system .Do not prefer to use this option.

###### status_<user_id>.txt 

```
SUCCESS ->[http://localhost:8081/auth/request] executed successfully and matched with expected configuration.
ERROR ->[http://localhost:8081/user/{userId}/detail] has returned [500] which is different than expected response code: 200.
```

With this project we can also store the Error Count, Success Count and Status of program (like In progress, Finished) into redis cahce. These values will be used by ESTT Dashboard project. Which is showing real time pie chart with number of success and error counts. If you want to store details in redis cache then you have to provide configuration `application.properties` file before executing `mvn clean install` program.

###### application.properties

```
redis.host=localhost
redis.port=6379
redis.key.timeout=7
redis.key.timeUnit=DAYS
```
Third property value indicates the key:value will be exist into cache till 7 days on storage. You can reduce as per your requirement. In fourth property you can provide option like `MINUTES,HOURS, SECONDS, DAYS` to keep key:value into redis cache. If you don't provide time unit then it will consider by default DAYS. For example if you have provided redis.key.timeout=7 and  redis.key.timeUnit=HOURS then key will be stored in cache for 7 hours and automatically will be deleted by redis. if you don't provide `redis.key.timeUnit` value then program will store key:value for 7 days.

### CI/CD integeration
If your release using CI CD deployment as part of your project release. If you have pre prod environment and before release to production environment you want to test all end points then you can utlize this tool and integrate with your pipeline job. I have also provided batch scripts to call executable jar file (endpoint-smoketest-tool.jar). For this you have to perform following steps:

1. Copy Jar file in your CI CD server. (Make sure Java1.8 or above installed)
2. Copy Configuration files at some location (EndPointConfig.json and ErrorCodes.properties)
3. Copy "chkSmokeTestStatus.sh" from project directory (estt-test-tool\devops\) to CI/CD server
4. After all above steps, integrate one more stage and copy following command as per configuration of your server. Give executation access to shell script for your user
```
./chkSmokeTestStatus.sh <<location_of_executable_jar>> <<location_of_configuration_files>> <<user_id>>
```
1. First arg: Location of executable endpoint-smoketest-tool.jar
2. Second arg: Locatoon of configuration files (EndPointConfig.json and ErrorCodes.properties location)
3. Third arg: User Id to mention who has started smoke testing.

## 3. ESST Dashboard (estt-dashboard)

This is web project and shows the real time progress of running program of endpoint-smoketest-tool. If endpoint-smoketest-tool stores error & success counts and program progress into redis cache then web project will read those values and based on data render interactive pie chart. On completion of endpoint-smoketest-tool chart will be interactive and you can click on "ERROR" or "SUCCESS" and can able to see status detail of each end points.  

### Technical stack
1. Spring Boot
2. Redis Cache
3. Angular js
4. HTML & CSS

### Setup

1. Proivde the configuration of redis and status file location where you are expection file will be generated by endpoint-smoketest-tool into 'application.properties'

```
spring.mvc.view.prefix = /views/
spring.mvc.view.suffix = .html

redis.host=<<host>>
redis.port=<<port>>

status.file.location=<<expected_location_of_status_file>>

```

2. Execute following command, which generates the executable jar file.

```
project_directory> mvn clean install
```

Run executable jar which will start web application on port 8080.

```
project_directory> java -jar target\estt-dashboard-0.0.1-SNAPSHOT.jar
```

Once application started successfully, check in your browser by `http:\\localhost:8080`. Render the home page and asking for user id to check real time progress or completed process:

If redis service not started then web application will throw exception. So be sure that download redis if you it is not installed on your VM or local machine and start redis service. 


# I WILL PROVIDE MORE DETAIL ON DASHBOARD

 




