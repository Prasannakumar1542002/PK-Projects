package com.exam.events;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.exam.util.ConstantValues;
import com.exam.util.EntityConstants;

public class UserExamMappingEvents {
	private static final String MODULE = UserExamMappingEvents.class.getName();
	private static final String RES_ERR = "OnlineexamUiLabels";

	public static String createExamForUser(HttpServletRequest request, HttpServletResponse response) {

		Locale locale = UtilHttp.getLocale(request);

		Delegator delegator = (Delegator) request.getAttribute(EntityConstants.DELEGATOR);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(EntityConstants.DISPATCHER);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(EntityConstants.USER_LOGIN);

		String partyId = (String) request.getAttribute(ConstantValues.PARTY_ID);
		String examId = (String) request.getAttribute(ConstantValues.EXAM_ID);
		String allowedAttempts = (String) request.getAttribute(ConstantValues.USEREXAM_ALLOWED_ATTEMPTS);
		String noOfAttempts = (String) request.getAttribute(ConstantValues.USEREXAM_NO_OF_ATTEMPTS);
		String lastPerformanceDate = (String) request.getAttribute(ConstantValues.USEREXAM_LAST_DATE);
		String timeoutDays = (String) request.getAttribute(ConstantValues.USEREXAM_TIMEOUT_DAYS);
		String passwordChangesAuto = (String) request.getAttribute(ConstantValues.USEREXAM_PASSWORD_CHANGE);
		String canSplitExams = (String) request.getAttribute(ConstantValues.USEREXAM_SPLIT);
		String canSeeDetailedResults = (String) request.getAttribute(ConstantValues.USEREXAM_SEE_RESULT);
		String maxSplitAttempts = (String) request.getAttribute(ConstantValues.USEREXAM_MAX_SPLIT);

		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime lastPerformanceDateLocalDateTime = LocalDateTime.parse(lastPerformanceDate, inputFormatter);
		String lastPerformanceDateParsed = lastPerformanceDateLocalDateTime.format(outputFormatter);

		Map<String, Object> userexamInfo = UtilMisc.toMap(ConstantValues.PARTY_ID, partyId, ConstantValues.EXAM_ID,
				examId, ConstantValues.USEREXAM_ALLOWED_ATTEMPTS, allowedAttempts,
				ConstantValues.USEREXAM_NO_OF_ATTEMPTS, noOfAttempts, ConstantValues.USEREXAM_LAST_DATE,
				lastPerformanceDateParsed, ConstantValues.USEREXAM_TIMEOUT_DAYS, timeoutDays,
				ConstantValues.USEREXAM_PASSWORD_CHANGE, passwordChangesAuto, ConstantValues.USEREXAM_SPLIT,
				canSplitExams, ConstantValues.USEREXAM_SEE_RESULT, canSeeDetailedResults,
				ConstantValues.USEREXAM_MAX_SPLIT, maxSplitAttempts, EntityConstants.USER_LOGIN, userLogin);

		try {
			Debug.logInfo(
					"=======Creating UserExamMapping record in event using service CreateUserExamMapping=========",
					MODULE);
			boolean hasFormErrors = false;

			if (hasFormErrors) {
				String errorMessage = UtilProperties.getMessage(RES_ERR, "ServiceCallingError",
						UtilHttp.getLocale(request));
				request.setAttribute("_ERROR_MESSAGE", errorMessage);
				return "error";
			}
			try {
				Map<String, ? extends Object> UserExamMappingInfoResult = dispatcher.runSync("CreateUserExamMapping",
						userexamInfo);
				ServiceUtil.getMessages(request, UserExamMappingInfoResult, null);
				if (ServiceUtil.isError(UserExamMappingInfoResult)) {
					String errorMessage = ServiceUtil.getErrorMessage(UserExamMappingInfoResult);
					request.setAttribute("ERROR_MESSAGE", errorMessage);
					Debug.logError(errorMessage, MODULE);
					return "error";
				} else {
					String successMessage = UtilProperties.getMessage(RES_ERR, "ServiceSuccessMessage",
							UtilHttp.getLocale(request));
					ServiceUtil.getMessages(request, UserExamMappingInfoResult, successMessage);
					request.setAttribute("EVENT_MESSAGE", successMessage);
					return "success";
				}
			} catch (GenericServiceException e) {
				String errMsg = "Error setting userexam info: " + e.toString();
				request.setAttribute("ERROR_MESSAGE", errMsg);
				return "error";
			}
		} catch (Exception e) {
			Debug.logError(e, MODULE);
			request.setAttribute("_ERROR_MESSAGE", e);
			return "error";
		}
	}

