package com.exam.events;

import java.math.BigDecimal;
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
import com.exam.forms.checks.ExamTopicMappingCheck;
import com.exam.helper.HibernateHelper;
import com.exam.util.ConstantValues;
import com.exam.util.EntityConstants;

public class ExamTopicMappingEvents {
	public static final String MODULE = ExamTopicMappingEvents.class.getName();
	private static final String RES_ERR = "OnlineExamPortalUiLabels";

	public static String getSelectedExams(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> combinedMap = UtilHttp.getCombinedMap(request);

		Delegator delegator = (Delegator) combinedMap.get(EntityConstants.DELEGATOR);
		List<Map<String, Object>> viewSelectedTopicsList = new ArrayList<Map<String, Object>>();
		String examId = (String) combinedMap.get(ConstantValues.EXAM_ID);
		List<GenericValue> examForTopics = null;
		try {
			examForTopics = EntityQuery.use(delegator).from("ExamTopicMapping").where(ConstantValues.EXAM_ID, examId)
					.queryList();
		} catch (GenericEntityException e) {
			e.printStackTrace();
		}
		Map<String, Object> selectedTopicsInfo = new HashMap<>();
		Debug.log("" + examForTopics);
		if (UtilValidate.isNotEmpty(examForTopics)) {
			for (GenericValue topicPerExam : examForTopics) {
				Map<String, Object> mappedTopics = new HashMap<String, Object>();
				String topicName = "";
				try {
					topicName = EntityQuery.use(delegator).from("TopicMaster")
							.where(ConstantValues.TOPIC_ID, topicPerExam.get(ConstantValues.TOPIC_ID)).queryOne()
							.getString(ConstantValues.TOPIC_NAME);
				} catch (GenericEntityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mappedTopics.put(ConstantValues.TOPIC_ID, topicPerExam.getString(ConstantValues.TOPIC_ID));
				mappedTopics.put(ConstantValues.TOPIC_NAME, topicName);
				viewSelectedTopicsList.add(mappedTopics);
			}
		}
		selectedTopicsInfo.put("SelectedTopics", viewSelectedTopicsList);
		request.setAttribute("SelectedTopicsInfo", selectedTopicsInfo);
		request.setAttribute("examId", examId);
		return "success";
	}

	public static String calculatePercentage(HttpServletRequest request, HttpServletResponse response)
			throws GenericEntityException {

		Map<String, Object> combinedMap = UtilHttp.getCombinedMap(request);

		Delegator delegator = (Delegator) combinedMap.get(EntityConstants.DELEGATOR);

		List<Map<String, Object>> viewSelectedTopicsList = new ArrayList<Map<String, Object>>();
		String examId = (String) combinedMap.get(ConstantValues.EXAM_ID);
		String percentage = (String) combinedMap.get(ConstantValues.EXAM_TOPIC_PERCENTAGE);
		Integer percentageForExam = 0;
		Integer updatedPercentageForExam = Integer.parseInt(percentage);
		String questionsPerTopic = (String) combinedMap.get(ConstantValues.TOPIC_QUES_PER_EXAM);
		Integer questionsForExam = 0;
		GenericValue exam = EntityQuery.use(delegator).from("ExamMaster").where(ConstantValues.EXAM_ID, examId)
				.queryOne();
		Debug.log("Exam: " + exam);
		String examQues = exam.getString(ConstantValues.EXAM_TOTAL_QUES);
		Debug.log("--------------------- " + examQues + " " + (Integer.valueOf(examQues)));
		Debug.log("Percentage: " + percentage + " " + Integer.valueOf(percentage));
		Integer updatedQuestions = 0;
		Integer topicQuestionsPerExam = (int) ((Float.valueOf(percentage) / 100) * Integer.valueOf(examQues));
		Debug.log("TopicPerExamQuestions: " + ((Integer.valueOf(percentage) / 100) * Integer.valueOf(examQues)));
		Debug.log("=============" + topicQuestionsPerExam);

		List<GenericValue> examForTopics = EntityQuery.use(delegator).from("ExamTopicMapping")
				.where(ConstantValues.EXAM_ID, examId).queryList();
		Map<String, Object> selectedTopicsInfo = new HashMap<>();
		Debug.log("" + examForTopics);
//		if (UtilValidate.isNotEmpty(examForTopics)) {
			for (GenericValue topicPerExam : examForTopics) {
				Map<String, Object> mappedTopics = new HashMap<String, Object>();
				String topicName = EntityQuery.use(delegator).from("TopicMaster")
						.where(ConstantValues.TOPIC_ID, topicPerExam.get(ConstantValues.TOPIC_ID)).queryOne()
						.getString(ConstantValues.TOPIC_NAME);
				String percentageCount = topicPerExam.getString(ConstantValues.EXAM_TOPIC_PERCENTAGE);
				String questionsCount = topicPerExam.getString(ConstantValues.TOPIC_QUES_PER_EXAM);
				Debug.log("--------" + percentageCount);
				questionsForExam = questionsForExam + Integer.parseInt(questionsCount);
				percentageForExam = percentageForExam + new BigDecimal(percentageCount).intValue();
				updatedPercentageForExam = updatedPercentageForExam + new BigDecimal(percentageCount).intValue();
				mappedTopics.put(ConstantValues.TOPIC_ID, topicPerExam.getString(ConstantValues.TOPIC_ID));
				mappedTopics.put(ConstantValues.TOPIC_NAME, topicName);
				viewSelectedTopicsList.add(mappedTopics);
			}
			if (updatedPercentageForExam > 100) {
				String warningMessage = UtilProperties.getMessage(RES_ERR, "PercentageWarningMessage",
						UtilHttp.getLocale(request));
				percentageForExam = 100 - percentageForExam;
				selectedTopicsInfo.put("SelectedTopics", viewSelectedTopicsList);
				selectedTopicsInfo.put(ConstantValues.EXAM_ID, examId);
				request.setAttribute("SelectedTopicsInfo", selectedTopicsInfo);
				request.setAttribute("message", warningMessage);
				request.setAttribute("percentage", percentageForExam);
				Debug.log("" + selectedTopicsInfo);
			} else {
				String successMessage = UtilProperties.getMessage(RES_ERR, "PercentageSuccessMessage",
						UtilHttp.getLocale(request));
				Debug.log(successMessage + "" + updatedPercentageForExam);
				request.setAttribute("message", successMessage);
				request.setAttribute("percentage", updatedPercentageForExam);
				selectedTopicsInfo.put("SelectedTopics", viewSelectedTopicsList);
				selectedTopicsInfo.put(ConstantValues.EXAM_ID, examId);
				request.setAttribute("SelectedTopicsInfo", selectedTopicsInfo);
				Debug.log("" + selectedTopicsInfo);
			}
			updatedQuestions = Integer.parseInt(examQues) - questionsForExam;
			Debug.log("-----------------------Updated Questions: " + updatedQuestions);
			request.setAttribute("questions", updatedQuestions);
//		} else {
//			request.setAttribute("message", "No previous records found for this examID.");
//			percentageForExam = Integer.parseInt(percentage);
//			updatedQuestions = Integer.parseInt(examQues) - Integer.parseInt(questionsPerTopic);
//			request.setAttribute("percentage", percentageForExam);
//			request.setAttribute("questions", updatedQuestions);
//		}

		request.setAttribute("topicQuestionsPerExam", topicQuestionsPerExam);
		return "success";
	}

	// Method to insert data into ExamTopicMapping Entity
	public static String createTopicForExam(HttpServletRequest request, HttpServletResponse response)
			throws GenericEntityException {
		Locale locale = UtilHttp.getLocale(request);
		Map<String, Object> combinedMap = UtilHttp.getCombinedMap(request);

		Delegator delegator = (Delegator) combinedMap.get(EntityConstants.DELEGATOR);
		LocalDispatcher dispatcher = (LocalDispatcher) combinedMap.get(EntityConstants.DISPATCHER);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(EntityConstants.USER_LOGIN);
		String examId = (String) combinedMap.get(ConstantValues.EXAM_ID);
		String topicId = (String) combinedMap.get(ConstantValues.TOPIC_ID);
		String percentage = (String) combinedMap.get(ConstantValues.EXAM_TOPIC_PERCENTAGE);
		String topicPassPercentage = (String) combinedMap.get(ConstantValues.EXAM_TOPIC_PASS_PERCENTAGE);
		String questionsPerTopic = (String) combinedMap.get(ConstantValues.TOPIC_QUES_PER_EXAM);
		Integer percentageForExam = 0;
		Integer updatedPercentageForExam = Integer.parseInt(percentage);
		Integer questionsForExam = 0;
		GenericValue exam = EntityQuery.use(delegator).from("ExamMaster").where(ConstantValues.EXAM_ID, examId)
				.queryOne();
		Debug.log("Exam: " + exam);

		Map<String, Object> examTopicInfo = UtilMisc.toMap(ConstantValues.EXAM_ID, examId, ConstantValues.TOPIC_ID,
				topicId, ConstantValues.EXAM_TOPIC_PERCENTAGE, percentage, ConstantValues.EXAM_TOPIC_PASS_PERCENTAGE,
				topicPassPercentage, ConstantValues.TOPIC_QUES_PER_EXAM, questionsPerTopic, EntityConstants.USER_LOGIN,
				userLogin);
		Debug.log("EXAMTOPIC: " + examTopicInfo);
		List<GenericValue> questionsPerTopicList = EntityQuery.use(delegator).from("ExamTopicMapping")
				.where(ConstantValues.EXAM_ID, examId).queryList();
		try {
			Integer examQues = Integer.parseInt(exam.getString(ConstantValues.EXAM_TOTAL_QUES));
			Debug.log("*********************" + examQues);
			Debug.log("List\n" + questionsPerTopicList);
//			Debug.log("PercentageForExam: " + percentageForExam);
//			request.setAttribute("message", percentageForExam);
			for (GenericValue questionPerTopic : questionsPerTopicList) {
				String questionsCount = questionPerTopic.getString(ConstantValues.TOPIC_QUES_PER_EXAM);
				Debug.logInfo("Percentage", questionsCount);
				questionsForExam = questionsForExam + Integer.parseInt(questionsCount);
			}

			for (GenericValue percentagePerTopic : questionsPerTopicList) {
				String percentageCount = percentagePerTopic.getString(ConstantValues.EXAM_TOPIC_PERCENTAGE);
				Debug.log("--------" + percentageCount);
				percentageForExam = percentageForExam + new BigDecimal(percentageCount).intValue();
				updatedPercentageForExam = updatedPercentageForExam + new BigDecimal(percentageCount).intValue();
			}
			Debug.log("PercentageForExam: " + percentageForExam);
			Integer checkQuestionsCount = questionsForExam + Integer.parseInt(questionsPerTopic);
			Integer updatedQuestions = examQues - questionsForExam - Integer.parseInt(questionsPerTopic);

			if (checkQuestionsCount <= examQues) {
				Map<String, Object> createExamTopicMappingInfoResult = dispatcher.runSync("CreateExamTopicMapping",
						examTopicInfo);
				Debug.log("" + createExamTopicMappingInfoResult);
				ServiceUtil.getMessages(request, createExamTopicMappingInfoResult, null);
				if (ServiceUtil.isError(createExamTopicMappingInfoResult)) {
					String errorMessage = ServiceUtil.getErrorMessage(createExamTopicMappingInfoResult);
					request.setAttribute("message", errorMessage);
					Debug.log("Percentage:", percentageForExam);
					request.setAttribute("percentage", percentageForExam);
					return "error";
				} else {
					String successMessage = UtilProperties.getMessage(RES_ERR, "ExamForTopicSuccessMessage",
							UtilHttp.getLocale(request));
					ServiceUtil.getMessages(request, createExamTopicMappingInfoResult, successMessage);
					request.setAttribute("message", successMessage);
					request.setAttribute("percentage", updatedPercentageForExam);
					request.setAttribute("questions", updatedQuestions);
					return "success";
				}
			} else {
				String errMessage = UtilProperties.getMessage(RES_ERR, "ExamForTopicWarningMessage",
						UtilHttp.getLocale(request));
				request.setAttribute("message", errMessage);
				return "error";
			}
		} catch (GenericServiceException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return "success";
		}
	}

	// Method to retrieve data's from ExamTopicMapping Entity
	public static String fetchAllExamTopics(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute(EntityConstants.DELEGATOR);
		List<Map<String, Object>> viewExamTopicMapList = new ArrayList<Map<String, Object>>();
		try {
			// Query to retrieve data's from ExamTopicMapping Entity
			List<GenericValue> listOfExamTopicMapData = EntityQuery.use(delegator).from("ExamTopicMapping").queryList();
			if (UtilValidate.isNotEmpty(listOfExamTopicMapData)) {
				for (GenericValue topicOfExam : listOfExamTopicMapData) {
					Map<String, Object> topicToMap = new HashMap<String, Object>();
					try {
						String examName = EntityQuery.use(delegator).from("ExamMaster")
								.where(ConstantValues.EXAM_ID, topicOfExam.get(ConstantValues.EXAM_ID)).queryOne()
								.getString(ConstantValues.EXAM_NAME);
						String topicName = EntityQuery.use(delegator).from("TopicMaster")
								.where(ConstantValues.TOPIC_ID, topicOfExam.get(ConstantValues.TOPIC_ID)).queryOne()
								.getString(ConstantValues.TOPIC_NAME);
						if (UtilValidate.isEmpty(examName)) {
							Debug.logInfo("ExamName: ", examName);
							Debug.logInfo("TopicName: ", topicName);
						} else {
							Debug.logInfo("ExamName: ", examName);
							Debug.logInfo("TopicName: ", topicName);
							topicToMap.put(ConstantValues.EXAM_ID, examName);
							topicToMap.put(ConstantValues.TOPIC_ID, topicName);

						}
					} catch (Exception e) {
						Debug.logError(e, MODULE);
					}
					topicToMap.put(ConstantValues.EXAM_TOPIC_PERCENTAGE,
							topicOfExam.get(ConstantValues.EXAM_TOPIC_PERCENTAGE));
					topicToMap.put(ConstantValues.EXAM_TOPIC_PASS_PERCENTAGE,
							topicOfExam.get(ConstantValues.EXAM_TOPIC_PASS_PERCENTAGE));
					topicToMap.put(ConstantValues.TOPIC_QUES_PER_EXAM,
							topicOfExam.get(ConstantValues.TOPIC_QUES_PER_EXAM));
					viewExamTopicMapList.add(topicToMap);

				}
				Map<String, Object> examforTopicsInfo = new HashMap<>();
				examforTopicsInfo.put("ExamTopicList", viewExamTopicMapList);
				request.setAttribute("ExamTopicsInfo", examforTopicsInfo);
				return "success";
			}
			String errorMessage = UtilProperties.getMessage(RES_ERR, "ErrorInFetchingData",
					UtilHttp.getLocale(request));// "No matched fields in ExamTopicMapping Entity";
			request.setAttribute("_ERROR_MESSAGE_", errorMessage);
			Debug.logError(errorMessage, MODULE);
			return "error";

		} catch (GenericEntityException e) {
			request.setAttribute("Error", e);
			return "error";
		}
	}

	// Method to Update data into ExamTopicMapping Entity
	public static String updateTopicForExam(HttpServletRequest request, HttpServletResponse response) {
		Locale locale = UtilHttp.getLocale(request);

		Delegator delegator = (Delegator) request.getAttribute(EntityConstants.DELEGATOR);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(EntityConstants.DISPATCHER);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(EntityConstants.USER_LOGIN);

		String examId = (String) request.getAttribute(ConstantValues.EXAM_ID);
		String topicId = (String) request.getAttribute(ConstantValues.TOPIC_ID);
		String percentage = (String) request.getAttribute(ConstantValues.EXAM_TOPIC_PERCENTAGE);
		String topicPassPercentage = (String) request.getAttribute(ConstantValues.EXAM_TOPIC_PASS_PERCENTAGE);
		String questionsPerExam = (String) request.getAttribute(ConstantValues.TOPIC_QUES_PER_EXAM);

		Map<String, Object> examtopicinfo = UtilMisc.toMap(ConstantValues.EXAM_ID, examId, ConstantValues.TOPIC_ID,
				topicId, ConstantValues.EXAM_TOPIC_PERCENTAGE, percentage, ConstantValues.EXAM_TOPIC_PASS_PERCENTAGE,
				topicPassPercentage, ConstantValues.TOPIC_QUES_PER_EXAM, questionsPerExam, EntityConstants.USER_LOGIN,
				userLogin);

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

			if (hasFormErrors) {
				request.setAttribute("_ERROR_MESSAGE", errors);
				return "error";
			}
			try {
				// Calling Entity-Auto Service to Update data into ExamTopicMapping
				Map<String, ? extends Object> updateExamTopicMappingInfoResult = dispatcher
						.runSync("UpdateExamTopicMappingMaster", examtopicinfo);
				ServiceUtil.getMessages(request, updateExamTopicMappingInfoResult, null);
				if (ServiceUtil.isError(updateExamTopicMappingInfoResult)) {
					String errorMessage = ServiceUtil.getErrorMessage(updateExamTopicMappingInfoResult);
					request.setAttribute("_ERROR_MESSAGE_", errorMessage);
					Debug.logError(errorMessage, MODULE);
					return "error";
				}
				String successMessage = UtilProperties.getMessage(RES_ERR, "ServiceSuccessMessage",
						UtilHttp.getLocale(request));
				ServiceUtil.getMessages(request, updateExamTopicMappingInfoResult, successMessage);
				request.setAttribute("_EVENT_MESSAGE_", successMessage);
				return "success";

			} catch (GenericServiceException e) {
				String errMsg = UtilProperties.getMessage(RES_ERR, "ServiceCallingError", UtilHttp.getLocale(request))
						+ e.toString();// "Error setting exam_topic_mapping info: " +
										// e.toString();
				request.setAttribute("_ERROR_MESSAGE_", errMsg);
				return "error";
			}
		} catch (Exception e) {
			Debug.logError(e, MODULE);
			request.setAttribute("_ERROR_MESSAGE", e);
			return "error";
		}
	}

//	public static String questionsPerTopic(HttpServletRequest request, HttpServletResponse response)
//	{
//		Delegator delegator = (Delegator) request.getAttribute(EntityConstants.DELEGATOR);
//		Map<String,Object> combinedMap = UtilHttp.getCombinedMap(request);
//		
//		Integer totalQuesCount = 0;
//		String examId = (String)combinedMap.get(ConstantValues.EXAM_ID);
//		String topicId = (String)combinedMap.get(ConstantValues.TOPIC_ID);
//		String questionPercentage = (String)combinedMap.get(ConstantValues.EXAMTOPIC_PERCENTAGE);
//		String questionsPerTopic = (String)combinedMap.get(ConstantValues.EXAMTOPIC_QUES_PER_EXAM);
//		String percentage = (String) combinedMap.get(ConstantValues.EXAMTOPIC_PERCENTAGE);
//		String topicPassPercentage = (String) combinedMap.get(ConstantValues.EXAMTOPIC_TOPIC_PASS_PERCENTAGE);
//		Integer questionsPerExam = 0;
//		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(EntityConstants.USER_LOGIN);
//		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(EntityConstants.DISPATCHER);
//		
//		Debug.log("^^^^^^^^^^^^^ "+examId + " " + topicId + " " + questionPercentage + " " + questionsPerTopic + " " + percentage + " " + topicPassPercentage);
//		try {
//			GenericValue exam = EntityQuery.use(delegator).from("ExamMaster").where(ConstantValues.EXAM_ID, examId).queryOne();
//			Debug.log("@@@@@@@@@@@@@@@" + exam);
//			if (exam==null)
//			{
//				questionsPerExam = 
//				Map<String, Object> examtopicinfo = UtilMisc.toMap(ConstantValues.EXAM_ID, examId, ConstantValues.TOPIC_ID,
//						topicId, ConstantValues.EXAMTOPIC_PERCENTAGE, percentage,
//						ConstantValues.EXAMTOPIC_TOPIC_PASS_PERCENTAGE, topicPassPercentage,ConstantValues.EXAMTOPIC_QUES_PER_EXAM, questionsPerExam,
//						EntityConstants.USER_LOGIN, userLogin);
//				try {
//					Map<String, ? extends Object> createExamTopicMappingInfoResult = dispatcher
//							.runSync("CreateExamTopicMapping", examtopicinfo);
//					Debug.log("Result " + createExamTopicMappingInfoResult);
//				} catch (GenericServiceException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			String examQues = exam.getString(ConstantValues.EXAM_TOTAL_QUES);
//			Debug.log("*********************"+examQues);
//			List<GenericValue> questionsPerTopicList = EntityQuery.use(delegator).from("ExamTopicMapping").where(ConstantValues.EXAM_ID, examId).queryList();
//			Debug.log("List\n"+questionsPerTopicList);
//			for (GenericValue questionPerTopic:questionsPerTopicList)
//			{
//				String questionsCount = questionPerTopic.getString(ConstantValues.EXAMTOPIC_QUES_PER_EXAM);
//				Debug.log("--------"+questionsCount);
//				totalQuesCount = totalQuesCount + Integer.parseInt(questionsCount);
//			}
//		} catch (GenericEntityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		request.setAttribute("output", totalQuesCount);
//		return "success";
//		
//	}

}
