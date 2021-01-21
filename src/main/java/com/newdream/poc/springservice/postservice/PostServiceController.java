/************************************************************************
* POC: Spring Boot application work on the Azure platform, step by step.
* WHAT:
*     class as Controller
*
*
* WHEN         WHO       WHY
* 2021-01-18   Bala      Created
/************************************************************************/
package com.newdream.poc.springservice.postservice;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/api/v1")

//Marked class as Controller
@RestController
public class PostServiceController {

	// Autowire the PostService class
	@Autowired
	private PostService postService;

	public PostServiceController() {
	}

	/************************************************************
	 * HELPER: .Description creating post mapping that post the csv detail in the
	 * database 1. Load CSV into Azure File Storage 2. Read data from Azure File
	 * Storage 3. Store in Azure SQL using JPA
	 * 
	 * .Request
	 * 
	 * .Responses For POC returning String.
	 * 
	 * .Notes
	 * 
	 * WHEN WHO WHY 2021-01-18 Bala Created
	 */
	@PostMapping(value = { "/post" })
	public ResponseEntity<?> Post(@RequestBody Map<String, Object> payload) {
		if (payload != null && payload.get("CSV_FILE_URL") != null) {
			boolean isFileUploaded = this.postService.toAzureFileStorage(payload.get("CSV_FILE_URL").toString());
			if (isFileUploaded) {
				boolean isFileLoaded = this.postService.fromAzureFileStorage();
				if (isFileLoaded) {
					boolean isRecordsInserted = this.postService.toAzureSQL();
					if (isRecordsInserted) {
						return new ResponseEntity<>("Updated", HttpStatus.OK);
					}
				}
			}
			return new ResponseEntity<>("Not updated,try again.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("Not updated,try again.", HttpStatus.BAD_REQUEST);
	}
}
