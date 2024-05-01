package com.exam.events;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import com.exam.util.ConstantValues;

public class QuestionInformation {
	// Define a constant for the class name
	public static final String module = ExamInformation.class.getName();

	public static String getQuestionInfo(HttpServletRequest request, HttpServletResponse response) {
		// Retrieve the LocalDispatcher and Delegator from the request attributes
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		Delegator delegator = (Delegator) request.getAttribute("delegator");

		// Retrieve examId, noOfQuestions, and initialize sequenceNum and performanceId
		String examId = request.getAttribute(ConstantValues.EXAM_ID).toString();
		String noOfQuestions = request.getAttribute(ConstantValues.EXAM_TOTAL_QUES).toString();
		int sequenceNum = 1;
		String performanceId = null;

		try {
			// Validate examId
			if (UtilValidate.isEmpty(examId)) {
				String errMsg = "examId is a required field on the form and can't be empty.";
				request.setAttribute("ERROR_MESSAGE", errMsg);
				return "error";
			}

			// Validate noOfQuestions
			if (UtilValidate.isEmpty(noOfQuestions)) {
				String errMsg = "noOfQuestion is a required field on the form and can't be empty.";
				request.setAttribute("ERROR_MESSAGE", errMsg);
				return "error";
			}

			Debug.log("=======Logging in process started=========");

			// Query UserExamMapping for the given examId
			List<GenericValue> userExamList = EntityQuery.use(delegator).from("UserExamMapping")
					.where(ConstantValues.EXAM_ID, examId).queryList();

			// Check if userExamList is empty
			if (UtilValidate.isEmpty(userExamList)) {
				String errMsg = "userExamList is empty.";
				request.setAttribute("ERROR_MESSAGE", errMsg);
				return "error";
			}

			// Process each UserExamMapping
			for (GenericValue getUserExamInfo : userExamList) {
				// Retrieve necessary information from UserExamMapping
				String partyId = getUserExamInfo.getString(ConstantValues.USEREXAM_PARTY_ID);
				String noOfAttempt = getUserExamInfo.getString(ConstantValues.USEREXAM_NO_OF_ATTEMPTS);
				String allowedAttempt = getUserExamInfo.getString(ConstantValues.USEREXAM_ALLOWED_ATTEMPTS);
				Integer count = Integer.parseInt(noOfAttempt);
				noOfAttempt = String.valueOf(count + 1);

				// Call service to create a UserAttemptMaster record
				Map<String, Object> createUserAttemptMasterresult = dispatcher.runSync("createUserAttemptMaster",
						UtilMisc.toMap(ConstantValues.EXAM_ID, examId, ConstantValues.EXAM_TOTAL_QUES, noOfQuestions,
								ConstantValues.USEREXAM_PARTY_ID, partyId, ConstantValues.USEREXAM_NO_OF_ATTEMPTS,
								noOfAttempt));

				// Check if the service call resulted in an error
				if (ServiceUtil.isError(createUserAttemptMasterresult)) {
					String errorMessage = ServiceUtil.getErrorMessage(createUserAttemptMasterresult);
					request.setAttribute("ERROR_MESSAGE", errorMessage);
					Debug.logError(errorMessage, module);
					return "error";
				} else {
					// Handle success scenario
					String successMessage = "createUserAttemptMasterresult successfully.";
					ServiceUtil.getMessages(request, createUserAttemptMasterresult, successMessage);
					performanceId = createUserAttemptMasterresult.get(ConstantValues.USER_ATTEMPT_PERFORMANCE_ID)
							.toString();
				}

				// Set performanceId in the session
				if (UtilValidate.isEmpty(performanceId)) {
					String errMsg = "performanceId is required fields on the form and can't be empty.";
					request.setAttribute("ERROR_MESSAGE", errMsg);
					return "error";
				}

				Debug.log("createUserAttemptMasterresult======================" + createUserAttemptMasterresult);

				// Query ExamTopicMapping for the given examId
				List<GenericValue> examTopicList = EntityQuery.use(delegator).from("ExamTopicMapping")
						.where(ConstantValues.EXAM_ID, examId).queryList();

				// Check if examTopicList is empty
				if (UtilValidate.isEmpty(examTopicList)) {
					String errMsg = "examTopicList is required fields on the form and can't be empty.";
					request.setAttribute("ERROR_MESSAGE", errMsg);
					return "error";
				}

				// Process each ExamTopicMapping
				for (GenericValue topicList : examTopicList) {
					// Retrieve necessary information from ExamTopicMapping
					String topicId = topicList.getString(ConstantValues.TOPIC_ID);
					String topicPassPercentage = topicList.getString(ConstantValues.EXAMTOPIC_TOPIC_PASS_PERCENTAGE);
					String questionsPerExam = topicList.getString(ConstantValues.EXAMTOPIC_QUES_PER_EXAM);

					// Validate topic information
					if (UtilValidate.isEmpty(topicId) || UtilValidate.isEmpty(topicPassPercentage)
							|| UtilValidate.isEmpty(questionsPerExam)) {
						String errMsg = "TopicList is empty.";
						request.setAttribute("ERROR_MESSAGE", errMsg);
						return "error";
					}

					// Call service to create a UserAttemptTopicMaster record
					Map<String, Object> createUserAttemptTopicMasterresult = dispatcher.runSync(
							"createUserAttemptTopicMaster",
							UtilMisc.toMap(ConstantValues.TOPIC_ID, topicId, ConstantValues.USER_ANSWER_PERFORMANCE_ID,
									performanceId, ConstantValues.EXAMTOPIC_TOPIC_PASS_PERCENTAGE, topicPassPercentage,
									ConstantValues.USER_TOPIC_TOTAL_QUES, questionsPerExam));

					// Check if the service call resulted in an error
					if (ServiceUtil.isError(createUserAttemptTopicMasterresult)) {
						String errorMessage = ServiceUtil.getErrorMessage(createUserAttemptTopicMasterresult);
						request.setAttribute("ERROR_MESSAGE", errorMessage);
						Debug.logError(errorMessage, module);
						return "error";
					} else {
						// Handle success scenario
						String successMessage = "createUserAttemptTopicMasterresult successfully.";
						ServiceUtil.getMessages(request, createUserAttemptTopicMasterresult, successMessage);
						request.setAttribute("EVENT_MESSAGE", successMessage);
					}

					// Call service to update UserExamMapping with noOfAttempts
					Map<String, Object> updateUserExamMappingnoOfAttemptsresult = dispatcher
							.runSync("updateUserExamMappingnoOfAttempts",
									UtilMisc.toMap(ConstantValues.EXAM_ID, examId,
											ConstantValues.USEREXAM_NO_OF_ATTEMPTS, noOfAttempt,
											ConstantValues.USEREXAM_PARTY_ID, partyId));

					// Check if the service call resulted in an error
					if (ServiceUtil.isError(updateUserExamMappingnoOfAttemptsresult)) {
						String errorMessage = ServiceUtil.getErrorMessage(updateUserExamMappingnoOfAttemptsresult);
						request.setAttribute("ERROR_MESSAGE", errorMessage);
						Debug.logError(errorMessage, module);
						return "error";
					} else {
						// Handle success scenario
						String successMessage = "updateUserExamMappingnoOfAttemptsresult successfully.";
						ServiceUtil.getMessages(request, updateUserExamMappingnoOfAttemptsresult, successMessage);
						request.setAttribute("EVENT_MESSAGE", successMessage);
					}

					// Call service to get question information
					Map<String, Object> getQuestionInformationresult = dispatcher.runSync("getQuestionInformation",
							UtilMisc.toMap(ConstantValues.EXAM_ID, examId, "request", request));

					// Check if the service call resulted in an error
					if (ServiceUtil.isError(getQuestionInformationresult)) {
						String errorMessage = ServiceUtil.getErrorMessage(getQuestionInformationresult);
						request.setAttribute("ERROR_MESSAGE", errorMessage);
						Debug.logError(errorMessage, module);
						return "error";
					} else {
						// Handle success scenario
						String successMessage = "getQuestionInformationresult successfully.";
						ServiceUtil.getMessages(request, getQuestionInformationresult, successMessage);
						request.setAttribute("question", getQuestionInformationresult);
					}
				}

				// Retrieve selectedQuestions from the session
				@SuppressWarnings("unchecked")
				List<List<GenericValue>> questionsInfo = (List<List<GenericValue>>) request.getSession()
						.getAttribute("selectedQuestions");

				// Process each question in selectedQuestions
				for (List<GenericValue> questions : questionsInfo) {
					for (GenericValue question : questions) {
						// Retrieve questionId from the GenericValue
						String questionId = question.getString(ConstantValues.QUES_ID);

						// Call service to create a UserAttemptAnswerMaster record
						Map<String, Object> resultMap = dispatcher.runSync("createUserAttemptAnswerMaster",
								UtilMisc.toMap(ConstantValues.QUES_ID, questionId,
										ConstantValues.USER_ANSWER_PERFORMANCE_ID, performanceId, "sequenceNum",
										sequenceNum));
						
						// Increment sequenceNum
						++sequenceNum;
						if(!ServiceUtil.isSuccess(resultMap))
						{
							String errMsg = "userattempt master is not created successfully";
							request.setAttribute("ERROR_MESSAGE", errMsg);
							return "error";
						}
					}
				}

				// Query UserAttemptAnswerMaster for the given performanceId
				List<GenericValue> userAttemptAnswerMaster = EntityQuery.use(delegator).from("UserAttemptAnswerMaster")
						.where(ConstantValues.USER_ANSWER_PERFORMANCE_ID, performanceId).queryList();
				if (UtilValidate.isEmpty(userAttemptAnswerMaster)) {
					String errMsg = "userAttemptAnswerMaster empty.";
					request.setAttribute("ERROR_MESSAGE", errMsg);
					return "error";
				}
				// Set questionSequence in the request
				request.setAttribute("userAttemptAnswerMaster", userAttemptAnswerMaster);
			}
		} catch (Exception e) {
			// Handle any exceptions that may occur
			String errMsg = "Error in calling or executing the service: " + e.toString();
			request.setAttribute("ERROR_MESSAGE", errMsg);
			return "error";
		}

		// Set success message in the request
		request.setAttribute("EVENT_MESSAGE", "getQuestionInformation successfully.");
		return "success";
	}
}