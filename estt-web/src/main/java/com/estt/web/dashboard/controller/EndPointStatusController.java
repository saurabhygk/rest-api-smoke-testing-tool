package com.estt.web.dashboard.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
	private BaseRedisHelper<Object> redisHelper;

	@RequestMapping(value = "/endpointsStatusCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> getEpStatusForPieChart(@RequestParam("userId") String userId) {

		Map<String, Object> responseMap = new HashMap<String, Object>();

		responseMap.put("EC", redisHelper.getValue(userId.concat("-EC")));
		responseMap.put("SC", redisHelper.getValue(userId.concat("-SC")));
		responseMap.put("status", redisHelper.getValue(userId.concat("-STATUS"))); // status code : 0-Started, 1-Finished, 2-Error
		responseMap.put("statusFile", (String) redisHelper.getValue(userId.concat("-STATUS_FILEPATH")));
		return ResponseEntity.ok(responseMap);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/listStatusDetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EndPointStatusDto>> listStatus(@RequestParam(required = false, name = "status") String status, @RequestParam("userId") String userId) throws Exception {
		List<EndPointStatusDto> response = new ArrayList<EndPointStatusDto>();
		List<String> statusDataList = (List<String>) redisHelper.getValue(userId.concat("-DATA"));

		if (!statusDataList.isEmpty()) {
			for (String line : statusDataList) {
				String[] splitMsg = line.split("\\->");
				String statusOfUrl = splitMsg[0].trim();
				String url = splitMsg[1].trim().substring(splitMsg[1].trim().indexOf("[") + 1,
						splitMsg[1].trim().indexOf("]"));
				EndPointStatusDto endPointStatusDto = new EndPointStatusDto(statusOfUrl, url,
						splitMsg[1].trim().substring(splitMsg[1].trim().indexOf("]") + 1, splitMsg[1].trim().length()));
				if ((!StringUtils.isEmpty(status) && status.equalsIgnoreCase(statusOfUrl))
						|| (StringUtils.isEmpty(status) || "null".equalsIgnoreCase(status))) {
					response.add(endPointStatusDto);
				}
			}
		} else {
			throw new Exception("Status not foud in redis cache..!!!");
		}
		return ResponseEntity.ok(response);
	}

	@RequestMapping(value = "/deleteStatusFile", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EndPointStatusDto>> deleteStatusFile(@RequestParam("userId") String userId)
			throws IOException {
		String statusFilePath = (String) redisHelper.getValue(userId.concat("-STATUS_FILEPATH"));
		List<EndPointStatusDto> response = new ArrayList<EndPointStatusDto>();
		File statusFile = new File(statusFilePath);
		if (statusFile.exists()) {
			statusFile.delete();
			redisHelper.deleteKey(userId.concat("-EC"));
			redisHelper.deleteKey(userId.concat("-SC"));
			redisHelper.deleteKey(userId.concat("-STATUS"));
			redisHelper.deleteKey(userId.concat("-DATA"));
			redisHelper.deleteKey(userId.concat("-STATUS_FILEPATH"));
		} else {
			throw new IOException(statusFilePath + " not exists");
		}
		return ResponseEntity.ok(response);
	}
}
