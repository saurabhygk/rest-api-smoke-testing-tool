package com.estt.web.dashboard.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estt.tool.test.redis.helper.BaseRedisHelper;
import com.estt.web.dashboard.dto.EndPointStatusDto;

@RestController
@RequestMapping("/real-time-data")
public class EndPointStatusController {
	
	@Autowired
	private BaseRedisHelper<Integer> redisHelper;
	
	@Value("${status.file.location}")
	private String statusFilePath;
	
	@Value("${status.file.name}")
	private String statusFileName;

	@RequestMapping(value = "/endpointsStatusCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Integer>> getEpStatusForPieChart(@RequestParam("userId") String userId) {
		
		Map<String, Integer> responseMap = new HashMap<String, Integer>();
		
		responseMap.put("EC", redisHelper.getValue(userId.concat("-EC")));
		responseMap.put("SC", redisHelper.getValue(userId.concat("-SC")));
		responseMap.put("status", redisHelper.getValue(userId.concat("-STATUS"))); // status code : 0-Started, 1-Finished, 2-Error
		return ResponseEntity.ok(responseMap);
	}
	
	@RequestMapping(value = "/listStatusDetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EndPointStatusDto>> listStatus(@RequestParam(required = false, name="status") String status) throws IOException {
	   List<EndPointStatusDto> response = new ArrayList<EndPointStatusDto>();
	   File fileStatus = new File(statusFilePath.concat("/").concat(statusFileName));
		if (fileStatus.exists()) {
			List<String> lines = Files.readAllLines(Paths.get(statusFilePath.concat("/").concat(statusFileName)));
			for(String line : lines) {
				String[] splitMsg = line.split("\\->");
				String statusOfUrl = splitMsg[0].trim();
				String url = splitMsg[1].trim().substring(splitMsg[1].trim().indexOf("[") + 1, splitMsg[1].trim().indexOf("]"));
				EndPointStatusDto endPointStatusDto = new EndPointStatusDto(statusOfUrl, url, splitMsg[1].trim().substring(splitMsg[1].trim().indexOf("]") + 1, splitMsg[1].trim().length()));
				if (!StringUtils.isEmpty(status) && status.equalsIgnoreCase(statusOfUrl)) {
					response.add(endPointStatusDto);
				} 
				if(StringUtils.isEmpty(status) || "null".equalsIgnoreCase(status)) {
					response.add(endPointStatusDto);
				}
			}
		} else {
			throw new IOException("Status file with name {"+ statusFileName +" } not exists at location : {" + statusFilePath + "}");
		}
		return ResponseEntity.ok(response);
	}
}
