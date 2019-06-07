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
	
	private static final String STATUS_FILE_NAME = "status";

	@RequestMapping(value = "/endpointsStatusCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> getEpStatusForPieChart(@RequestParam("userId") String userId) {
		
		Map<String, Object> responseMap = new HashMap<String, Object>();
		
		responseMap.put("EC", redisHelper.getValue(userId.concat("-EC")));
		responseMap.put("SC", redisHelper.getValue(userId.concat("-SC")));
		responseMap.put("status", redisHelper.getValue(userId.concat("-STATUS"))); // status code : 0-Started, 1-Finished, 2-Error
		String fileName = STATUS_FILE_NAME.concat("_").concat(userId).concat(".txt");
		String statusFilePathWithNameForUserId = statusFilePath.concat("/").concat(fileName);
		File fileStatus = new File(statusFilePathWithNameForUserId);
		if (fileStatus.exists()) {
			responseMap.put("statusFile", statusFilePathWithNameForUserId);
		}
		return ResponseEntity.ok(responseMap);
	}
	
	@RequestMapping(value = "/listStatusDetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EndPointStatusDto>> listStatus(@RequestParam(required = false, name="status") String status, @RequestParam("userId") String userId) throws IOException {
	   List<EndPointStatusDto> response = new ArrayList<EndPointStatusDto>();
	   String fileName = STATUS_FILE_NAME.concat("_").concat(userId).concat(".txt");
	   String statusFilePathWithNameForUserId = statusFilePath.concat("/").concat(fileName);
	   File fileStatus = new File(statusFilePathWithNameForUserId);
		if (fileStatus.exists()) {
			List<String> lines = Files.readAllLines(Paths.get(statusFilePathWithNameForUserId));
			for(String line : lines) {
				String[] splitMsg = line.split("\\->");
				String statusOfUrl = splitMsg[0].trim();
				String url = splitMsg[1].trim().substring(splitMsg[1].trim().indexOf("[") + 1, splitMsg[1].trim().indexOf("]"));
				EndPointStatusDto endPointStatusDto = new EndPointStatusDto(statusOfUrl, url, splitMsg[1].trim().substring(splitMsg[1].trim().indexOf("]") + 1, splitMsg[1].trim().length()));
				if ((!StringUtils.isEmpty(status) && status.equalsIgnoreCase(statusOfUrl)) 
						|| (StringUtils.isEmpty(status) || "null".equalsIgnoreCase(status))) {
					response.add(endPointStatusDto);
				}
			}
		} else {
			throw new IOException("Status file with name {"+ fileName +" } not exists at location : {" + statusFilePath + "}");
		}
		return ResponseEntity.ok(response);
	}
}
