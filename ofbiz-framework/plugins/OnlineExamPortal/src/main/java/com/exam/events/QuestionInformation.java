package com.exam.events;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import com.exam.util.ConstantValues;
import com.exam.util.EntityConstants;

public class QuestionInformation {
	// Define a constant for the class name
	public static final String MODULE_NAME = QuestionInformation.class.getName();
	private static final String RES_ERR = "OnlineExamPortalUiLabels";

	public static String getQuestionInfo(HttpServletRequest request, HttpServletResponse response) {
		// Retrieve the LocalDispatcher and Delegator from the request attributes
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(EntityConstants.DISPATCHER);
		Delegator delegator = (Delegator) request.getAttribute(EntityConstants.DELEGATOR);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(EntityConstants.USER_LOGIN);

		// Retrieve examId, noOfQuestions, and initialize sequenceNum and performanceId
		String examId = request.getAttribute(ConstantValues.EXAM_ID).toString();
		String partyId = userLogin.getString("partyId");
		String noOfQuestions = request.getAttribute(ConstantValues.EXAM_TOTAL_QUES).toString();
		int sequenceNum = 1;
		String performanceId = null;

		try {
			// Validate examId
			if (UtilValidate.isEmpty(partyId)) {
				String errMsg = "partyId"
						+ UtilProperties.getMessage(RES_ERR, "EmptyVariableMessage", UtilHttp.getLocale(request));
				request.setAttribute("ERROR_MESSAGE", errMsg);
				return "error";
			}

			// Validate noOfQuestions
			if (UtilValidate.isEmpty(noOfQuestions)) {
				String errMsg = "NumberOfQuestions"
						+ UtilProperties.getMessage(RES_ERR, "EmptyVariableMessage", UtilHttp.getLocale(request));
				;
				request.setAttribute("ERROR_MESSAGE", errMsg);
				return "error";
			}

			Debug.log("=======Logging in process started=========");

			// Query UserExamMapping for the given examId
			GenericValue userExamList = EntityQuery.use(delegator).from("UserExamMapping")
					.where(ConstantValues.EXAM_ID, examId, ConstantValues.PARTY_ID, partyId).queryOne();
//
//			// Check if userExamList is empty
//			if (UtilValidate.isEmpty(userExamList)) {
//				String errMsg = "UserExamList"
//						+ UtilProperties.getMessage(RES_ERR, "EmptyVariableMessage", UtilHttp.getLocale(request));
//				;
//				request.setAttribute("ERROR_MESSAGE", errMsg);
//				return "error";
//			}

			// Process each UserExamMapping

			// Retrieve necessary information from UserExamMapping
			String noOfAttempt = userExamList.getString(ConstantValues.USEREXAM_NO_OF_ATTEMPTS);
			String allowedAttempt = userExamList.getString(ConstantValues.USEREXAM_ALLOWED_ATTEMPTS);
			Integer attemptCount = Integer.parseInt(noOfAttempt);
			noOfAttempt = String.valueOf(attemptCount + 1);

			// Call service to create a UserAttemptMaster record
			Map<String, Object> userAttemptMasterResult = dispatcher.runSync("createUserAttemptMaster",
					UtilMisc.toMap(ConstantValues.EXAM_ID, examId, ConstantValues.EXAM_TOTAL_QUES, noOfQuestions,
							ConstantValues.PARTY_ID, partyId, ConstantValues.USEREXAM_NO_OF_ATTEMPTS, noOfAttempt,
							EntityConstants.USER_LOGIN, userLogin));

			// Check if the service call resulted in an error
			if (ServiceUtil.isError(userAttemptMasterResult)) {
				String errorMessage = ServiceUtil.getErrorMessage(userAttemptMasterResult);
				request.setAttribute("ERROR_MESSAGE", errorMessage);
				Debug.logError(errorMessage, MODULE_NAME);
				return "error";
			} else {
				// Handle success scenario
				String successMessage = UtilProperties.getMessage(RES_ERR, "ServiceSuccessMessage",
						UtilHttp.getLocale(request));
				ServiceUtil.getMessages(request, userAttemptMasterResult, successMessage);
				performanceId = userAttemptMasterResult.get(ConstantValues.USER_ATTEMPT_PERFORMANCE_ID).toString();
			}

			if (UtilValidate.isEmpty(performanceId)) {
				String errMsg = "PerformanceID"
						+ UtilProperties.getMessage(RES_ERR, "EmptyVariableMessage", UtilHttp.getLocale(request));
				;
				request.setAttribute("ERROR_MESSAGE", errMsg);
				return "error";
			}

			Debug.log("Create User Attempt Master Result======================" + userAttemptMasterResult);

			// Query ExamTopicMapping for the given examId
			List<GenericValue> examTopicMapping = EntityQuery.use(delegator).from("ExamTopicMapping")
					.where(ConstantValues.EXAM_ID, examId).queryList();

			// Check if examTopicList is empty
			if (UtilValidate.isEmpty(examTopicMapping)) {
				String errMsg = "ExamTopicList"
						+ UtilProperties.getMessage(RES_ERR, "EmptyVariableMessage", UtilHttp.getLocale(request));
				;
				request.setAttribute("ERROR_MESSAGE", errMsg);
				return "error";
			}

			// Process each ExamTopicMapping
			for (GenericValue oneExamTopic : examTopicMapping) {
				// Retrieve necessary information from ExamTopicMapping
				String topicId = oneExamTopic.getString(ConstantValues.TOPIC_ID);
				String topicPassPercentage = oneExamTopic.getString(ConstantValues.EXAM_TOPIC_PASS_PERCENTAGE);
				String questionsPerExam = oneExamTopic.getString(ConstantValues.TOPIC_QUES_PER_EXAM);

				// Validate topic information
				if (UtilValidate.isEmpty(topicId) || UtilValidate.isEmpty(topicPassPercentage)
						|| UtilValidate.isEmpty(questionsPerExam)) {
					String errMsg = "TopicInformation"
							+ UtilProperties.getMessage(RES_ERR, "EmptyVariableMessage", UtilHttp.getLocale(request));
					;
					request.setAttribute("ERROR_MESSAGE", errMsg);
					return "error";
				}

				// Call service to create a UserAttemptTopicMaster record
				Map<String, Object> userAttemptTopicMasterResult = dispatcher.runSync("createUserAttemptTopicMaster",
						UtilMisc.toMap(ConstantValues.TOPIC_ID, topicId, ConstantValues.USER_ANSWER_PERFORMANCE_ID,
								performanceId, ConstantValues.EXAM_TOPIC_PASS_PERCENTAGE, topicPassPercentage,
								ConstantValues.USER_TOPIC_TOTAL_QUES, questionsPerExam, EntityConstants.USER_LOGIN,
								userLogin));

				// Check if the service call resulted in an error
				if (ServiceUtil.isError(userAttemptTopicMasterResult)) {
					String errorMessage = ServiceUtil.getErrorMessage(userAttemptTopicMasterResult);
					request.setAttribute("ERROR_MESSAGE", errorMessage);
					Debug.logError(errorMessage, MODULE_NAME);
					return "error";
				} else {
					// Handle success scenario
					String successMessage = UtilProperties.getMessage(RES_ERR, "ServiceSuccessMessage",
							UtilHttp.getLocale(request));
					ServiceUtil.getMessages(request, userAttemptTopicMasterResult, successMessage);
					request.setAttribute("EVENT_MESSAGE", successMessage);
				}

				// Call service to update UserExamMapping with noOfAttempts
				Map<String, Object> userExamMappingNoOfAttemptsResult = dispatcher
						.runSync("updateUserExamMappingnoOfAttempts",
								UtilMisc.toMap(ConstantValues.EXAM_ID, examId, ConstantValues.USEREXAM_NO_OF_ATTEMPTS,
										noOfAttempt, ConstantValues.PARTY_ID, partyId, EntityConstants.USER_LOGIN,
										userLogin));

				// Check if the service call resulted in an error
				if (ServiceUtil.isError(userExamMappingNoOfAttemptsResult)) {
					String errorMessage = ServiceUtil.getErrorMessage(userExamMappingNoOfAttemptsResult);
					request.setAttribute("ERROR_MESSAGE", errorMessage);
					Debug.logError(errorMessage, MODULE_NAME);
					return "error";
				} else {
					// Handle success scenario
					String successMessage = UtilProperties.getMessage(RES_ERR, "ServiceSuccessMessage",
							UtilHttp.getLocale(request));
					ServiceUtil.getMessages(request, userExamMappingNoOfAttemptsResult, successMessage);
					request.setAttribute("EVENT_MESSAGE", successMessage);
				}

				// Call service to get question information
				Map<String, Object> questionInformationResult = dispatcher.runSync("getQuestionInformation",
						UtilMisc.toMap(ConstantValues.EXAM_ID, examId, "request", request, EntityConstants.USER_LOGIN,
								userLogin));

				// Check if the service call resulted in an error
				if (ServiceUtil.isError(questionInformationResult)) {
					String errorMessage = ServiceUtil.getErrorMessage(questionInformationResult);
					request.setAttribute("ERROR_MESSAGE", errorMessage);
					Debug.logError(errorMessage, MODULE_NAME);
					return "error";
				} else {
					// Handle success scenario
					String successMessage = UtilProperties.getMessage(RES_ERR, "ServiceSuccessMessage",
							UtilHttp.getLocale(request));
					ServiceUtil.getMessages(request, questionInformationResult, successMessage);
					request.setAttribute("question", questionInformationResult);
				}
			}

			// Retrieve selectedQuestions from the session
			@SuppressWarnings("unchecked")
			List<List<GenericValue>> questionsInfoList = (List<List<GenericValue>>) request.getSession()
					.getAttribute("selectedQuestions");

			// Process each question in selectedQuestions
			for (List<GenericValue> questions : questionsInfoList) {
				for (GenericValue oneQuestion : questions) {
					// Retrieve questionId from the GenericValue
					String questionId = oneQuestion.getString(ConstantValues.QUES_ID);

					// Call service to create a UserAttemptAnswerMaster record
					Map<String, Object> resultMap = dispatcher.runSync("createUserAttemptAnswerMaster",
							UtilMisc.toMap(ConstantValues.QUES_ID, questionId,
									ConstantValues.USER_ANSWER_PERFORMANCE_ID, performanceId, "sequenceNum",
									sequenceNum, EntityConstants.USER_LOGIN, userLogin));

					// Increment sequenceNum
					++sequenceNum;
					if (!ServiceUtil.isSuccess(resultMap)) {
						String errMsg = "userAttempt" + UtilProperties.getMessage(RES_ERR, "EmptyVariableMessage",
								UtilHttp.getLocale(request));
						;
						request.setAttribute("ERROR_MESSAGE", errMsg);
						return "error";
					}
				}
			}

			// Query UserAttemptAnswerMaster for the given performanceId
			List<GenericValue> userAttemptAnswerMasterList = EntityQuery.use(delegator).from("UserAttemptAnswerMaster")
					.where(ConstantValues.USER_ANSWER_PERFORMANCE_ID, performanceId).queryList();
			if (UtilValidate.isEmpty(userAttemptAnswerMasterList)) {
				String errMsg = "UserAttemptAnswerMaster"
						+ UtilProperties.getMessage(RES_ERR, "EmptyVariableMessage", UtilHttp.getLocale(request));
				;
				request.setAttribute("ERROR_MESSAGE", errMsg);
				return "error";
			}

			// Set questionSequence in the request
			request.setAttribute("userAttemptAnswerMaster", userAttemptAnswerMasterList);
		} catch (Exception e) {
			// Handle any exceptions that may occur
			String errMsg = "Error in calling or executing the service: " + e.toString();
			request.setAttribute("ERROR_MESSAGE", errMsg);
			return "error";
		}

		// Set success message in the request
		request.setAttribute("EVENT_MESSAGE", "getQuestionInformation successfully.");
//		request.getSession().setAttribute("examId", examId);
		request.getSession().setAttribute("performanceId", performanceId);
		return "success";
	}
}
