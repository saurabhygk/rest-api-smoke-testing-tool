/**
 * Copyright (c) Rakuten Travel, Inc. All Rights Reserved.
 *
 * This program is the information assets which are handled
 * as "Strictly Confidential".
 * Permission of Use is only admitted in Rakuten Inc.
 * Development Department.
 * If you don't have permission , MUST not be published,
 * broadcast, rewritten for broadcast or publication
 * or redistributed directly or indirectly in any medium.
 *
 * $Id$
 * Created on 2016/12/13
 * Updated on 2016/12/13
 */
package com.estt.smoke.test.config;

import com.estt.config.creator.EndPointConfigCreatorByCommandLine;
import com.estt.config.creator.EndPointConfigCreatorByDataFile;

/**
 * This application will generate configuration file(s) for end point smoke test tool
 * * EndPointConfig.json
 * * ErrorCodes.properties
 */
public class EndPointConfigCreator {
	// args[0] : to store created config file
	// args[1] : Optional : if user wants to create configuration file by reading datafile (location of datafile with name datafile.txt)
	public static void main(String[] args) throws Exception{
		if(args.length == 1) {
			// create configuration file by asking options from command line
			EndPointConfigCreatorByCommandLine cmd = new EndPointConfigCreatorByCommandLine(args[0]);
			cmd.createConfigurationFiles();
		} else if(args.length == 2){
			// create configuration file by reading datafile
			EndPointConfigCreatorByDataFile datFile = new EndPointConfigCreatorByDataFile(args[0], args[1]);
			datFile.createConfigurationFiles(null, null);
		} else{
			System.out.println("Please try again by following either one option:");
			System.out.println("\t 1. provide second argument to create configuration file using data file");
			System.out.println("\t 2. Only provide first argument to create configuration file using command line options");
			return;
		}
	}
}
