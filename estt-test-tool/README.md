## endpoint-smoketest-tool

Provides the automate smoke test execution of API end point(s) deployed on production.

## Development specification
[DevSpec](https://confluence.rakuten-it.com/confluence/display/TPHV/Developer+Specification+for+Endpoint+Smoke+Testing)

## Architecture and Configuration files overview
[Architecture Overview](https://confluence.rakuten-it.com/confluence/display/TPHV/Developer+Specification+for+Endpoint+Smoke+Testing#DeveloperSpecificationforEndpointSmokeTesting-ArchitectureOverview)

## Test endpoint smoke test tool
User can do smoke testing by following ways:
1. On local machine.

    Endpoint smoke test tool is standalone executable java application, so if user wants to test end points after deployment of API on server, then user can able to test by providing configuration file location.

    a. Download executable jar file from nexus server:

        http://stg-qtrrepo101z.stg.jp.local/service/local/artifact/maven/redirectr='$RELEASE_ID'&g='$GROUP_ID'&a='$ARTIFACT_ID'&v='$VERSION_ID'&p=jar

        Where,
            RELEASE_ID='public'
            GROUP_ID='travel.search-platform'
            ARTIFACT_ID='endpoint-smoketest-tool'
            VERSION_ID='0.0.1-SNAPSHOT'

    b. Create EndPointConfig.json and ErrorCode.properties file by referring example from URL:

        https://confluence.rakuten-it.com/confluence/display/TPHV/Developer+Specification+for+Endpoint+Smoke+Testing#DeveloperSpecificationforEndpointSmokeTesting-Configurationfiles

    c. Execute following command:<br/>
        ```
        java -jar endpoint-smoketest-tool.jar <<Configuration_files_directory_path>>
        ```

    If any one of endpoint not not tested correctly, then produce the status.txt file under the same directory where configuration file located.

2. Integration with Ansible server:

    If user want to do test with Ansible server, then there are two options:

    a. Manually integration testing on Ansible server, follow below link :

       [Steps With Ansible](https://confluence.rakuten-it.com/confluence/display/TPHV/Steps+to+Integrate+with+Ansible)

    b. If user want to do test with deployment Jenkin job then, user have to pass following Ansible command with Jenkin Job.

       Before copying below command to jenkin job all configuration steps must be followed from the URL:
       https://confluence.rakuten-it.com/confluence/display/TPHV/Developer+Specification+for+Endpoint+Smoke+Testing#DeveloperSpecificationforEndpointSmokeTesting-Configurationfiles

       PRODUCT_NAME=<<module_name>> ansible-playbook -i ../inventories/inventory.py deploy.yml -e 'hosts=<<target_server_name>>
       repo_name=search-api-v2 war_name=[<<module_name:artifact_version>>] build_user=‘<<user_short_name>>’ env_code=dev ver=20161107
       cmd=<<test_script_recpective_to_test_api>> rs_branch=‘<<branch_of_release_service>>’ --tags 'static,war' -vv -K

## Links
[Links](https://confluence.rakuten-it.com/confluence/display/TPHV/Developer+Specification+for+Endpoint+Smoke+Testing#DeveloperSpecificationforEndpointSmokeTesting-Link)

## Limitation
This tool can be used only for those API which are giving standard format of error response with valid error code. If any API does not returning standard/valid response then this tool can not be useful.

## Future release
We are planing create web application to configure end points and error codes. Once user provides all detail and press Run button, this tool will pickup all configuration and download the reports after successful execution.

## Licence
Copyright &copy; 2014 Rakuten, Inc.

## Author
[trv-tech-search](trv-tech-search@mail.rakuten.com)