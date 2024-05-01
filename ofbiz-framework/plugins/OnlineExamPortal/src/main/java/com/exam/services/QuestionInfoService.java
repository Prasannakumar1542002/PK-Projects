package com.exam.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;

import com.exam.util.ConstantValues;

public class QuestionInfoService {

	public static final Map<String, Object> emptyMap = UtilMisc.toMap("status", "error");
	// Main service method to get question information
	private static final String RES_ERR = "OnlineExamPortalUiLabels";

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getQuestionInfo(DispatchContext dctx, Map<String, ? extends Object> context) {

		// Extracting parameters from the context
		String examId = (String) context.get(ConstantValues.EXAM_ID);
		Delegator delegator = dctx.getDelegator();
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> question = new HashMap<>();
		HttpServletRequest request = (HttpServletRequest) context.get("request");
		try {
			// Validate examId
			if (UtilValidate.isEmpty(examId)) {
				String errMsg = "examId"
						+ UtilProperties.getMessage(RES_ERR, "EmptyVariableMessage", UtilHttp.getLocale(request));
				Debug.log(errMsg);
				return emptyMap;
			}

			// Retrieve a list of topics for the given examId
			List<GenericValue> examTopicMappingList = EntityQuery.use(delegator).from("ExamTopicMapping")
					.where(ConstantValues.EXAM_ID, examId).queryList();

			if (UtilValidate.isEmpty(examTopicMappingList)) {
				String errMsg = "examTopicMappingList"
						+ UtilProperties.getMessage(RES_ERR, "EmptyVariableMessage", UtilHttp.getLocale(request));
				Debug.log(errMsg);
				return emptyMap; // If no topics found, return emptyMap
			}

			// Loop through each topic and select random questions
			for (GenericValue getTopic : examTopicMappingList) {
				String topicId = getTopic.getString(ConstantValues.TOPIC_ID);

				// Validate topicId
				if (UtilValidate.isEmpty(topicId)) {
					String errMsg = "topicId"
							+ UtilProperties.getMessage(RES_ERR, "EmptyVariableMessage", UtilHttp.getLocale(request));
					Debug.log(errMsg);
					return emptyMap; // If no topics found, return emptyMap
				}

				// Get the number of questions to be selected for the current topic
				Integer questionsPerExam = Integer.parseInt(getTopic.getString(ConstantValues.TOPIC_QUES_PER_EXAM));

				// Validate questionsPerExam
				if (UtilValidate.isEmpty(questionsPerExam)) {
					String errMsg = "questionsPerExam"
							+ UtilProperties.getMessage(RES_ERR, "EmptyVariableMessage", UtilHttp.getLocale(request));
					Debug.log(errMsg);
					return emptyMap; // If no topics found, return emptyMap
				}

				// Process the topic only if both topicId and questionsPerExam are not null
				if (topicId != null && questionsPerExam != null) {
					// Retrieve all questions for the current topic
					List<GenericValue> topicQuestions = EntityQuery.use(delegator).from("QuestionMaster")
							.where(ConstantValues.TOPIC_ID, topicId).queryList();

					// Log if no questions available for the topic
					if (UtilValidate.isEmpty(topicQuestions)) {
						String errMsg = "topicQuestions" + UtilProperties.getMessage(RES_ERR, "EmptyVariableMessage",
								UtilHttp.getLocale(request));
						Debug.log(errMsg);
						return emptyMap; // If no topics found, return emptyMap
					}

					Random rd = new Random();
					List<GenericValue> questionList = new ArrayList<>();

					// Select random questions based on questionsPerExam
					for (int i = 0; i < questionsPerExam; i++) {
						int rand = rd.nextInt(topicQuestions.size());
						questionList.add(topicQuestions.get(rand));
						topicQuestions.remove(rand);
					}

					// Add the selected questions to the map if not empty
					if (UtilValidate.isNotEmpty(questionList)) {
						question.put(topicId, questionList);
					}
				}
			}

			// Prepare a list of lists containing selected questions
			List<List<GenericValue>> questionTopicList = new ArrayList<>();
			for (Entry<String, Object> entry : question.entrySet()) {
				questionTopicList.add((List<GenericValue>) entry.getValue());
			}

			// Add the list of selected questions to the result map and session attribute
			if (UtilValidate.isNotEmpty(questionTopicList)) {
				result.put("examquestion", questionTopicList);
				request.getSession().setAttribute("selectedQuestions", questionTopicList);
			}

			return result; // Return the result map
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage()); // Log the exception message

		}

		return emptyMap; // Return emptyMap in case of an exception
	}
}