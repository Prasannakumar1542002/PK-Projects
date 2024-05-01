package com.exam.events;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.exam.forms.HibernateValidationMaster;
import com.exam.forms.checks.ExamTopicMappingCheck;
import com.exam.helper.HibernateHelper;
import com.exam.util.ConstantValues;


public class ExamTopicMappingEvents {
	public static final String MODULE = ExamTopicMappingEvents.class.getName();
	private static final String RES_ERR = "OnlineExamPortalUiLabels";

	// Method to insert data into ExamTopicMapping Entity
	public static String createExamTopicMappingEvent(HttpServletRequest request, HttpServletResponse response) {
		Locale locale = UtilHttp.getLocale(request);

		Delegator delegator = (Delegator) request.getAttribute("delegator");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

		String examId = (String) request.getAttribute(ConstantValues.EXAMTOPIC_EXAM_ID);
		String topicId = (String) request.getAttribute(ConstantValues.EXAMTOPIC_TOPIC_ID);
		String percentage = (String) request.getAttribute(ConstantValues.EXAMTOPIC_PERCENTAGE);
		String topicPassPercentage = (String) request.getAttribute(ConstantValues.EXAMTOPIC_TOPIC_PASS_PERCENTAGE);
		String questionsPerExam = (String) request.getAttribute(ConstantValues.EXAMTOPIC_QUES_PER_EXAM);

		Map<String, Object> examtopicinfo = UtilMisc.toMap(ConstantValues.EXAM_ID, examId, ConstantValues.TOPIC_ID, topicId, ConstantValues.EXAMTOPIC_PERCENTAGE,
				percentage, ConstantValues.EXAMTOPIC_TOPIC_PASS_PERCENTAGE, topicPassPercentage, ConstantValues.EXAMTOPIC_QUES_PER_EXAM, questionsPerExam);

		try {
			Debug.logInfo(
					"=======Creating ExamTopicMapping record in event using service UpdateExamTopicMapping=========",
					MODULE);
			HibernateValidationMaster hibernate = HibernateHelper.populateBeanFromMap(examtopicinfo,
					HibernateValidationMaster.class);

			Set<ConstraintViolation<HibernateValidationMaster>> errors = HibernateHelper
					.checkValidationErrors(hibernate, ExamTopicMappingCheck.class);
			boolean hasFormErrors = HibernateHelper.validateFormSubmission(delegator, errors, request, locale,
					"Mandatory Err ExamTopicMapping Entity", RES_ERR, false);

			if (!hasFormErrors) {
				try {
					// Calling Entity-Auto Service to Insert data into ExamTopicMapping Entity
					Map<String, ? extends Object> createExamTopicMappingInfoResult = dispatcher
							.runSync("CreateExamTopicMapping", UtilMisc.<String, Object>toMap(examtopicinfo));
					ServiceUtil.getMessages(request, createExamTopicMappingInfoResult, null);
					if (ServiceUtil.isError(createExamTopicMappingInfoResult)) {
						String errorMessage = ServiceUtil.getErrorMessage(createExamTopicMappingInfoResult);
						request.setAttribute("_ERROR_MESSAGE_", errorMessage);
						Debug.logError(errorMessage, MODULE);
						return "error";
					} else {
						String successMessage = "Create ExamTopicMapping Service executed successfully.";
						ServiceUtil.getMessages(request, createExamTopicMappingInfoResult, successMessage);
						request.setAttribute("_EVENT_MESSAGE_", successMessage);
						return "success";
					}
				} catch (GenericServiceException e) {
					String errMsg = "Error setting exam_topic_mapping info: " + e.toString();
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

	// Method to retrieve data's from ExamTopicMapping Entity
	public static String fetchExamTopicMappingEvent(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		List<Map<String, Object>> examTopicMappingdata = new ArrayList<Map<String, Object>>();
		try {
			// Query to retrieve data's from ExamTopicMapping Entity
			List<GenericValue> listOfExamTopicMappingData = EntityQuery.use(delegator).from("ExamTopicMapping")
					.queryList();
			if (UtilValidate.isNotEmpty(listOfExamTopicMappingData)) {
				for (GenericValue list : listOfExamTopicMappingData) {
					Map<String, Object> listOfExamTopicMappingEntity = new HashMap<String, Object>();
					listOfExamTopicMappingEntity.put(ConstantValues.EXAMTOPIC_EXAM_ID,
							list.get(ConstantValues.EXAMTOPIC_EXAM_ID));
					listOfExamTopicMappingEntity.put(ConstantValues.EXAMTOPIC_TOPIC_ID,
							list.get(ConstantValues.EXAMTOPIC_TOPIC_ID));
					listOfExamTopicMappingEntity.put(ConstantValues.EXAMTOPIC_PERCENTAGE,
							list.get(ConstantValues.EXAMTOPIC_PERCENTAGE));
					listOfExamTopicMappingEntity.put(ConstantValues.EXAMTOPIC_TOPIC_PASS_PERCENTAGE,
							list.get(ConstantValues.EXAMTOPIC_TOPIC_PASS_PERCENTAGE));
					listOfExamTopicMappingEntity.put(ConstantValues.EXAMTOPIC_QUES_PER_EXAM,
							list.get(ConstantValues.EXAMTOPIC_QUES_PER_EXAM));
					examTopicMappingdata.add(listOfExamTopicMappingEntity);
				}
				request.setAttribute("ExamTopicMapping", examTopicMappingdata);
				return "success";
			} else {
				String errorMessage = "No matched fields in ExamTopicMapping Entity";
				request.setAttribute("_ERROR_MESSAGE_", errorMessage);
				Debug.logError(errorMessage, MODULE);
				return "error";
			}
		} catch (GenericEntityException e) {
			request.setAttribute("Error", e);
			return "error";
		}
	}

	// Method to Update data into ExamTopicMapping Entity
	public static String updateExamTopicMappingEvent(HttpServletRequest request, HttpServletResponse response) {
		Locale locale = UtilHttp.getLocale(request);

		Delegator delegator = (Delegator) request.getAttribute("delegator");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

		String examId = (String) request.getAttribute(ConstantValues.EXAMTOPIC_EXAM_ID);
		String topicId = (String) request.getAttribute(ConstantValues.EXAMTOPIC_TOPIC_ID);
		String percentage = (String) request.getAttribute(ConstantValues.EXAMTOPIC_PERCENTAGE);
		String topicPassPercentage = (String) request.getAttribute(ConstantValues.EXAMTOPIC_TOPIC_PASS_PERCENTAGE);
		String questionsPerExam = (String) request.getAttribute(ConstantValues.EXAMTOPIC_QUES_PER_EXAM);

		
		
		Map<String, Object> examtopicinfo = UtilMisc.toMap(ConstantValues.EXAM_ID, examId, ConstantValues.TOPIC_ID, topicId, ConstantValues.EXAMTOPIC_PERCENTAGE,
				percentage, ConstantValues.EXAMTOPIC_TOPIC_PASS_PERCENTAGE, topicPassPercentage, ConstantValues.EXAMTOPIC_QUES_PER_EXAM, questionsPerExam);

		try {
			Debug.logInfo(
					"=======Updating ExamTopicMapping record in event using service UpdateExamTopicMappingMaster=========",
					MODULE);
			HibernateValidationMaster hibernate = HibernateHelper.populateBeanFromMap(examtopicinfo,
					HibernateValidationMaster.class);

			Set<ConstraintViolation<HibernateValidationMaster>> errors = HibernateHelper
					.checkValidationErrors(hibernate, ExamTopicMappingCheck.class);
			boolean hasFormErrors = HibernateHelper.validateFormSubmission(delegator, errors, request, locale,
					"Mandatory Err ExamTopicMapping Entity", RES_ERR, false);

			if (!hasFormErrors) {
				try {
					// Calling Entity-Auto Service to Update data into ExamTopicMapping
					Map<String, ? extends Object> updateExamTopicMappingInfoResult = dispatcher
							.runSync("UpdateExamTopicMappingMaster", UtilMisc.<String, Object>toMap(examtopicinfo));
					ServiceUtil.getMessages(request, updateExamTopicMappingInfoResult, null);
					if (ServiceUtil.isError(updateExamTopicMappingInfoResult)) {
						String errorMessage = ServiceUtil.getErrorMessage(updateExamTopicMappingInfoResult);
						request.setAttribute("_ERROR_MESSAGE_", errorMessage);
						Debug.logError(errorMessage, MODULE);
						return "error";
					} else {
						String successMessage = "Update ExamTopicMapping Service executed successfully.";
						ServiceUtil.getMessages(request, updateExamTopicMappingInfoResult, successMessage);
						request.setAttribute("_EVENT_MESSAGE_", successMessage);
						return "success";
					}
				} catch (GenericServiceException e) {
					String errMsg = "Error setting exam_topic_mapping info: " + e.toString();
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

}
