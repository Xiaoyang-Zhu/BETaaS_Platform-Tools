/*
Copyright 2014-2015 Intecs Spa

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/

package eu.betaas.service.dependabilitymanager;

/**
 * It contains the failures report row info 
 * @author Intecs
 */
public class FailureReportRow {

	/*public String date;
	public String layer; 
	public String code;
	public String level;
	public String originator; 
	public String description;*/
	
	public String notification_time;
	
	public int layer;
	
	public int code;
	
	public int level;
	
	public String originator;
	
	public String description;
	
	public FailureReportRow(String localDate, int localLayer,int localCode, int localLevel,String localOriginator, String localDescription){
		notification_time = localDate;
		layer = localLayer; 
		code = localCode;
		level = localLevel;
		originator = localOriginator; 
		description = localDescription;
	}
}
