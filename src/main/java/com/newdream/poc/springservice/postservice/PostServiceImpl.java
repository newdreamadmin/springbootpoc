/************************************************************************
* Defining the class is a "Business Service Facade".
* WHAT:
*     class as Service
*
*
* WHEN         WHO       WHY
* 2021-01-18   Bala      Created
/************************************************************************/
package com.newdream.poc.springservice.postservice;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import com.azure.storage.blob.BlobClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	BlobClientBuilder client;

	@Autowired
	private PostServiceRepo azureSQLRepo;

	// Common Locals
	@Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
	private int batchSize;
	private String blobName = null;

	public PostServiceImpl() {
	}

	@Override
	public boolean toAzureFileStorage(String CSV_FILE_URL) {
		blobName = FilenameUtils.getBaseName(CSV_FILE_URL);
		try {
			client.blobName(blobName).buildClient().copyFromUrl(CSV_FILE_URL);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean fromAzureFileStorage() {
		try {
			File temp_csv_directory = new File("/temp/");
			if (temp_csv_directory.exists()) {
				FileUtils.deleteDirectory(temp_csv_directory);
			}

			// Creates the directory named by this abstract pathname.
			temp_csv_directory.mkdir();
			File temp = new File("/temp/" + blobName);

			// Downloads the entire blob into a file specified by the path.
			client.blobName(blobName).buildClient().downloadToFile(temp.getPath());

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean toAzureSQL() {

		List<String> csvRows = null;
		List<StoreItems> rows = new ArrayList<>();

		try {

			// Tests whether the file or directory denoted by this abstract pathnameexists.
			File temp_csv_directory = new File("/temp/" + blobName);
			if (!temp_csv_directory.exists()) {
				return false;
			}

			// Reading all lines from a file as a Stream
			var reader = Files.lines(Paths.get(temp_csv_directory.getPath()));
			csvRows = reader.collect(Collectors.toList());

			// Converting csv row values to json object.
			var content = csvToJson(csvRows);

			// Creates a new JSONArray with values from the JSON string.
			JSONArray jsonArray = new JSONArray(content);

			for (int i = 0; i < jsonArray.length(); i++) {

				// Returns the value at index if it exists and is a JSONObject.
				JSONObject objects = jsonArray.getJSONObject(i);

				// The ObjectMapper class converting between Java objects and matching JSON
				// constructs.
				StoreItems bean = new ObjectMapper().readValue(objects.toString(), StoreItems.class);

				// Appends the specified element to the end of this list.
				rows.add(bean);

				// Validating batch sizes
				if (i % batchSize == 0 && i > 0) {

					// Saves all given entities.
					azureSQLRepo.saveAll(rows);

					// Removes all of the elements from this list (optional operation).The list will
					// be empty after this call returns.
					rows.clear();
				}
			}
			if (rows.size() > 0) {
				azureSQLRepo.saveAll(rows);
				rows.clear();
			}
			// Deletes a directory recursively.
			temp_csv_directory.delete();

			// Closes this stream
			reader.close();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static String csvToJson(List<String> csv) {

		// remove empty lines
		// this will affect permanently the list.
		// be careful if you want to use this list after executing this method
		csv.removeIf(e -> e.trim().isEmpty());

		// csv is empty or have declared only columns
		if (csv.size() <= 1) {
			return "[]";
		}

		// get first line = columns names
		String[] columns = csv.get(0).split(",");

		// get all rows
		StringBuilder json = new StringBuilder("[\n");
		csv.subList(1, csv.size()) // substring without first row(columns)
				.stream().map(e -> e.split(",")).filter(e -> e.length == columns.length) // values size should match
																							// with columns size
				.forEach(row -> {

					json.append("\t{\n");

					for (int i = 0; i < columns.length; i++) {
						json.append("\t\t\"").append(columns[i]).append("\" : \"").append(row[i]).append("\",\n"); // comma-1
					}

					// replace comma-1 with \n
					json.replace(json.lastIndexOf(","), json.length(), "\n");

					json.append("\t},"); // comma-2

				});

		// remove comma-2
		json.replace(json.lastIndexOf(","), json.length(), "");

		json.append("\n]");

		return json.toString();

	}

}
