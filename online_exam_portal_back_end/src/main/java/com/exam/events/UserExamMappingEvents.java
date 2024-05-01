package com.exam.events;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

import com.exam.util.ConstantValues;

public class UserExamMappingEvents {
	private static final String MODULE = UserExamMappingEvents.class.getName();
	private static final String RES_ERR = "OnlineexamUiLabels";

	public static String createUserExamMappingEvent(HttpServletRequest request, HttpServletResponse response) {

		Locale locale = UtilHttp.getLocale(request);

		Delegator delegator = (Delegator) request.getAttribute("delegator");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

		String partyId = (String) request.getAttribute(ConstantValues.USEREXAM_PARTY_ID);
		String examId = (String) request.getAttribute(ConstantValues.USEREXAM_EXAM_ID);
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

		Map<String, Object> userexamInfo = UtilMisc.toMap(ConstantValues.USEREXAM_PARTY_ID, partyId, ConstantValues.USEREXAM_EXAM_ID, examId, ConstantValues.USEREXAM_ALLOWED_ATTEMPTS,
				allowedAttempts, ConstantValues.USEREXAM_NO_OF_ATTEMPTS, noOfAttempts, ConstantValues.USEREXAM_LAST_DATE, lastPerformanceDateParsed,
				ConstantValues.USEREXAM_TIMEOUT_DAYS, timeoutDays, ConstantValues.USEREXAM_PASSWORD_CHANGE, passwordChangesAuto, ConstantValues.USEREXAM_SPLIT, canSplitExams,
				ConstantValues.USEREXAM_SEE_RESULT, canSeeDetailedResults, ConstantValues.USEREXAM_MAX_SPLIT, maxSplitAttempts);

		try {
			Debug.logInfo(
					"=======Creating UserExamMapping record in event using service CreateUserExamMapping=========",
					MODULE);
			boolean hasFormErrors = false;

			if (!hasFormErrors) {
				try {
					Map<String, ? extends Object> UserExamMappingInfoResult = dispatcher
							.runSync("CreateUserExamMapping", userexamInfo);
					ServiceUtil.getMessages(request, UserExamMappingInfoResult, null);
					if (ServiceUtil.isError(UserExamMappingInfoResult)) {
						String errorMessage = ServiceUtil.getErrorMessage(UserExamMappingInfoResult);
						request.setAttribute("ERROR_MESSAGE", errorMessage);
						Debug.logError(errorMessage, MODULE);
						return "error";
					} else {
						String successMessage = "Create UserExamMapping Service executed successfully.";
						ServiceUtil.getMessages(request, UserExamMappingInfoResult, successMessage);
						request.setAttribute("EVENT_MESSAGE", successMessage);
						return "success";
					}
				} catch (GenericServiceException e) {
					String errMsg = "Error setting userexam info: " + e.toString();
					request.setAttribute("ERROR_MESSAGE", errMsg);
					return "error";
				}
			} else {
				request.setAttribute("_ERROR_MESSAGE", "unable to create");
				return "error";
			}
		} catch (Exception e) {
			Debug.logError(e, MODULE);
			request.setAttribute("_ERROR_MESSAGE", e);
			return "error";
		}
	}

	public static String fetchUserExamMappingEvent(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		List<Map<String, Object>> UserExamMappingdata = new ArrayList<Map<String, Object>>();
		try {
			List<GenericValue> listOfUserExamMappingData = EntityQuery.use(delegator).from("UserExamMapping")
					.queryList();
			if (UtilValidate.isNotEmpty(listOfUserExamMappingData)) {
				for (GenericValue list : listOfUserExamMappingData) {
					Map<String, Object> listOfUserExamMappingEntity = new HashMap<String, Object>();
					listOfUserExamMappingEntity.put(ConstantValues.USEREXAM_PARTY_ID,
							list.get(ConstantValues.USEREXAM_PARTY_ID));
					listOfUserExamMappingEntity.put(ConstantValues.USEREXAM_EXAM_ID,
							list.get(ConstantValues.USEREXAM_EXAM_ID));
					listOfUserExamMappingEntity.put(ConstantValues.USEREXAM_ALLOWED_ATTEMPTS,
							list.get(ConstantValues.USEREXAM_ALLOWED_ATTEMPTS));
					listOfUserExamMappingEntity.put(ConstantValues.USEREXAM_NO_OF_ATTEMPTS,
							list.get(ConstantValues.USEREXAM_NO_OF_ATTEMPTS));
					listOfUserExamMappingEntity.put(ConstantValues.USEREXAM_LAST_DATE,
							list.get(ConstantValues.USEREXAM_LAST_DATE));
					listOfUserExamMappingEntity.put(ConstantValues.USEREXAM_TIMEOUT_DAYS,
							list.get(ConstantValues.USEREXAM_TIMEOUT_DAYS));
					listOfUserExamMappingEntity.put(ConstantValues.USEREXAM_PASSWORD_CHANGE,
							list.get(ConstantValues.USEREXAM_PASSWORD_CHANGE));
					listOfUserExamMappingEntity.put(ConstantValues.USEREXAM_SPLIT,
							list.get(ConstantValues.USEREXAM_SPLIT));
					listOfUserExamMappingEntity.put(ConstantValues.USEREXAM_SEE_RESULT,
							list.get(ConstantValues.USEREXAM_SEE_RESULT));
					listOfUserExamMappingEntity.put(ConstantValues.USEREXAM_MAX_SPLIT,
							list.get(ConstantValues.USEREXAM_MAX_SPLIT));
					UserExamMappingdata.add(listOfUserExamMappingEntity);
				}
				request.setAttribute("UserExamMapping", UserExamMappingdata);
				return "success";
			} else {
				String errorMessage = "No matched fields in UserExamMapping Entity";
				request.setAttribute("ERROR_MESSAGE", errorMessage);
				Debug.logError(errorMessage, MODULE);
				return "error";
			}
		} catch (GenericEntityException e) {
			request.setAttribute("Error", e);
			return "error";
		}
	}

