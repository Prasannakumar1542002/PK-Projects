package com.exam.events;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import com.exam.forms.checks.ExamMasterCheck;
import com.exam.helper.HibernateHelper;
import com.exam.util.ConstantValues;
import com.exam.util.EntityConstants;

public class ExamMasterEvents {
	private static final String MODULE = ExamMasterEvents.class.getName();
	private static final String RES_ERR = "OnlineExamPortalUiLabels";

	// Method to insert data into ExamMaster Entity
	public static String createExam(HttpServletRequest request, HttpServletResponse response) {
		Locale locale = UtilHttp.getLocale(request);

		Delegator delegator = (Delegator) request.getAttribute(EntityConstants.DELEGATOR);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(EntityConstants.DISPATCHER);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(EntityConstants.USER_LOGIN);

		String examName = (String) request.getAttribute(ConstantValues.EXAM_NAME);
		String description = (String) request.getAttribute(ConstantValues.EXAM_DESCRIPTION);
		String creationDate = (String) request.getAttribute(ConstantValues.EXAM_CREATION_DATE);
		String expirationDate = (String) request.getAttribute(ConstantValues.EXAM_EXPIRATION_DATE);
		String noOfQuestions = (String) request.getAttribute(ConstantValues.EXAM_TOTAL_QUES);
		String durationMinutes = (String) request.getAttribute(ConstantValues.EXAM_DURATION);
		String passPercentage = (String) request.getAttribute(ConstantValues.EXAM_PASS_PERCENTAGE);
		String questionsRandomized = (String) request.getAttribute(ConstantValues.EXAM_QUES_RANDOMIZED);
		String answersMust = (String) request.getAttribute(ConstantValues.EXAM_ANS_MUST);
		String enableNegativeMark = (String) request.getAttribute(ConstantValues.EXAM_ENABLE_NEG_MARK);
		String negativeMarkValue = (String) request.getAttribute(ConstantValues.EXAM_NEG_MARK);

		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		LocalDateTime creationLocalDateTime = LocalDateTime.parse(creationDate, inputFormatter);
		String creationDateFormatted = creationLocalDateTime.format(outputFormatter);

		LocalDateTime expirationLocalDateTime = LocalDateTime.parse(expirationDate, inputFormatter);
		String expirationDateFormatted = expirationLocalDateTime.format(outputFormatter);

		Map<String, Object> examInfo = UtilMisc.toMap(ConstantValues.EXAM_NAME, examName,
				ConstantValues.EXAM_DESCRIPTION, description, ConstantValues.EXAM_CREATION_DATE, creationDateFormatted,
				ConstantValues.EXAM_EXPIRATION_DATE, expirationDateFormatted, ConstantValues.EXAM_TOTAL_QUES,
				noOfQuestions, ConstantValues.EXAM_DURATION, durationMinutes, ConstantValues.EXAM_PASS_PERCENTAGE,
				passPercentage, ConstantValues.EXAM_QUES_RANDOMIZED, questionsRandomized, ConstantValues.EXAM_ANS_MUST,
				answersMust, ConstantValues.EXAM_ENABLE_NEG_MARK, enableNegativeMark, ConstantValues.EXAM_NEG_MARK,
				negativeMarkValue, EntityConstants.USER_LOGIN, userLogin);

		try {
			Debug.logInfo("=======Creating ExamMaster record in event using service CreateExamMaster=========", MODULE);
			HibernateValidationMaster hibernate = HibernateHelper.populateBeanFromMap(examInfo,
					HibernateValidationMaster.class);

			Set<ConstraintViolation<HibernateValidationMaster>> errors = HibernateHelper
					.checkValidationErrors(hibernate, ExamMasterCheck.class);
			boolean hasFormErrors = HibernateHelper.validateFormSubmission(delegator, errors, request, locale,
					"Mandatory Err ExamMaster Entity", RES_ERR, false);

			if (hasFormErrors) {
				request.setAttribute("_ERROR_MESSAGE", errors);
				return "error";
			}
			try {
				// Calling Entity-Auto service to insert data into ExamMaster Entity
				Map<String, ? extends Object> createExamMasterInfoResult = dispatcher.runSync("CreateExamMaster",
						examInfo);
				ServiceUtil.getMessages(request, createExamMasterInfoResult, null);
				if (ServiceUtil.isError(createExamMasterInfoResult)) {
					String errorMessage = ServiceUtil.getErrorMessage(createExamMasterInfoResult);
					request.setAttribute("_ERROR_MESSAGE_", errorMessage);
					Debug.logError(errorMessage, MODULE);
					return "error";
				}
				String successMessage = UtilProperties.getMessage(RES_ERR, "ServiceSuccessMessage",
						UtilHttp.getLocale(request));
				ServiceUtil.getMessages(request, createExamMasterInfoResult, successMessage);
				request.setAttribute("_EVENT_MESSAGE_", successMessage);
				return "success";

			} catch (GenericServiceException e) {
				String errorMessage = UtilProperties.getMessage(RES_ERR, "ServiceCallingError",
						UtilHttp.getLocale(request)) + e.toString();// "Error setting exam info: " + e.toString();
				request.setAttribute("_ERROR_MESSAGE_", errorMessage);
				return "error";
			}

		} catch (Exception e) {
			Debug.logError(e, MODULE);
			request.setAttribute("_ERROR_MESSAGE", e);
			return "error";
		}

	}

