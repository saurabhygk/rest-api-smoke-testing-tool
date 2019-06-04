package com.estt.web.dashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	
	@RequestMapping("/")
	public String home() {
		return "home";
	}
	
	@RequestMapping("/progress")
	public String preogress() {
		return "progress";
	}
	
	@RequestMapping("/listStatusDetails")
	public String listStatusDetails() {
		return "status-detail";
	}
	
	@RequestMapping("/createEsttConfigFile")
	public String createEsttConfigFile() {
		return "generate-estt-config";
	}
}
