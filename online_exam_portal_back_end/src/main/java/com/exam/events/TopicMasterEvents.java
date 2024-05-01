package com.exam.events;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import com.exam.forms.HibernateValidationMaster;
import com.exam.forms.checks.TopicMasterCheck;
import com.exam.helper.HibernateHelper;
import com.exam.util.ConstantValues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TopicMasterEvents {

	private static final String MODULE = TopicMasterEvents.class.getName();
	private static final String RES_ERR = "OnlineExamPortalUiLabels";

	public static String createTopicMasterEvent(HttpServletRequest request, HttpServletResponse response) {

		Locale locale = UtilHttp.getLocale(request);

		Delegator delegator = (Delegator) request.getAttribute("delegator");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

		String topicName = (String) request.getAttribute(ConstantValues.TOPIC_NAME);

		Map<String, Object> topicInfo = UtilMisc.toMap(ConstantValues.TOPIC_NAME, topicName);

		try {
			Debug.logInfo("=======Creating TopicMaster record in event using service CreateTopicMaster=========",
					MODULE);
			HibernateValidationMaster hibernate = HibernateHelper.populateBeanFromMap(topicInfo,
					HibernateValidationMaster.class);

			Set<ConstraintViolation<HibernateValidationMaster>> errors = HibernateHelper
					.checkValidationErrors(hibernate, TopicMasterCheck.class);
			boolean hasFormErrors = HibernateHelper.validateFormSubmission(delegator, errors, request, locale,
					"Mandatory Err TopicMaster Entity", RES_ERR, false);

			if (!hasFormErrors) {
				try {
					Map<String, ? extends Object> createTopicMasterInfoResult = dispatcher.runSync("CreateTopicMaster",
							topicInfo);
					ServiceUtil.getMessages(request, createTopicMasterInfoResult, null);
					if (ServiceUtil.isError(createTopicMasterInfoResult)) {
						String errorMessage = ServiceUtil.getErrorMessage(createTopicMasterInfoResult);
						request.setAttribute("_ERROR_MESSAGE_", errorMessage);
						Debug.logError(errorMessage, MODULE);
						return "error";
					} else {
						String successMessage = "Create Topic Service executed successfully.";
						ServiceUtil.getMessages(request, createTopicMasterInfoResult, successMessage);
						request.setAttribute("_EVENT_MESSAGE_", successMessage);
						return "success";
					}
				} catch (GenericServiceException e) {
					String errMsg = "Error setting topic info: " + e.toString();
					request.setAttribute("_ERROR_MESSAGE_", errMsg);
					return "error";
				}
			} else {
				request.setAttribute("_ERROR_MESSAGE", errors);
				return "error";
			}
		} catch (Exception e) {
			Debug.logError(e, MODULE);
			request.setAttribute("_ERROR_MESSAGE", e);
			return "error";
		}
	}

	public static String fetchTopicMasterEvent(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		List<Map<String, Object>> topicMasterdata = new ArrayList<Map<String, Object>>();
		try {
			List<GenericValue> listOfTopicMasterData = EntityQuery.use(delegator).from("TopicMaster").queryList();
			if (UtilValidate.isNotEmpty(listOfTopicMasterData)) {
				for (GenericValue list : listOfTopicMasterData) {
					Map<String, Object> listOfTopicMasterEntity = new HashMap<String, Object>();
					listOfTopicMasterEntity.put(ConstantValues.TOPIC_ID, list.get(ConstantValues.TOPIC_ID));
					listOfTopicMasterEntity.put(ConstantValues.TOPIC_NAME, list.get(ConstantValues.TOPIC_NAME));
					topicMasterdata.add(listOfTopicMasterEntity);
				}
				request.setAttribute("TopicMaster", topicMasterdata);
				return "success";
			} else {
				String errorMessage = "No matched fields in TopicMaster Entity";
				request.setAttribute("_ERROR_MESSAGE_", errorMessage);
				Debug.logError(errorMessage, MODULE);
				return "error";
			}
		} catch (GenericEntityException e) {
			request.setAttribute("Error", e);
			return "error";
		}
	}

	public static String updateTopicMasterEvent(HttpServletRequest request, HttpServletResponse response) {

		Locale locale = UtilHttp.getLocale(request);

		Delegator delegator = (Delegator) request.getAttribute("delegator");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		String topicId = (String) request.getAttribute(ConstantValues.TOPIC_ID);
		String topicName = (String) request.getAttribute(ConstantValues.TOPIC_NAME);
		Map<String, Object> topicInfo = UtilMisc.toMap(ConstantValues.TOPIC_ID, topicId, ConstantValues.TOPIC_NAME,
				topicName);

		try {
			Debug.logInfo("=======Updating TopicMaster record in event using service UpdateTopicMaster=========",
					MODULE);
			HibernateValidationMaster hibernate = HibernateHelper.populateBeanFromMap(topicInfo,
					HibernateValidationMaster.class);

			Set<ConstraintViolation<HibernateValidationMaster>> errors = HibernateHelper
					.checkValidationErrors(hibernate, TopicMasterCheck.class);
			boolean hasFormErrors = HibernateHelper.validateFormSubmission(delegator, errors, request, locale,
					"Mandatory Err TopicMaster Entity", RES_ERR, false);

			if (!hasFormErrors) {
				try {
					Map<String, ? extends Object> updateTopicMasterInfoResult = dispatcher.runSync("UpdateTopicMaster",
							topicInfo);
					ServiceUtil.getMessages(request, updateTopicMasterInfoResult, null);
					if (ServiceUtil.isError(updateTopicMasterInfoResult)) {
						String errorMessage = ServiceUtil.getErrorMessage(updateTopicMasterInfoResult);
						request.setAttribute("_ERROR_MESSAGE_", errorMessage);
						Debug.logError(errorMessage, MODULE);
						return "error";
					} else {
						String successMessage = "Update Topic Service executed successfully.";
						ServiceUtil.getMessages(request, updateTopicMasterInfoResult, successMessage);
						request.setAttribute("_EVENT_MESSAGE_", successMessage);
						Debug.logError(successMessage, MODULE);
						return "success";
					}
				} catch (GenericServiceException e) {
					String errMsg = "Error setting topic info: " + e.toString();
					request.setAttribute("_ERROR_MESSAGE_", errMsg);
					return "error";
				}
			} else {
				request.setAttribute("_ERROR_MESSAGE", errors);
				return "error";
			}
		} catch (Exception e) {
			Debug.logError(e, MODULE);
			request.setAttribute("_ERROR_MESSAGE", e);
			return "error";
		}
	}

	public static String deleteTopicMasterEvent(HttpServletRequest request, HttpServletResponse response) {
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		String topicId = (String) request.getAttribute(ConstantValues.TOPIC_ID);
		Map<String, Object> topicInfo = UtilMisc.toMap(ConstantValues.TOPIC_ID, topicId);

		try {
			Debug.logInfo("=======Deleting TopicMaster record in event using service DeleteTopicMaster=========",
					MODULE);
			try {
				Map<String, ? extends Object> deleteTopicMasterInfoResult = dispatcher.runSync("DeleteTopicMaster",
						topicInfo);
				if (UtilValidate.isNotEmpty(topicInfo)) {
					String successMessage = "delete TopicMaster Service executed successfully.";
					ServiceUtil.getMessages(request, deleteTopicMasterInfoResult, successMessage);
					request.setAttribute("_EVENT_MESSAGE_", successMessage);
					Debug.logError(successMessage, MODULE);
					return "success";
				} else {
					String errorMessage = ServiceUtil.getErrorMessage(deleteTopicMasterInfoResult);
					request.setAttribute("_ERROR_MESSAGE_", errorMessage);
					Debug.logError(errorMessage, MODULE);
					return "error";
				}
			} catch (GenericServiceException e) {
				String errMsg = "Error deleting topic info: " + e.toString();
				request.setAttribute("_ERROR_MESSAGE_", errMsg);
				return "error";
			}
		} catch (Exception e) {
			Debug.logError(e, MODULE);
			request.setAttribute("_ERROR_MESSAGE", e);
			return "error";
		}
	}

}
