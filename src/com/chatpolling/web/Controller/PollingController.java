package com.chatpolling.web.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chatpolling.util.CommonUtil;
import com.chatpolling.web.Helper.PollingHelper;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;

@Controller
@RequestMapping(value = "/poll")
public class PollingController {

	private static Logger logger = Logger.getLogger(PollingController.class.getPackage().getName());

	@RequestMapping(value = "/createPoll", method = RequestMethod.POST)
	public @ResponseBody String createPoll(@RequestBody String requestJSON)
			throws JsonGenerationException, JsonMappingException, IOException {

		logger.info("IN PollingController -> createPoll() \n requestJSON :: " + requestJSON);
		HashMap<String, Object> responseMap = new HashMap<String, Object>();
		String responseJSON = "";
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			HashMap<String, Object> pollDetailsMap = new HashMap<String, Object>();
			TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
			};
			pollDetailsMap = objectMapper.readValue(requestJSON, typeRef);

			responseMap = PollingHelper.createPollHelper(pollDetailsMap);

		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			responseMap.put("success", false);
			responseMap.put("message", "Error on creating poll!");
		}
		responseJSON = objectMapper.writeValueAsString(responseMap);
		logger.info("\n OUT PollingController -> createPoll() \nresponseJSON to be returned is :: " + responseJSON);
		return responseJSON;
	}

	@RequestMapping(value = "/fetchPoll", method = RequestMethod.POST)
	public @ResponseBody String fetchPoll(@RequestBody String requestJSON)
			throws JsonGenerationException, JsonMappingException, IOException {

		logger.info("IN PollingController -> fetchPoll() \n requestJSON :: " + requestJSON);
		HashMap<String, Object> responseMap = new HashMap<String, Object>();
		String responseJSON = "";
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
			};
			HashMap<String, Object> requestMap = objectMapper.readValue(requestJSON, typeRef);
			String streamID = (String) requestMap.get("streamID");

			if (!CommonUtil.isEmptyString(streamID)) {
				responseMap = PollingHelper.fetchPollHelper(streamID);
			} else {
				responseMap.put("success", false);
				responseMap.put("message", "Required details are empty!");
			}

		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			responseMap.put("success", false);
			responseMap.put("message", "Error in fetching polls!");
		}
		responseJSON = objectMapper.writeValueAsString(responseMap);
		logger.info("\n OUT PollingController -> fetchPoll() \nresponseJSON to be returned is :: " + responseJSON);
		return responseJSON;
	}

	@RequestMapping(value = "/updatePollOption", method = RequestMethod.POST)
	public @ResponseBody String updatePollOption(@RequestBody String requestJSON)
			throws JsonGenerationException, JsonMappingException, IOException {

		logger.info("IN PollingController -> updatePollOption() \n requestJSON :: " + requestJSON);
		HashMap<String, Object> responseMap = new HashMap<String, Object>();
		String responseJSON = "";
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
			};
			HashMap<String, Object> requestMap = objectMapper.readValue(requestJSON, typeRef);
			String pollID = (String) requestMap.get("pollID");
			String pollOptionID = (String) requestMap.get("pollOptionID");
			String contactID = (String) requestMap.get("contactID");

			if (CommonUtil.isEmptyString(pollID) || CommonUtil.isEmptyString(pollOptionID)
					|| CommonUtil.isEmptyString(contactID)) {
				responseMap.put("success", false);
				responseMap.put("message", "Required details are empty!");
			} else {
				responseMap = PollingHelper.updatePollOptionHelper(pollID, pollOptionID, contactID);
			}

		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			responseMap.put("success", false);
			responseMap.put("message", "Error in fetching polls!");
		}
		responseJSON = objectMapper.writeValueAsString(responseMap);
		logger.info(
				"\n OUT PollingController -> updatePollOption() \nresponseJSON to be returned is :: " + responseJSON);
		return responseJSON;
	}

	@RequestMapping(value = "/getBlobSession")
	public @ResponseBody String getSession(@RequestParam(value = "action") String action, HttpServletRequest request)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("IN AdminController getBlobSession for action " + action);
		HashMap<String, Object> resultResponseMap = new HashMap<String, Object>();
		String strResponse = "";
		try {
			String blobSessionUrl = GCSUpload.instance().usession(action);
			logger.info(" blobSessionUrl = " + blobSessionUrl);
			if (CommonUtil.isEmptyString(blobSessionUrl)) {
				resultResponseMap.put(CommonUtil.Success, false);
				resultResponseMap.put(CommonUtil.error, "Failed Processing File");
			} else {
				resultResponseMap.put(CommonUtil.Success, true);
				resultResponseMap.put("blobSessionUrl", blobSessionUrl);
			}
		} catch (Exception exception) {
			logger.log(Level.SEVERE, "Exception occurred in getting the Image Upload URL -->" + exception.getMessage());
			resultResponseMap.put(CommonUtil.Success, false);
			resultResponseMap.put(CommonUtil.error, "Failed Processing File");
		}
		strResponse = new ObjectMapper().writeValueAsString(resultResponseMap);
		logger.info("OUT AdminController displayCroppedImage result " + strResponse);
		return strResponse;
	}

	@RequestMapping(value = "/uploadImage")
	public @ResponseBody String displayCroppedImage(@RequestParam(value = "mime", required = false) String mime,
			@RequestParam(value = "name", required = false) String name, HttpServletResponse response,
			HttpSession session, HttpServletRequest request) throws Exception {
		String resultResponseStr = "";
		HashMap<String, Object> resultResponseMap = new HashMap<String, Object>();
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
		System.out.println("Blobs : " + blobs);
		List<BlobKey> blobKeys = blobs.get("file1");
		System.out.println("BlobKeys : " + blobKeys);
		if (blobKeys == null || blobKeys.isEmpty()) {
			resultResponseMap.put(CommonUtil.Success, false);
			resultResponseMap.put(CommonUtil.message, "Blobkey is null...Cannot Upload.");
		} else {
			ImagesService imagesService = ImagesServiceFactory.getImagesService();
			ServingUrlOptions servingOptions = ServingUrlOptions.Builder.withBlobKey(blobKeys.get(0));
			String servingUrl = imagesService.getServingUrl(servingOptions);
			resultResponseMap.put(CommonUtil.Success, true);
			resultResponseMap.put(CommonUtil.message, "User Profile Pic Uploaded,successfully");
			resultResponseMap.put("imgUrl", servingUrl);
		}
		resultResponseStr = new ObjectMapper().writeValueAsString(resultResponseMap);
		return resultResponseStr;
	}
}
