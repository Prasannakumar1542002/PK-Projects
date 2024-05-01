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
import org.apache.ofbiz.base.util.UtilProperties;
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
import com.exam.util.EntityConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TopicMasterEvents {

	private static final String MODULE = TopicMasterEvents.class.getName();
	private static final String RES_ERR = "OnlineExamPortalUiLabels";

	public static String createTopic(HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, Object> combinedMap = UtilHttp.getCombinedMap(request);
		Locale locale = UtilHttp.getLocale(request);

		Delegator delegator = (Delegator) combinedMap.get(EntityConstants.DELEGATOR);
		LocalDispatcher dispatcher = (LocalDispatcher) combinedMap.get(EntityConstants.DISPATCHER);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(EntityConstants.USER_LOGIN);

		String topicName = (String) combinedMap.get(ConstantValues.TOPIC_NAME);

		Map<String, Object> topicInfo = UtilMisc.toMap(ConstantValues.TOPIC_NAME, topicName, EntityConstants.USER_LOGIN,
				userLogin);

		try {
			Debug.logInfo("=======Creating TopicMaster record in event using service CreateTopicMaster=========",
					MODULE);
			HibernateValidationMaster hibernate = HibernateHelper.populateBeanFromMap(topicInfo,
					HibernateValidationMaster.class);

			Set<ConstraintViolation<HibernateValidationMaster>> errors = HibernateHelper
					.checkValidationErrors(hibernate, TopicMasterCheck.class);
			boolean hasFormErrors = HibernateHelper.validateFormSubmission(delegator, errors, request, locale,
					"Mandatory Err TopicMaster Entity", RES_ERR, false);

			if (hasFormErrors) {
				request.setAttribute("_ERROR_MESSAGE", errors);
				return "error";
			}
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
					String successMessage = UtilProperties.getMessage(RES_ERR, "ServiceSuccessMessage",
							UtilHttp.getLocale(request));
					ServiceUtil.getMessages(request, createTopicMasterInfoResult, successMessage);
					request.setAttribute("_EVENT_MESSAGE_", successMessage);
					return "success";
				}
			} catch (GenericServiceException e) {
				String errorMessage = UtilProperties.getMessage(RES_ERR, "ServiceCallingError",
						UtilHttp.getLocale(request)) + e.toString();
				request.setAttribute("_ERROR_MESSAGE_", errorMessage);
				return "error";
			}

		} catch (Exception e) {
			Debug.logError(e, MODULE);
			request.setAttribute("_ERROR_MESSAGE", e);
			return "error";
		}
	}

	public static String fetchOneTopic(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute("delegator");

		String topicId = (String) request.getAttribute(ConstantValues.TOPIC_ID);
		try {
			GenericValue fetchedTopic = EntityQuery.use(delegator).select(ConstantValues.TOPIC_NAME).from("TopicMaster")
					.where(ConstantValues.TOPIC_ID, topicId).cache().queryOne();

			request.setAttribute("TopicMaster", fetchedTopic);
			return "success";

		} catch (GenericEntityException e) {
			request.setAttribute("Error", e);
			return "error";
		}
	}

	public static String fetchAllTopics(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute(EntityConstants.DELEGATOR);
		List<Map<String, Object>> viewTopicList = new ArrayList<Map<String, Object>>();
		try {
			List<GenericValue> listOfTopics = EntityQuery.use(delegator).from("TopicMaster").cache().queryList();
			if (UtilValidate.isNotEmpty(listOfTopics)) {
				for (GenericValue topic : listOfTopics) {
					Map<String, Object> topicList = new HashMap<String, Object>();
					topicList.put(ConstantValues.TOPIC_ID, topic.get(ConstantValues.TOPIC_ID));
					topicList.put(ConstantValues.TOPIC_NAME, topic.get(ConstantValues.TOPIC_NAME));
					viewTopicList.add(topicList);
				}
				Map<String, Object> topicsInfo = new HashMap<>();
				topicsInfo.put("TopicList", viewTopicList);
				request.setAttribute("TopicInfo", topicsInfo);
				return "success";
			} else {
				String errorMessage = UtilProperties.getMessage(RES_ERR, "ErrorInFetchingData",
						UtilHttp.getLocale(request));
				request.setAttribute("_ERROR_MESSAGE_", errorMessage);
				Debug.logError(errorMessage, MODULE);
				return "error";
			}
		} catch (GenericEntityException e) {
			request.setAttribute("Error", e);
			return "error";
		}
	}

	public static String updateTopic(HttpServletRequest request, HttpServletResponse response) {

		Locale locale = UtilHttp.getLocale(request);

		Delegator delegator = (Delegator) request.getAttribute(EntityConstants.DELEGATOR);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(EntityConstants.DISPATCHER);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(EntityConstants.USER_LOGIN);
		String topicId = (String) request.getAttribute(ConstantValues.TOPIC_ID);
		String topicName = (String) request.getAttribute(ConstantValues.TOPIC_NAME);
		Map<String, Object> topicInfo = UtilMisc.toMap(ConstantValues.TOPIC_ID, topicId, ConstantValues.TOPIC_NAME,
				topicName, EntityConstants.USER_LOGIN, userLogin);

		try {
			Debug.logInfo("=======Updating TopicMaster record in event using service UpdateTopicMaster=========",
					MODULE);
			HibernateValidationMaster hibernate = HibernateHelper.populateBeanFromMap(topicInfo,
					HibernateValidationMaster.class);

			Set<ConstraintViolation<HibernateValidationMaster>> errors = HibernateHelper
					.checkValidationErrors(hibernate, TopicMasterCheck.class);
			boolean hasFormErrors = HibernateHelper.validateFormSubmission(delegator, errors, request, locale,
					"Mandatory Err TopicMaster Entity", RES_ERR, false);

			if (hasFormErrors) {
				request.setAttribute("_ERROR_MESSAGE", errors);
				return "error";
			}
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
					String successMessage = UtilProperties.getMessage(RES_ERR, "ServiceSuccessMessage",
							UtilHttp.getLocale(request));
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
		} catch (Exception e) {
			Debug.logError(e, MODULE);
			request.setAttribute("_ERROR_MESSAGE", e);
			return "error";
		}
	}

	public static String deleteTopic(HttpServletRequest request, HttpServletResponse response) {
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(EntityConstants.DISPATCHER);
		String topicId = (String) request.getAttribute(ConstantValues.TOPIC_ID);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(EntityConstants.USER_LOGIN);
		Map<String, Object> topicInfo = UtilMisc.toMap(ConstantValues.TOPIC_ID, topicId, EntityConstants.USER_LOGIN,
				userLogin);

		try {
			Debug.logInfo("=======Deleting TopicMaster record in event using service DeleteTopicMaster=========",
					MODULE);
			try {
				Map<String, ? extends Object> deleteTopicMasterInfoResult = dispatcher.runSync("DeleteTopicMaster",
						topicInfo);
				if (UtilValidate.isNotEmpty(topicInfo)) {
					String successMessage = UtilProperties.getMessage(RES_ERR, "DeleteErrorMessage",
							UtilHttp.getLocale(request));
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
				String errorMessage = UtilProperties.getMessage(RES_ERR, "DeleteErrorMessage",
						UtilHttp.getLocale(request)) + e.toString();
				request.setAttribute("_ERROR_MESSAGE_", errorMessage);
				return "error";
			}
		} catch (Exception e) {
			Debug.logError(e, MODULE);
			request.setAttribute("_ERROR_MESSAGE", e);
			return "error";
		}
	}

}
