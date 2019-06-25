package com.estt.tool.test;

import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by saurabh.yagnik on 2016/10/13.
 */
@Configuration
@ContextConfiguration(locations = { "classpath:**/applicationContext-test.xml" })
public class AppConfigTest {
}