	public static String fetchAllUsers(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute(EntityConstants.DELEGATOR);
		List<Map<String, Object>> viewUserExamMapList = new ArrayList<Map<String, Object>>();
		try {
			List<GenericValue> listOfUserExamMapData = EntityQuery.use(delegator).from("UserExamMapping").queryList();
			if (UtilValidate.isNotEmpty(listOfUserExamMapData)) {
				for (GenericValue user : listOfUserExamMapData) {
					Map<String, Object> userexamList = new HashMap<String, Object>();
					userexamList.put(ConstantValues.PARTY_ID, user.get(ConstantValues.PARTY_ID));
					userexamList.put(ConstantValues.EXAM_ID, user.get(ConstantValues.EXAM_ID));
					userexamList.put(ConstantValues.USEREXAM_ALLOWED_ATTEMPTS,
							user.get(ConstantValues.USEREXAM_ALLOWED_ATTEMPTS));
					userexamList.put(ConstantValues.USEREXAM_NO_OF_ATTEMPTS,
							user.get(ConstantValues.USEREXAM_NO_OF_ATTEMPTS));
					userexamList.put(ConstantValues.USEREXAM_LAST_DATE, user.get(ConstantValues.USEREXAM_LAST_DATE));
					userexamList.put(ConstantValues.USEREXAM_TIMEOUT_DAYS,
							user.get(ConstantValues.USEREXAM_TIMEOUT_DAYS));
					userexamList.put(ConstantValues.USEREXAM_PASSWORD_CHANGE,
							user.get(ConstantValues.USEREXAM_PASSWORD_CHANGE));
					userexamList.put(ConstantValues.USEREXAM_SPLIT, user.get(ConstantValues.USEREXAM_SPLIT));
					userexamList.put(ConstantValues.USEREXAM_SEE_RESULT, user.get(ConstantValues.USEREXAM_SEE_RESULT));
					userexamList.put(ConstantValues.USEREXAM_MAX_SPLIT, user.get(ConstantValues.USEREXAM_MAX_SPLIT));
					viewUserExamMapList.add(userexamList);
				}
				Map<String, Object> userofExamInfo = new HashMap<>();
				userofExamInfo.put("UserExamList", viewUserExamMapList);
				request.setAttribute("UserExamInfo", userofExamInfo);
				return "success";
			} else {
				String errorMessage = UtilProperties.getMessage(RES_ERR, "ErrorInFetchingData",
						UtilHttp.getLocale(request));
				request.setAttribute("ERROR_MESSAGE", errorMessage);
				Debug.logError(errorMessage, MODULE);
				return "error";
			}
		} catch (GenericEntityException e) {
			request.setAttribute("Error", e);
			return "error";
		}
	}

	public static String updateUserForExam(HttpServletRequest request, HttpServletResponse response) {

		Locale locale = UtilHttp.getLocale(request);

		Delegator delegator = (Delegator) request.getAttribute(EntityConstants.DELEGATOR);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(EntityConstants.DISPATCHER);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(EntityConstants.USER_LOGIN);

		String partyId = (String) request.getAttribute(ConstantValues.PARTY_ID);
		String examId = (String) request.getAttribute(ConstantValues.EXAM_ID);
		String allowedAttempts = (String) request.getAttribute(ConstantValues.USEREXAM_ALLOWED_ATTEMPTS);
		String noOfAttempts = (String) request.getAttribute(ConstantValues.USEREXAM_NO_OF_ATTEMPTS);
		String lastPerformanceDate = (String) request.getAttribute(ConstantValues.USEREXAM_LAST_DATE);
		String timeoutDays = (String) request.getAttribute(ConstantValues.USEREXAM_TIMEOUT_DAYS);
		String passwordChangesAuto = (String) request.getAttribute(ConstantValues.USEREXAM_PASSWORD_CHANGE);
		String canSplitExams = (String) request.getAttribute(ConstantValues.USEREXAM_SPLIT);
		String canSeeDetailedResults = (String) request.getAttribute(ConstantValues.USEREXAM_SEE_RESULT);
		String maxSplitAttempts = (String) request.getAttribute(ConstantValues.USEREXAM_MAX_SPLIT);

		Map<String, Object> userexamInfo = UtilMisc.toMap(ConstantValues.PARTY_ID, partyId, ConstantValues.EXAM_ID,
				examId, ConstantValues.USEREXAM_ALLOWED_ATTEMPTS, allowedAttempts,
				ConstantValues.USEREXAM_NO_OF_ATTEMPTS, noOfAttempts, ConstantValues.USEREXAM_LAST_DATE,
				lastPerformanceDate, ConstantValues.USEREXAM_TIMEOUT_DAYS, timeoutDays,
				ConstantValues.USEREXAM_PASSWORD_CHANGE, passwordChangesAuto, ConstantValues.USEREXAM_SPLIT,
				canSplitExams, ConstantValues.USEREXAM_SEE_RESULT, canSeeDetailedResults,
				ConstantValues.USEREXAM_MAX_SPLIT, maxSplitAttempts, EntityConstants.USER_LOGIN, userLogin);
		try {
			Debug.logInfo(
					"=======Updating UserExamMapping record in event using service UpdateUserExamMapping=========",
					MODULE);
			boolean hasFormErrors = false;
			if (hasFormErrors) {
				String errorMessage = UtilProperties.getMessage(RES_ERR, "ServiceCallingError",
						UtilHttp.getLocale(request));
				request.setAttribute("_ERROR_MESSAGE", errorMessage);
				return "error";
			}
			try {
				Map<String, ? extends Object> updateUserExamMappingInfoResult = dispatcher
						.runSync("UpdateUserExamMapping", UtilMisc.<String, Object>toMap(userexamInfo));
				ServiceUtil.getMessages(request, updateUserExamMappingInfoResult, null);
				if (ServiceUtil.isError(updateUserExamMappingInfoResult)) {
					String errorMessage = ServiceUtil.getErrorMessage(updateUserExamMappingInfoResult);
					request.setAttribute("ERROR_MESSAGE", errorMessage);
					Debug.logError(errorMessage, MODULE);
					return "error";
				} else {
					String successMessage = UtilProperties.getMessage(RES_ERR, "ServiceSuccessMessage",
							UtilHttp.getLocale(request));
					ServiceUtil.getMessages(request, updateUserExamMappingInfoResult, successMessage);
					request.setAttribute("EVENT_MESSAGE", successMessage);
					return "success";
				}
			} catch (GenericServiceException e) {
				String errMsg = "Error setting exam info: " + e.toString();
				request.setAttribute("ERROR_MESSAGE", errMsg);
				return "error";
			}

		} catch (Exception e) {
			Debug.logError(e, MODULE);
			request.setAttribute("_ERROR_MESSAGE", e);
			return "error";
		}
	}
}