	// Method to retrieve data's from ExamMaster Entity
	public static String fetchAllExams(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute(EntityConstants.DELEGATOR);
		List<Map<String, Object>> viewExamList = new ArrayList<Map<String, Object>>();
		try {
			// Query to retrieve data's from ExamMaster
			List<GenericValue> listOfExams = EntityQuery.use(delegator).from("ExamMaster").queryList();
			if (UtilValidate.isNotEmpty(listOfExams)) {
				for (GenericValue exam : listOfExams) {
					Map<String, Object> examList = new HashMap<String, Object>();
					examList.put(ConstantValues.EXAM_ID, exam.get(ConstantValues.EXAM_ID));
					examList.put(ConstantValues.EXAM_NAME, exam.get(ConstantValues.EXAM_NAME));
					examList.put(ConstantValues.EXAM_DESCRIPTION, exam.get(ConstantValues.EXAM_DESCRIPTION));
					examList.put(ConstantValues.EXAM_CREATION_DATE, exam.get(ConstantValues.EXAM_CREATION_DATE));
					examList.put(ConstantValues.EXAM_EXPIRATION_DATE, exam.get(ConstantValues.EXAM_EXPIRATION_DATE));
					examList.put(ConstantValues.EXAM_TOTAL_QUES, exam.get(ConstantValues.EXAM_TOTAL_QUES));
					examList.put(ConstantValues.EXAM_DURATION, exam.get(ConstantValues.EXAM_DURATION));
					examList.put(ConstantValues.EXAM_PASS_PERCENTAGE, exam.get(ConstantValues.EXAM_PASS_PERCENTAGE));
					examList.put(ConstantValues.EXAM_QUES_RANDOMIZED, exam.get(ConstantValues.EXAM_QUES_RANDOMIZED));
					examList.put(ConstantValues.EXAM_ANS_MUST, exam.get(ConstantValues.EXAM_ANS_MUST));
					examList.put(ConstantValues.EXAM_ENABLE_NEG_MARK, exam.get(ConstantValues.EXAM_ENABLE_NEG_MARK));
					examList.put(ConstantValues.EXAM_NEG_MARK, exam.get(ConstantValues.EXAM_NEG_MARK));
					viewExamList.add(examList);
				}
				Map<String, Object> examsInfo = new HashMap<>();
				examsInfo.put("ExamList", viewExamList);
				request.setAttribute("ExamInfo", examsInfo);
				return "success";
			}
			String errorMessage = UtilProperties.getMessage(RES_ERR, "ErrorInFetchingData",
					UtilHttp.getLocale(request));// "No matched fields in ExamMaster Entity";
			request.setAttribute("_ERROR_MESSAGE_", errorMessage);
			Debug.logError(errorMessage, MODULE);
			return "error";

		} catch (GenericEntityException e) {
			request.setAttribute("Error", e);
			return "error";
		}
	}

