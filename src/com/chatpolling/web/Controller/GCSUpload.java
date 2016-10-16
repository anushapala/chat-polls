package com.chatpolling.web.Controller;

import java.util.logging.Logger;

import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class GCSUpload {
	private GCSUpload() {
	}

	private static GCSUpload gcs = null;

	private static Logger logger = Logger.getLogger(GCSUpload.class.getPackage().getName());

	public static synchronized GCSUpload instance() {
		if (gcs == null) {
			gcs = new GCSUpload();
		}
		return gcs;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	// private final GcsService gcsService = GcsServiceFactory
	// .createGcsService(new
	// RetryParams.Builder().retryMinAttempts(0).retryMaxAttempts(1).build());

	public String usession(String action) {
		logger.info("IN GCSUpload usession() for action " + action);
		String uploadURL = "", rqrdSessionUrl = "";
		if ("uploadProfilePic".equalsIgnoreCase(action)) {
			uploadURL = "/poll/uploadImage";
			rqrdSessionUrl = this.uploadURL(uploadURL);
		}
//		if ("bulkUploadClockINSessions".equalsIgnoreCase(action)) {
//			uploadURL = "/userClockIn/uploadClockInRecordsFileToGCS";
//			rqrdSessionUrl = this.uploadURL(uploadURL);
//		} else if ("uploadProfilePic".equalsIgnoreCase(action)) {
//			
//		} else {
//			rqrdSessionUrl = "";
//		}
		logger.info("OUT GCSUPload usession() rqrdSessionUrl " + rqrdSessionUrl);
		return rqrdSessionUrl;
	}

	private String uploadURL(String url) {
		logger.info("IN GCSUpload --> uploadURL() URL = " + url);

		return BlobstoreServiceFactory.getBlobstoreService().createUploadUrl(url);
	}

}