	public static String updateUserExamMappingEvent(HttpServletRequest request, HttpServletResponse response) {

		Locale locale = UtilHttp.getLocale(request);

		Delegator delegator = (Delegator) request.getAttribute("delegator");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

		String partyId = (String) request.getAttribute(ConstantValues.USEREXAM_PARTY_ID);
		String examId = (String) request.getAttribute(ConstantValues.USEREXAM_EXAM_ID);
		String allowedAttempts = (String) request.getAttribute(ConstantValues.USEREXAM_ALLOWED_ATTEMPTS);
		String noOfAttempts = (String) request.getAttribute(ConstantValues.USEREXAM_NO_OF_ATTEMPTS);
		String lastPerformanceDate = (String) request.getAttribute(ConstantValues.USEREXAM_LAST_DATE);
		String timeoutDays = (String) request.getAttribute(ConstantValues.USEREXAM_TIMEOUT_DAYS);
		String passwordChangesAuto = (String) request.getAttribute(ConstantValues.USEREXAM_PASSWORD_CHANGE);
		String canSplitExams = (String) request.getAttribute(ConstantValues.USEREXAM_SPLIT);
		String canSeeDetailedResults = (String) request.getAttribute(ConstantValues.USEREXAM_SEE_RESULT);
		String maxSplitAttempts = (String) request.getAttribute(ConstantValues.USEREXAM_MAX_SPLIT);

		Map<String, Object> userexamInfo = UtilMisc.toMap(ConstantValues.USEREXAM_PARTY_ID, partyId, ConstantValues.USEREXAM_EXAM_ID, examId, ConstantValues.USEREXAM_ALLOWED_ATTEMPTS,
				allowedAttempts, ConstantValues.USEREXAM_NO_OF_ATTEMPTS, noOfAttempts, ConstantValues.USEREXAM_LAST_DATE, lastPerformanceDate,
				ConstantValues.USEREXAM_TIMEOUT_DAYS, timeoutDays, ConstantValues.USEREXAM_PASSWORD_CHANGE, passwordChangesAuto, ConstantValues.USEREXAM_SPLIT, canSplitExams,
				ConstantValues.USEREXAM_SEE_RESULT, canSeeDetailedResults, ConstantValues.USEREXAM_MAX_SPLIT, maxSplitAttempts);
		try {
			Debug.logInfo(
					"=======Updating UserExamMapping record in event using service UpdateUserExamMapping=========",
					MODULE);
			boolean hasFormErrors = false;
			if (!hasFormErrors) {
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
						String successMessage = "Update UserExamMapping Service executed successfully.";
						ServiceUtil.getMessages(request, updateUserExamMappingInfoResult, successMessage);
						request.setAttribute("EVENT_MESSAGE", successMessage);
						return "success";
					}
				} catch (GenericServiceException e) {
					String errMsg = "Error setting exam info: " + e.toString();
					request.setAttribute("ERROR_MESSAGE", errMsg);
					return "error";
				}
			} else {
				request.setAttribute("_ERROR_MESSAGE", "unable to create");
				return "error";
			}
		} catch (Exception e) {
			Debug.logError(e, MODULE);
			request.setAttribute("_ERROR_MESSAGE", e);
			return "error";
		}
	}

	public static String deleteUserExamMappingEvent(HttpServletRequest request, HttpServletResponse response) {
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		String partyId = (String) request.getAttribute(ConstantValues.USEREXAM_PARTY_ID);
		String examId=(String) request.getAttribute(ConstantValues.USEREXAM_EXAM_ID);
		Map<String, Object> userExamInfo = UtilMisc.toMap(ConstantValues.USEREXAM_PARTY_ID, partyId,ConstantValues.USEREXAM_EXAM_ID,examId);
		try {
			Debug.logInfo("=======Deleting userExamMapping record in event using service DeleteTopicMaster=========",
					MODULE);
			try {
				Map<String, ? extends Object> deleteUserExamMappingInfoResult = dispatcher.runSync("DeleteUserExamMapping",
						userExamInfo);
				if (UtilValidate.isNotEmpty(userExamInfo)) {
					String successMessage = "delete userExamMapping Service executed successfully.";
					ServiceUtil.getMessages(request, deleteUserExamMappingInfoResult, successMessage);
					request.setAttribute("_EVENT_MESSAGE_", successMessage);
					Debug.logError(successMessage, MODULE);
					return "success";
				} else {
					String errorMessage = ServiceUtil.getErrorMessage(deleteUserExamMappingInfoResult);
					request.setAttribute("_ERROR_MESSAGE_", errorMessage);
					Debug.logError(errorMessage, MODULE);
					return "error";
				}
			} catch (GenericServiceException e) {
				String errMsg = "Error deleting userExamMapping info: " + e.toString();
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