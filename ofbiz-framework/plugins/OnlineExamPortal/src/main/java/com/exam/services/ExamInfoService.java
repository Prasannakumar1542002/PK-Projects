package com.exam.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class ExamInfoService {
	public static final String MODULE_NAME = ExamInfoService.class.getName();
	public static final Map<String, Object> emptyMap = UtilMisc.toMap("status", "error");
	private static final String RES_ERR = "OnlineExamPortalUiLabels";

	// Service method to get exam information based on user login ID
	public static Map<String, Object> getExamInfo(DispatchContext dispatchContext,
			Map<String, ? extends Object> context) {
		HttpServletRequest request = (HttpServletRequest) context.get("request");

		Delegator delegator = dispatchContext.getDelegator();

		try {
			// Get partyId from UserLogin entity
			String partyId = ((GenericValue) context.get("userLogin")).getString(ConstantValues.PARTY_ID);

			// Check if partyId is empty
			if (UtilValidate.isEmpty(partyId)) {
				String errMsg = "partyId "
						+ UtilProperties.getMessage(RES_ERR, "EmptyVariableMessage", UtilHttp.getLocale(request));
				Debug.log(errMsg);
				return emptyMap; // Return early if partyId is empty
			}

			// Query UserExamMapping entity
			List<GenericValue> userExamMappingList = EntityQuery.use(delegator).from("UserExamMapping")
					.where(ConstantValues.PARTY_ID, partyId).queryList();

			// Check if UserExamMapping entity is empty
			if (UtilValidate.isEmpty(userExamMappingList)) {
				String errMsg = "UserExamMapping entity"
						+ UtilProperties.getMessage(RES_ERR, "EmptyVariableMessage", UtilHttp.getLocale(request));
				Debug.log(errMsg);
				return emptyMap; // Return early if UserExamMapping entity is empty
			}

			List<Map<String, Object>> examList = new ArrayList<>();
			// Process each UserExamMapping
			for (GenericValue userExamMapping : userExamMappingList) {
				String examId = userExamMapping.getString(ConstantValues.EXAM_ID);

				// Check if examId is empty
				if (UtilValidate.isEmpty(examId)) {
					String errMsg = "examId"
							+ UtilProperties.getMessage(RES_ERR, "EmptyVariableMessage", UtilHttp.getLocale(request));
					Debug.log(errMsg);
					return emptyMap; // Return early if examId is empty
				}

				// Query ExamMaster entity
				GenericValue examMasterEntity = EntityQuery.use(delegator).from("ExamMaster")
						.where(ConstantValues.EXAM_ID, examId).queryOne();

				// Check if ExamMaster entity is empty
				if (UtilValidate.isEmpty(examMasterEntity)) {
					String errMsg = "ExamMaster entity "
							+ UtilProperties.getMessage(RES_ERR, "EmptyVariableMessage", UtilHttp.getLocale(request));
					Debug.log(errMsg);
					return emptyMap; // Return early if ExamMaster entity is empty
				}
				// Expiration Date
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				String expirationDateString=examMasterEntity.getString(ConstantValues.EXAM_EXPIRATION_DATE);
				LocalDateTime expirationDate = LocalDateTime
						.parse(expirationDateString.substring(0, expirationDateString.length()-2),formatter);
				if (UtilValidate.isEmpty(expirationDate)) {
					String errMsg = "ExpirationDate"
							+ UtilProperties.getMessage(RES_ERR, "EmptyVariableMessage", UtilHttp.getLocale(request));
					Debug.log(errMsg);
					return emptyMap;
				}
				if (expirationDate.isAfter(LocalDateTime.now())) {
					// Create a map to store exam details
					Map<String, Object> examDetailsMap = new HashMap<>();
					examDetailsMap.put("expirationDate", expirationDate.toString());
					examDetailsMap.put("examId", examId);

					// Exam Name
					String examName = (String) examMasterEntity.getString(ConstantValues.EXAM_NAME);
					if (UtilValidate.isEmpty(examName)) {
						String errMsg = "ExamName" + UtilProperties.getMessage(RES_ERR, "EmptyVariableMessage",
								UtilHttp.getLocale(request));
						Debug.log(errMsg);
						return emptyMap;
					}
					examDetailsMap.put("examName", examName);

					// Description
					String description = (String) examMasterEntity.getString(ConstantValues.EXAM_DESCRIPTION);
					if (UtilValidate.isEmpty(description)) {
						String errMsg = "Description" + UtilProperties.getMessage(RES_ERR, "EmptyVariableMessage",
								UtilHttp.getLocale(request));
						Debug.log(errMsg);
						return emptyMap;
					}
					examDetailsMap.put("description", description);

					// Creation Date
					String creationDate = (String) examMasterEntity.getString(ConstantValues.EXAM_CREATION_DATE);
					if (UtilValidate.isEmpty(creationDate)) {
						String errMsg = "CreationDate" + UtilProperties.getMessage(RES_ERR, "EmptyVariableMessage",
								UtilHttp.getLocale(request));
						Debug.log(errMsg);
						return emptyMap;
					}
					examDetailsMap.put("creationDate", creationDate);

					// No Of Questions
					String noOfQuestions = (String) examMasterEntity.getString(ConstantValues.EXAM_TOTAL_QUES);
					if (UtilValidate.isEmpty(noOfQuestions)) {
						String errMsg = "NumberOfQuestions" + UtilProperties.getMessage(RES_ERR, "EmptyVariableMessage",
								UtilHttp.getLocale(request));
						Debug.log(errMsg);
						return emptyMap;
					}
					examDetailsMap.put("noOfQuestions", noOfQuestions);

					// Duration Minutes
					String durationMinutes = (String) examMasterEntity.getString(ConstantValues.EXAM_DURATION);
					if (UtilValidate.isEmpty(durationMinutes)) {
						String errMsg = "DurationMinutes" + UtilProperties.getMessage(RES_ERR, "EmptyVariableMessage",
								UtilHttp.getLocale(request));
						Debug.log(errMsg);
						return emptyMap;
					}
					examDetailsMap.put("durationMinutes", durationMinutes);

					// Pass Percentage
					String passPercentage = (String) examMasterEntity.getString(ConstantValues.EXAM_PASS_PERCENTAGE);
					if (UtilValidate.isEmpty(passPercentage)) {
						String errMsg = "PassPercentage" + UtilProperties.getMessage(RES_ERR, "EmptyVariableMessage",
								UtilHttp.getLocale(request));
						Debug.log(errMsg);
						return emptyMap;
					}
					examDetailsMap.put("passPercentage", passPercentage);

					// Questions Randomized
					String questionsRandomized = (String) examMasterEntity
							.getString(ConstantValues.EXAM_QUES_RANDOMIZED);
					if (UtilValidate.isEmpty(questionsRandomized)) {
						String errMsg = "QuestionsRandomized" + UtilProperties.getMessage(RES_ERR,
								"EmptyVariableMessage", UtilHttp.getLocale(request));
						Debug.log(errMsg);
						return emptyMap;
					}
					examDetailsMap.put("questionsRandomized", questionsRandomized);

					// Answers Must
					String answersMust = (String) examMasterEntity.getString(ConstantValues.EXAM_ANS_MUST);
					if (UtilValidate.isEmpty(answersMust)) {
						String errMsg = "AnswersMust" + UtilProperties.getMessage(RES_ERR, "EmptyVariableMessage",
								UtilHttp.getLocale(request));
						Debug.log(errMsg);
						return emptyMap;
					}
					examDetailsMap.put("answersMust", answersMust);

					// Enable Negative Mark
					String enableNegativeMark = (String) examMasterEntity
							.getString(ConstantValues.EXAM_ENABLE_NEG_MARK);
					if (UtilValidate.isEmpty(enableNegativeMark)) {
						String errMsg = "EnableNegativeMark" + UtilProperties.getMessage(RES_ERR,
								"EmptyVariableMessage", UtilHttp.getLocale(request));
						Debug.log(errMsg);
						return emptyMap;
					}
					examDetailsMap.put("enableNegativeMark", enableNegativeMark);

					// Negative Mark Value
					String negativeMarkValue = (String) examMasterEntity.getString(ConstantValues.EXAM_NEG_MARK);
					if (UtilValidate.isEmpty(negativeMarkValue)) {
						String errMsg = "NegativeMark" + UtilProperties.getMessage(RES_ERR, "EmptyVariableMessage",
								UtilHttp.getLocale(request));
						Debug.log(errMsg);
						return emptyMap;
					}
					examDetailsMap.put("negativeMarkValue", negativeMarkValue);

					// Add the exam details map to the list
					examList.add(examDetailsMap);
				}
			}

			// Create a result map and return it
			Map<String, Object> result = new HashMap<>();
			result.put("examList", examList);
			return result;

		} catch (Exception e) {
			// Log any exceptions that may occur
			Debug.logError(e, MODULE_NAME);
			return emptyMap;
		}

	}
}