	// Method to Update data's into ExamMaster Entity
	public static String updateExam(HttpServletRequest request, HttpServletResponse response) {

		Locale locale = UtilHttp.getLocale(request);

		Delegator delegator = (Delegator) request.getAttribute(EntityConstants.DELEGATOR);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(EntityConstants.DISPATCHER);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(EntityConstants.USER_LOGIN);

		String examId = (String) request.getAttribute(ConstantValues.EXAM_ID);
		String examName = (String) request.getAttribute(ConstantValues.EXAM_NAME);
		String description = (String) request.getAttribute(ConstantValues.EXAM_DESCRIPTION);
		String creationDate = (String) request.getAttribute(ConstantValues.EXAM_CREATION_DATE);
		String expirationDate = (String) request.getAttribute(ConstantValues.EXAM_EXPIRATION_DATE);
		String noOfQuestions = (String) request.getAttribute(ConstantValues.EXAM_TOTAL_QUES);
		String durationMinutes = (String) request.getAttribute(ConstantValues.EXAM_DURATION);
		String passPercentage = (String) request.getAttribute(ConstantValues.EXAM_PASS_PERCENTAGE);
		String questionsRandomized = (String) request.getAttribute(ConstantValues.EXAM_QUES_RANDOMIZED);
		String answersMust = (String) request.getAttribute(ConstantValues.EXAM_ANS_MUST);
		String enableNegativeMark = (String) request.getAttribute(ConstantValues.EXAM_ENABLE_NEG_MARK);
		String negativeMarkValue = (String) request.getAttribute(ConstantValues.EXAM_NEG_MARK);

		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		String creationDateString = (String) request.getAttribute(ConstantValues.EXAM_CREATION_DATE);
		LocalDateTime creationLocalDateTime = LocalDateTime.parse(creationDateString, inputFormatter);
		String creationDateFormatted = creationLocalDateTime.format(outputFormatter);

		String expirationDateString = (String) request.getAttribute(ConstantValues.EXAM_EXPIRATION_DATE);
		LocalDateTime expirationLocalDateTime = LocalDateTime.parse(expirationDateString, inputFormatter);
		String expirationDateFormatted = expirationLocalDateTime.format(outputFormatter);

		Map<String, Object> examInfo = UtilMisc.toMap(ConstantValues.EXAM_ID, examId, ConstantValues.EXAM_NAME,
				examName, ConstantValues.EXAM_DESCRIPTION, description, ConstantValues.EXAM_CREATION_DATE,
				creationDateFormatted, ConstantValues.EXAM_EXPIRATION_DATE, expirationDateFormatted,
				ConstantValues.EXAM_TOTAL_QUES, noOfQuestions, ConstantValues.EXAM_DURATION, durationMinutes,
				ConstantValues.EXAM_PASS_PERCENTAGE, passPercentage, ConstantValues.EXAM_QUES_RANDOMIZED,
				questionsRandomized, ConstantValues.EXAM_ANS_MUST, answersMust, ConstantValues.EXAM_ENABLE_NEG_MARK,
				enableNegativeMark, ConstantValues.EXAM_NEG_MARK, negativeMarkValue, EntityConstants.USER_LOGIN,
				userLogin);

		try {
			Debug.logInfo("=======Updating ExamMaster record in event using service UpdateExamMaster=========", MODULE);
			HibernateValidationMaster hibernate = HibernateHelper.populateBeanFromMap(examInfo,
					HibernateValidationMaster.class);

			Set<ConstraintViolation<HibernateValidationMaster>> errors = HibernateHelper
					.checkValidationErrors(hibernate, ExamMasterCheck.class);
			boolean hasFormErrors = HibernateHelper.validateFormSubmission(delegator, errors, request, locale,
					"Mandatory Fields missing in ExamMaster Entity", RES_ERR, false);

			if (hasFormErrors) {
				request.setAttribute("_ERROR_MESSAGE", errors);
				return "error";

			}
			try {
				// Calling Entity-Auto Service to Update data into ExamMaster Entity
				Map<String, ? extends Object> updateExamMasterInfoResult = dispatcher.runSync("UpdateExamMaster",
						examInfo);
				ServiceUtil.getMessages(request, updateExamMasterInfoResult, null);
				if (ServiceUtil.isError(updateExamMasterInfoResult)) {
					String errorMessage = ServiceUtil.getErrorMessage(updateExamMasterInfoResult);
					request.setAttribute("_ERROR_MESSAGE_", errorMessage);
					Debug.logError(errorMessage, MODULE);
					return "error";
				} else {
					String successMessage = UtilProperties.getMessage(RES_ERR, "ServiceSuccessMessage",
							UtilHttp.getLocale(request));
					ServiceUtil.getMessages(request, updateExamMasterInfoResult, successMessage);
					request.setAttribute("_EVENT_MESSAGE_", successMessage);
					return "success";
				}
			} catch (GenericServiceException e) {
				String errMsg = UtilProperties.getMessage(RES_ERR, "ServiceCallingError", UtilHttp.getLocale(request))
						+ e.toString();
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
