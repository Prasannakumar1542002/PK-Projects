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
import com.exam.forms.checks.QuestionMasterCheck;
import com.exam.helper.HibernateHelper;
import com.exam.util.ConstantValues;
import com.exam.util.EntityConstants;

public class QuestionMasterEvents {
	public static final String MODULE = QuestionMasterEvents.class.getName();
	private static final String RES_ERR = "OnlineExamPortalUiLabels";

	public static String createQuestion(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute(EntityConstants.DELEGATOR);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(EntityConstants.DISPATCHER);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(EntityConstants.USER_LOGIN);
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
				ConstantValues.QUES_NEG_MARK, negativeMarkValue, EntityConstants.USER_LOGIN, userLogin);

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

			if (hasFormErrors) {
				request.setAttribute("_ERROR_MESSAGE", errors);
				return "error";
			}
				try {
					Map<String, ? extends Object> createQuestionMasterInfoResult = dispatcher
							.runSync("CreateQuestionMaster", questionInfo);
					ServiceUtil.getMessages(request, createQuestionMasterInfoResult, null);
					if (ServiceUtil.isError(createQuestionMasterInfoResult)) {
						String errorMessage = ServiceUtil.getErrorMessage(createQuestionMasterInfoResult);
						request.setAttribute("_ERROR_MESSAGE_", errorMessage);
						Debug.logError(errorMessage, MODULE);
						return "error";
					} else {
						String successMessage = UtilProperties.getMessage(RES_ERR, "ServiceSuccessMessage",
								UtilHttp.getLocale(request));
						request.setAttribute("_EVENT_MESSAGE_", successMessage);
						return "success";
					}
				} catch (GenericServiceException e) {
					String errorMessage = UtilProperties.getMessage(RES_ERR, "ServiceCallingError",
							UtilHttp.getLocale(request));
					request.setAttribute("_ERROR_MESSAGE_", errorMessage);
					return "error";
				}
		
		} catch (Exception e) {
			Debug.logError(e, MODULE);
			request.setAttribute("_ERROR_MESSAGE", e);
			return "error";
		}
	}

	public static String fetchAllQuestions(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute(EntityConstants.DELEGATOR);
		List<Map<String, Object>> viewQuestionList = new ArrayList<Map<String, Object>>();
		try {
			List<GenericValue> listOfQuestions = EntityQuery.use(delegator).from("QuestionMaster").queryList();
			if (UtilValidate.isNotEmpty(listOfQuestions)) {
				for (GenericValue question : listOfQuestions) {
					Map<String, Object> questionList = new HashMap<String, Object>();
					questionList.put(ConstantValues.QUES_ID, question.get(ConstantValues.QUES_ID));
					questionList.put(ConstantValues.QUES_DETAIL, question.get(ConstantValues.QUES_DETAIL));
					questionList.put(ConstantValues.QUES_OPTION_A,
							question.get(ConstantValues.QUES_OPTION_A));
					questionList.put(ConstantValues.QUES_OPTION_B,
							question.get(ConstantValues.QUES_OPTION_B));
					questionList.put(ConstantValues.QUES_OPTION_C,
							question.get(ConstantValues.QUES_OPTION_C));
					questionList.put(ConstantValues.QUES_OPTION_D,
							question.get(ConstantValues.QUES_OPTION_D));
					questionList.put(ConstantValues.QUES_OPTION_E,
							question.get(ConstantValues.QUES_OPTION_E));
					questionList.put(ConstantValues.QUES_ANSWER, question.get(ConstantValues.QUES_ANSWER));
					questionList.put(ConstantValues.QUES_NUM_ANS, question.get(ConstantValues.QUES_NUM_ANS));
					questionList.put(ConstantValues.QUES_TYPE, question.get(ConstantValues.QUES_TYPE));
					questionList.put(ConstantValues.QUES_DIFFICULTY_LEVEL,
							question.get(ConstantValues.QUES_DIFFICULTY_LEVEL));
					questionList.put(ConstantValues.QUES_ANS_VALUE,
							question.get(ConstantValues.QUES_ANS_VALUE));
					String topicName = EntityQuery.use(delegator).from("TopicMaster").where(ConstantValues.TOPIC_ID, question.get(ConstantValues.TOPIC_ID)).queryOne().getString(ConstantValues.TOPIC_NAME);
					questionList.put(ConstantValues.QUES_TOPIC_ID,
							topicName);
					questionList.put(ConstantValues.QUES_NEG_MARK,
							question.get(ConstantValues.QUES_NEG_MARK));
					viewQuestionList.add(questionList);
				}
				Map<String, Object> questionsInfo = new HashMap<>();
				questionsInfo.put("QuestionList", viewQuestionList);
				request.setAttribute("QuestionInfo", questionsInfo);
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

	public static String updateQuestion(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute(EntityConstants.DELEGATOR);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(EntityConstants.DISPATCHER);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(EntityConstants.USER_LOGIN);
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

		Map<String, Object> questionInfo = UtilMisc.toMap(ConstantValues.QUES_ID, questionId,
				ConstantValues.QUES_DETAIL, questionDetail, ConstantValues.QUES_OPTION_A, optionA,
				ConstantValues.QUES_OPTION_B, optionB, ConstantValues.QUES_OPTION_C, optionC,
				ConstantValues.QUES_OPTION_D, optionD, ConstantValues.QUES_OPTION_E, optionE,
				ConstantValues.QUES_ANSWER, answer, ConstantValues.QUES_NUM_ANS, numAnswers, ConstantValues.QUES_TYPE,
				questionType, ConstantValues.QUES_DIFFICULTY_LEVEL, difficultyLevel, ConstantValues.QUES_ANS_VALUE,
				answerValue, ConstantValues.TOPIC_ID, topicId, ConstantValues.QUES_NEG_MARK, negativeMarkValue, EntityConstants.USER_LOGIN,
				userLogin);

		try {
			Debug.logInfo("=======Updating QuestionMaster record in event using service UpdateQuestionMaster=========",
					MODULE);
			HibernateValidationMaster hibernate = HibernateHelper.populateBeanFromMap(questionInfo,
					HibernateValidationMaster.class);

			Set<ConstraintViolation<HibernateValidationMaster>> errors = HibernateHelper
					.checkValidationErrors(hibernate, QuestionMasterCheck.class);
			boolean hasFormErrors = HibernateHelper.validateFormSubmission(delegator, errors, request, locale,
					"Mandatory Err QuestionMaster Entity", RES_ERR, false);

			if (hasFormErrors) {
				request.setAttribute("_ERROR_MESSAGE", errors);
				return "error";
			}
				try {
					Map<String, ? extends Object> updateQuestionMasterInfoResult = dispatcher
							.runSync("UpdateQuestionMaster", questionInfo);
					ServiceUtil.getMessages(request, updateQuestionMasterInfoResult, null);
					if (ServiceUtil.isError(updateQuestionMasterInfoResult)) {
						String errorMessage = ServiceUtil.getErrorMessage(updateQuestionMasterInfoResult);
						request.setAttribute("_ERROR_MESSAGE_", errorMessage);
						Debug.logError(errorMessage, MODULE);
						return "error";
					} else {
						String successMessage = UtilProperties.getMessage(RES_ERR, "ServiceSuccessMessage",
								UtilHttp.getLocale(request));
						ServiceUtil.getMessages(request, updateQuestionMasterInfoResult, successMessage);
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

	public static String deleteQuestion(HttpServletRequest request, HttpServletResponse response) {
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(EntityConstants.DISPATCHER);
		String questionId = (String) request.getAttribute(ConstantValues.QUES_ID);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(EntityConstants.USER_LOGIN);
		Map<String, Object> questionInfo = UtilMisc.toMap(ConstantValues.QUES_ID, questionId,EntityConstants.USER_LOGIN, userLogin);
		
		try {
			Debug.logInfo("=======Deleting QuestionMaster record in event using service DeleteQuestionMaster=======",
					MODULE);
			try {
				Map<String, ? extends Object> deleteQuestionMasterInfoResult = dispatcher
						.runSync("DeleteQuestionMaster", questionInfo);
				if (UtilValidate.isNotEmpty(questionInfo)) {
					String successMessage = UtilProperties.getMessage(RES_ERR, "DeleteErrorMessage",
							UtilHttp.getLocale(request));
					ServiceUtil.getMessages(request, deleteQuestionMasterInfoResult, successMessage);
					request.setAttribute("_EVENT_MESSAGE_", successMessage);
					return "success";
				} else {
					String errorMessage = ServiceUtil.getErrorMessage(deleteQuestionMasterInfoResult);
					request.setAttribute("_ERROR_MESSAGE_", errorMessage);
					return "error";
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
