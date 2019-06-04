## esst-config-creator

Provides the facility and flexibility to create configuration files (**EndPointConfig.json** and **ErrorCodes.properties**) for End Point Smoke Test Tool.
If any New Comer or any deveoloper unware about configuration file creation, then they can able to use this tool create configuration file using command line options.

## Development specification
[DevSpec](https://confluence.rakuten-it.com/confluence/display/TPHV/Developer+Specification%3A+Standalone+application+to+create+ESTT+configuration+files)

## Architecture or Flow overview
[Architecture or Flow overview](https://confluence.rakuten-it.com/confluence/display/TPHV/Developer+Specification%3A+Standalone+application+to+create+ESTT+configuration+files#DeveloperSpecification:StandaloneapplicationtocreateESTTconfigurationfiles-ArchitectureOverview)

## Run estt config creator tool
User can able to create configuration file following steps:

1. Clone repository, [GIT repository](https://saurabh.yagnik@git.rakuten-it.com/scm/trv/estt-config-creator.git)
2. run maven command, this will create runnable jar file under target directory
    ```
    mvn clean install
    ```
3. By following scenarios user can able to create configuration files.

    **Scenario 1:** In this scenario user must have to provide two argument, first **<<path_to_store_created_config_files>>** and second **<<path_of_datafile.txt>>**

    ```
    java -jar target/estt-config-creator-tool.jar <<path_to_store_created_config_files>> <<path_of_datafile.txt>>
    ```

    Example:
    ```
    java -jar target/estt-config-creator-tool.jar /usr/local/configs
    ```

    Example of datafile.txt: (User must have to create data file with following pattern and with **datafile.txt** name)<br/>
    
    **In project structure, attached datafile.txt for reference under datafile directory**
    ```
    M | POST
    U | http://dev-atrapi-search301z.dev.jp.local/search/v2/ota/OTAHotelAvailRQ.do
    RH | Host:api-search.travel.dev.jp.local,Content-Type:application/xml
    RP | apiClientId=rakutentravel_ota_test
    RB | <?xml version="1.0" encoding="utf-8" ?><OTA_HotelSearchRQ xmlns="http://www.opentravel.org/OTA/2003/05" EchoToken="ABC123" TimeStamp="2014-08-20T19:00:00" PrimaryLangID="ja_JP" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.opentravel.org/OTA/2003/05 ../Schemas/OTA_HotelAvailRQ.xsd" Version="1"><Criteria><Criterion><Position Latitude="35.6895" Longitude="139.6917" /><Radius Distance="100" UnitOfMeasureCode="2" /><StayDateRange Start="yyyy-MM-dd" End="yyyy-MM-dd"/><RoomStayCandidates><RoomStayCandidate Quantity="2"><GuestCounts><GuestCount Age="20" Count="2"/><GuestCount Age="10" Count="2"/><GuestCount Age="8" Count="2"/><GuestCount Age="2" Count="4"/></GuestCounts></RoomStayCandidate></RoomStayCandidates><TPA_Extensions><InfantOption RequirementType="1" Count="1"/><InfantOption RequirementType="2" Count="1"/><InfantOption RequirementType="3" Count="1"/> </TPA_Extensions></Criterion></Criteria></OTA_HotelSearchRQ>

    M | POST
    U | http://dev-atrapi-search301z.dev.jp.local/search/v2/ota/OTAHotelAvailRQ.do
    RH | Host:api-search.travel.dev.jp.local,Content-Type:application/xml
    RP | apiClientId=rakutentravel_ota_test
    RB | <?xml version="1.0" encoding="utf-8" ?><OTA_HotelSearchRQ xmlns="http://www.opentravel.org/OTA/2003/05" EchoToken="ABC123" TimeStamp="2014-08-20T19:00:00" PrimaryLangID="ja_JP" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.opentravel.org/OTA/2003/05 ../Schemas/OTA_HotelAvailRQ.xsd" Version="1"><Criteria>	<Criterion>		<Position Latitude="0" Longitude="0" />		<Radius Distance="100" UnitOfMeasureCode="2" />		<StayDateRange Start="yyyy-MM-dd" End="yyyy-MM-dd"/>		<RoomStayCandidates>			<RoomStayCandidate Quantity="2">				<GuestCounts>					<GuestCount Age="20" Count="2"/>					<GuestCount Age="10" Count="2"/>					<GuestCount Age="8" Count="2"/>					<GuestCount Age="2" Count="4"/>				</GuestCounts>			</RoomStayCandidate>		</RoomStayCandidates>		<TPA_Extensions>			<InfantOption RequirementType="1" Count="1"/>			<InfantOption RequirementType="2" Count="1"/>			<InfantOption RequirementType="3" Count="1"/>	 </TPA_Extensions></Criterion></Criteria></OTA_HotelSearchRQ>

    ERRORCODES | SY001,SY002,EDAJ0001,EDAJ0002,EDAJ0003,EDAJ0004,ETAJ000

    WHERE*
        - M     : Method Type
        - U     : URL
        - RH    : Request Header
        - RB    : Request Parameter
        - RB    : Request Body
    ```

    **Scenario 2:** In this scenario user can provide one argument **<<path_to_store_created_config_files>>** and tool will ask you to enter configuration by command line options.

    ```
    java -jar target/estt-config-creator-tool.jar <<path_to_store_created_config_files>>
    ```

    Example:
    ```
    java -jar target/estt-config-creator-tool.jar /usr/local/configs
    ```

5. After successful execution of jar file, user can able to see EndPointConfig.json and ErrorCodes.properties files under the provided directory
6. Now user can copy these two files, under the respective API module to test End Point Smoke Test Tool.

## Limitation
MacOS can not allow copy paste or write more than 1024 characters for any option. But other OS like Window and Linux allows more than 1024 characters for option. (This could be the issue of MacOS or they do not allowed)

**NOTE**: Those uses are using MacOS they must have to choose **Scenario 1**. If your parameters having length of >1024 characters

## Future release
We can create Swing application in future, if this application using by lots of user.

## Licence
Copyright &copy; 2014 Rakuten, Inc.

## Author
[trv-tech-search](trv-tech-search@mail.rakuten.com)