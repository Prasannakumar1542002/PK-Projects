package com.exam.events;

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
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import com.exam.forms.HibernateValidationMaster;
import com.exam.forms.checks.QuestionMasterCheck;
import com.exam.helper.HibernateHelper;
import com.exam.util.ConstantValues;

public class QuestionMasterEvents {
	public static final String MODULE = QuestionMasterEvents.class.getName();
	private static final String RES_ERR = "OnlineExamPortalUiLabels";

	public static String createQuestionMasterEvent(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		Locale locale = UtilHttp.getLocale(request);

		String questionDetail = (String) request.getAttribute(ConstantValues.QUES_DETAIL);
		String optionA = (String) request.getAttribute(ConstantValues.QUES_OPTION_A);
		String optionB = (String) request.getAttribute(ConstantValues.QUES_OPTION_B);
		String optionC = (String) request.getAttribute(ConstantValues.QUES_OPTION_C);
		String optionD = (String) request.getAttribute(ConstantValues.QUES_OPTION_D);
		String optionE = (String) request.getAttribute(ConstantValues.QUES_OPTION_E);
		String answer = (String) request.getAttribute(ConstantValues.QUES_ANSWER);
		String numAnswers = (String) request.getAttribute(ConstantValues.QUES_NUM_ANS);
		String questionType = (String) request.getAttribute(ConstantValues.QUES_TYPE);
		String difficultyLevel = (String) request.getAttribute(ConstantValues.QUES_DIFFICULTY_LEVEL);
		String answerValue = (String) request.getAttribute(ConstantValues.QUES_ANS_VALUE);
		String topicId = (String) request.getAttribute(ConstantValues.QUES_TOPIC_ID);
		String negativeMarkValue = (String) request.getAttribute(ConstantValues.QUES_NEG_MARK);

		Map<String, Object> questionInfo = UtilMisc.toMap(ConstantValues.QUES_DETAIL, questionDetail,
				ConstantValues.QUES_OPTION_A, optionA, ConstantValues.QUES_OPTION_B, optionB,
				ConstantValues.QUES_OPTION_C, optionC, ConstantValues.QUES_OPTION_D, optionD,
				ConstantValues.QUES_OPTION_E, optionE, ConstantValues.QUES_ANSWER, answer, ConstantValues.QUES_NUM_ANS,
				numAnswers, ConstantValues.QUES_TYPE, questionType, ConstantValues.QUES_DIFFICULTY_LEVEL,
				difficultyLevel, ConstantValues.QUES_ANS_VALUE, answerValue, ConstantValues.TOPIC_ID, topicId,
				ConstantValues.QUES_NEG_MARK, negativeMarkValue);

		try {
			Debug.logInfo(
					"=======Creating QuestionMaster record in event using service CreateQuestionMasterService=========",
					MODULE);
			HibernateValidationMaster hibernate = HibernateHelper.populateBeanFromMap(questionInfo,
					HibernateValidationMaster.class);

			Set<ConstraintViolation<HibernateValidationMaster>> errors = HibernateHelper
					.checkValidationErrors(hibernate, QuestionMasterCheck.class);
			boolean hasFormErrors = HibernateHelper.validateFormSubmission(delegator, errors, request, locale,
					"Mandatory Err QuestionMaster Entity", RES_ERR, false);

			if (!hasFormErrors) {
				try {
					Map<String, Object> questionInfo2 = UtilMisc.toMap(ConstantValues.QUES_DETAIL, questionDetail,
							ConstantValues.QUES_OPTION_A, optionA, ConstantValues.QUES_OPTION_B, optionB,
							ConstantValues.QUES_OPTION_C, optionC, ConstantValues.QUES_OPTION_D, optionD,
							ConstantValues.QUES_OPTION_E, optionE, ConstantValues.QUES_ANSWER, answer,
							ConstantValues.QUES_NUM_ANS, numAnswers, "QuestionType", questionType,
							ConstantValues.QUES_DIFFICULTY_LEVEL, difficultyLevel, ConstantValues.QUES_ANS_VALUE,
							answerValue, ConstantValues.TOPIC_ID, topicId, ConstantValues.QUES_NEG_MARK,
							negativeMarkValue);
					Map<String, ? extends Object> createQuestionMasterInfoResult = dispatcher
							.runSync("CreateQuestionMaster", questionInfo2);
					ServiceUtil.getMessages(request, createQuestionMasterInfoResult, null);
					if (ServiceUtil.isError(createQuestionMasterInfoResult)) {
						String errorMessage = ServiceUtil.getErrorMessage(createQuestionMasterInfoResult);
						request.setAttribute("_ERROR_MESSAGE_", errorMessage);
						Debug.logError(errorMessage, MODULE);
						return "error";
					} else {
						String successMessage = "Create QuestionMaster Service executed successfully.";
						ServiceUtil.getMessages(request, createQuestionMasterInfoResult, successMessage);
						request.setAttribute("_EVENT_MESSAGE_", successMessage);
						return "success";
					}
				} catch (GenericServiceException e) {
					String errMsg = "Error setting question info: " + e.toString();
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

	public static String fetchQuestionMasterEvent(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		List<Map<String, Object>> questionMasterdata = new ArrayList<Map<String, Object>>();
		try {
			List<GenericValue> listOfQuestionMasterData = EntityQuery.use(delegator).from("QuestionMaster").queryList();
			if (UtilValidate.isNotEmpty(listOfQuestionMasterData)) {
				for (GenericValue list : listOfQuestionMasterData) {
					Map<String, Object> listOfQuestionMasterEntity = new HashMap<String, Object>();
					listOfQuestionMasterEntity.put(ConstantValues.QUES_ID, list.get(ConstantValues.QUES_ID));
					listOfQuestionMasterEntity.put(ConstantValues.QUES_DETAIL, list.get(ConstantValues.QUES_DETAIL));
					listOfQuestionMasterEntity.put(ConstantValues.QUES_OPTION_A,
							list.get(ConstantValues.QUES_OPTION_A));
					listOfQuestionMasterEntity.put(ConstantValues.QUES_OPTION_B,
							list.get(ConstantValues.QUES_OPTION_B));
					listOfQuestionMasterEntity.put(ConstantValues.QUES_OPTION_C,
							list.get(ConstantValues.QUES_OPTION_C));
					listOfQuestionMasterEntity.put(ConstantValues.QUES_OPTION_D,
							list.get(ConstantValues.QUES_OPTION_D));
					listOfQuestionMasterEntity.put(ConstantValues.QUES_OPTION_E,
							list.get(ConstantValues.QUES_OPTION_E));
					listOfQuestionMasterEntity.put(ConstantValues.QUES_ANSWER, list.get(ConstantValues.QUES_ANSWER));
					listOfQuestionMasterEntity.put(ConstantValues.QUES_NUM_ANS, list.get(ConstantValues.QUES_NUM_ANS));
					listOfQuestionMasterEntity.put(ConstantValues.QUES_TYPE, list.get("QuestionType"));
					listOfQuestionMasterEntity.put(ConstantValues.QUES_DIFFICULTY_LEVEL,
							list.get(ConstantValues.QUES_DIFFICULTY_LEVEL));
					listOfQuestionMasterEntity.put(ConstantValues.QUES_ANS_VALUE,
							list.get(ConstantValues.QUES_ANS_VALUE));
					listOfQuestionMasterEntity.put(ConstantValues.QUES_TOPIC_ID,
							list.get(ConstantValues.QUES_TOPIC_ID));
					listOfQuestionMasterEntity.put(ConstantValues.QUES_NEG_MARK,
							list.get(ConstantValues.QUES_NEG_MARK));
					questionMasterdata.add(listOfQuestionMasterEntity);
				}
				request.setAttribute("QuestionMaster", questionMasterdata);
				return "success";
			} else {
				String errorMessage = "No matched fields in QuestionMaster Entity";
				request.setAttribute("_ERROR_MESSAGE_", errorMessage);
				Debug.logError(errorMessage, MODULE);
				return "error";
			}
		} catch (GenericEntityException e) {
			request.setAttribute("Error", e);
			return "error";
		}
	}

	public static String updateQuestionMasterEvent(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		Locale locale = UtilHttp.getLocale(request);

		String questionId = (String) request.getAttribute(ConstantValues.QUES_ID);
		String questionDetail = (String) request.getAttribute(ConstantValues.QUES_DETAIL);
		String optionA = (String) request.getAttribute(ConstantValues.QUES_OPTION_A);
		String optionB = (String) request.getAttribute(ConstantValues.QUES_OPTION_B);
		String optionC = (String) request.getAttribute(ConstantValues.QUES_OPTION_C);
		String optionD = (String) request.getAttribute(ConstantValues.QUES_OPTION_D);
		String optionE = (String) request.getAttribute(ConstantValues.QUES_OPTION_E);
		String answer = (String) request.getAttribute(ConstantValues.QUES_ANSWER);
		String numAnswers = (String) request.getAttribute(ConstantValues.QUES_NUM_ANS);
		String questionType = (String) request.getAttribute(ConstantValues.QUES_TYPE);
		String difficultyLevel = (String) request.getAttribute(ConstantValues.QUES_DIFFICULTY_LEVEL);
		String answerValue = (String) request.getAttribute(ConstantValues.QUES_ANS_VALUE);
		String topicId = (String) request.getAttribute(ConstantValues.QUES_TOPIC_ID);
		String negativeMarkValue = (String) request.getAttribute(ConstantValues.QUES_NEG_MARK);

		Map<String, Object> questionInfo = UtilMisc.toMap(ConstantValues.QUES_ID,questionId,ConstantValues.QUES_DETAIL, questionDetail,
				ConstantValues.QUES_OPTION_A, optionA, ConstantValues.QUES_OPTION_B, optionB,
				ConstantValues.QUES_OPTION_C, optionC, ConstantValues.QUES_OPTION_D, optionD,
				ConstantValues.QUES_OPTION_E, optionE, ConstantValues.QUES_ANSWER, answer, ConstantValues.QUES_NUM_ANS,
				numAnswers, ConstantValues.QUES_TYPE, questionType, ConstantValues.QUES_DIFFICULTY_LEVEL,
				difficultyLevel, ConstantValues.QUES_ANS_VALUE, answerValue, ConstantValues.TOPIC_ID, topicId,
				ConstantValues.QUES_NEG_MARK, negativeMarkValue);

		try {
			Debug.logInfo("=======Updating QuestionMaster record in event using service UpdateQuestionMaster=========",
					MODULE);
			HibernateValidationMaster hibernate = HibernateHelper.populateBeanFromMap(questionInfo,
					HibernateValidationMaster.class);

			Set<ConstraintViolation<HibernateValidationMaster>> errors = HibernateHelper
					.checkValidationErrors(hibernate, QuestionMasterCheck.class);
			boolean hasFormErrors = HibernateHelper.validateFormSubmission(delegator, errors, request, locale,
					"Mandatory Err QuestionMaster Entity", RES_ERR, false);

			if (!hasFormErrors) {
				try {
					Map<String, Object> questionInfo2 = UtilMisc.toMap(ConstantValues.QUES_DETAIL, questionDetail,
							ConstantValues.QUES_OPTION_A, optionA, ConstantValues.QUES_OPTION_B, optionB,
							ConstantValues.QUES_OPTION_C, optionC, ConstantValues.QUES_OPTION_D, optionD,
							ConstantValues.QUES_OPTION_E, optionE, ConstantValues.QUES_ANSWER, answer,
							ConstantValues.QUES_NUM_ANS, numAnswers, "QuestionType", questionType,
							ConstantValues.QUES_DIFFICULTY_LEVEL, difficultyLevel, ConstantValues.QUES_ANS_VALUE,
							answerValue, ConstantValues.TOPIC_ID, topicId, ConstantValues.QUES_NEG_MARK,
							negativeMarkValue);
					Map<String, ? extends Object> updateQuestionMasterInfoResult = dispatcher
							.runSync("UpdateQuestionMaster", questionInfo2);
					ServiceUtil.getMessages(request, updateQuestionMasterInfoResult, null);
					if (ServiceUtil.isError(updateQuestionMasterInfoResult)) {
						String errorMessage = ServiceUtil.getErrorMessage(updateQuestionMasterInfoResult);
						request.setAttribute("_ERROR_MESSAGE_", errorMessage);
						Debug.logError(errorMessage, MODULE);
						return "error";
					} else {
						String successMessage = "Update QuestionMaster Service executed successfully.";
						ServiceUtil.getMessages(request, updateQuestionMasterInfoResult, successMessage);
						request.setAttribute("_EVENT_MESSAGE_", successMessage);
						return "success";
					}
				} catch (GenericServiceException e) {
					String errMsg = "Error setting question info: " + e.toString();
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

	public static String deleteQuestionMasterEvent(HttpServletRequest request, HttpServletResponse response) {
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		String questionId = (String) request.getAttribute(ConstantValues.QUES_ID);
		Map<String, Object> questionInfo = UtilMisc.toMap(ConstantValues.QUES_ID, questionId);
		try {
			Debug.logInfo("=======Deleting QuestionMaster record in event using service DeleteQuestionMaster=======",
					MODULE);
			try {
				Map<String, ? extends Object> deleteQuestionMasterInfoResult = dispatcher
						.runSync("DeleteQuestionMaster", questionInfo);
				if (UtilValidate.isNotEmpty(questionInfo)) {
					String successMessage = "delete QuestionMaster Service executed successfully.";
					ServiceUtil.getMessages(request, deleteQuestionMasterInfoResult, successMessage);
					request.setAttribute("_EVENT_MESSAGE_", successMessage);
					return "success";
				} else {
					String errorMessage = ServiceUtil.getErrorMessage(deleteQuestionMasterInfoResult);
					request.setAttribute("_ERROR_MESSAGE_", errorMessage);
					return "error";
				}

			} catch (GenericServiceException e) {
				String errMsg = "Error deleting question info: " + e.toString();
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
