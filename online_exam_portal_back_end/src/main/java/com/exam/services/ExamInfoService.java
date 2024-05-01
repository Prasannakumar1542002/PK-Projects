package com.exam.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;

import com.exam.util.ConstantValues;
import com.exam.util.EntityConstants;

public class ExamInfoService {
    public static final String module = ExamInfoService.class.getName();

    // Service method to get exam information based on user login ID
    public static Map<String, Object> getExamInfo(DispatchContext dctx, Map<String, ? extends Object> context) {
        // Retrieve user ID from the context
        String userid = ((GenericValue)context.get("userLogin")).getString(EntityConstants.USER_LOGIN_ID);

        // Check if user ID is empty
        if (UtilValidate.isEmpty(userid)) {
            String errMsg = "Userid is required fields on the form and can't be empty.";
            Debug.log(errMsg);
            return null; // Return early if user ID is empty
        }

        Delegator delegator = (Delegator) dctx.getDelegator();

        try {
            // Query UserLogin entity
            GenericValue getparty = EntityQuery.use(delegator).from("UserLogin").where(EntityConstants.USER_LOGIN_ID, userid).queryOne();

            // Check if UserLogin entity is empty
            if (UtilValidate.isEmpty(getparty)) {
                String errMsg = "UserLogin entity is empty.";
                Debug.log(errMsg);
                return null; // Return early if UserLogin entity is empty
            }

            // Get partyId from UserLogin entity
            String partyid = getparty.getString(ConstantValues.USEREXAM_PARTY_ID);

            // Check if partyId is empty
            if (UtilValidate.isEmpty(partyid)) {
                String errMsg = "Inside UserLogin entity partyId is empty.";
                Debug.log(errMsg);
                return null; // Return early if partyId is empty
            }

            // Query UserExamMapping entity
            List<GenericValue> getuserExamMap = EntityQuery.use(delegator).from("UserExamMapping")
                    .where(ConstantValues.USEREXAM_PARTY_ID, partyid).queryList();

            // Check if UserExamMapping entity is empty
            if (UtilValidate.isEmpty(getuserExamMap)) {
                String errMsg = "UserExamMapping entity is empty.";
                Debug.log(errMsg);
                return null; // Return early if UserExamMapping entity is empty
            }
            List<Map<String, Object>> listExam = new ArrayList<>();
            // Process each UserExamMapping
            for (GenericValue getvalueuserExamMap : getuserExamMap) {
                String examid = getvalueuserExamMap.getString(ConstantValues.EXAM_ID);

                // Check if examid is empty
                if (UtilValidate.isEmpty(examid)) {
                    String errMsg = "Inside UserExamMapping entity examid is empty.";
                    Debug.log(errMsg);
                    return null; // Return early if examid is empty
                }

                // Query ExamMaster entity
                GenericValue getExam = EntityQuery.use(delegator).from("ExamMaster").where(ConstantValues.EXAM_ID, examid).queryOne();

                // Check if ExamMaster entity is empty
                if (UtilValidate.isEmpty(getExam)) {
                    String errMsg = "ExamMaster entity is empty.";
                    Debug.log(errMsg);
                    return null; // Return early if ExamMaster entity is empty
                }

                // Create a map to store exam details
                Map<String, Object> examDetailsMap = new HashMap<>();

                // Process each attribute and add it to the map

                // Exam ID
                String examId = (String) getExam.getString(ConstantValues.EXAM_ID);
                if (UtilValidate.isEmpty(examId)) {
                    String errMsg = "examId is null.";
                    Debug.log(errMsg);
                }
                examDetailsMap.put("examId", examId);

                // Exam Name
                String examName = (String) getExam.getString(ConstantValues.EXAM_NAME);
                if (UtilValidate.isEmpty(examName)) {
                    String errMsg = "examName is null.";
                    Debug.log(errMsg);
                }
                examDetailsMap.put("examName", examName);

                // Description
                String description = (String) getExam.getString(ConstantValues.EXAM_DESCRIPTION);
                if (UtilValidate.isEmpty(description)) {
                    String errMsg = "description is null.";
                    Debug.log(errMsg);
                }
                examDetailsMap.put("description", description);

                // Creation Date
                String creationDate = (String) getExam.getString(ConstantValues.EXAM_CREATION_DATE);
                if (UtilValidate.isEmpty(creationDate)) {
                    String errMsg = "creationDate is null.";
                    Debug.log(errMsg);
                }
                examDetailsMap.put("creationDate", creationDate);

                // Expiration Date
                String expirationDate = (String) getExam.getString(ConstantValues.EXAM_EXPIRATION_DATE);
                if (UtilValidate.isEmpty(expirationDate)) {
                    String errMsg = "expirationDate is null.";
                    Debug.log(errMsg);
                }
                examDetailsMap.put("expirationDate", expirationDate);

                // No Of Questions
                String noOfQuestions = (String) getExam.getString(ConstantValues.EXAM_TOTAL_QUES);
                if (UtilValidate.isEmpty(noOfQuestions)) {
                    String errMsg = "noOfQuestions is null.";
                    Debug.log(errMsg);
                }
                examDetailsMap.put("noOfQuestions", noOfQuestions);

                // Duration Minutes
                String durationMinutes = (String) getExam.getString(ConstantValues.EXAM_DURATION);
                if (UtilValidate.isEmpty(durationMinutes)) {
                    String errMsg = "durationMinutes is null.";
                    Debug.log(errMsg);
                }
                examDetailsMap.put("durationMinutes", durationMinutes);

                // Pass Percentage
                String passPercentage = (String) getExam.getString(ConstantValues.EXAM_PASS_PERCENTAGE);
                if (UtilValidate.isEmpty(passPercentage)) {
                    String errMsg = "passPercentage is null.";
                    Debug.log(errMsg);
                }
                examDetailsMap.put("passPercentage", passPercentage);

                // Questions Randomized
                String questionsRandomized = (String) getExam.getString(ConstantValues.EXAM_QUES_RANDOMIZED);
                if (UtilValidate.isEmpty(questionsRandomized)) {
                    String errMsg = "questionsRandomized is null.";
                    Debug.log(errMsg);
                }
                examDetailsMap.put("questionsRandomized", questionsRandomized);

                // Answers Must
                String answersMust = (String) getExam.getString(ConstantValues.EXAM_ANS_MUST);
                if (UtilValidate.isEmpty(answersMust)) {
                    String errMsg = "answersMust is null.";
                    Debug.log(errMsg);
                }
                examDetailsMap.put("answersMust", answersMust);

                // Enable Negative Mark
                String enableNegativeMark = (String) getExam.getString(ConstantValues.EXAM_ENABLE_NEG_MARK);
                if (UtilValidate.isEmpty(enableNegativeMark)) {
                    String errMsg = "enableNegativeMark is null.";
                    Debug.log(errMsg);
                }
                examDetailsMap.put("enableNegativeMark", enableNegativeMark);

                // Negative Mark Value
                String negativeMarkValue = (String) getExam.getString(ConstantValues.EXAM_NEG_MARK);
                if (UtilValidate.isEmpty(negativeMarkValue)) {
                    String errMsg = "negativeMarkValue is null.";
                    Debug.log(errMsg);
                }
                examDetailsMap.put("negativeMarkValue", negativeMarkValue);


                //  list to store the exam details map
             
                listExam.add(examDetailsMap);

               
            }
            // Create a result map and return it
            Map<String, Object> result = new HashMap<>();
            result.put("exam", listExam);
            return result;

        } catch (Exception e) {
            // Log any exceptions that may occur
            Debug.logError(e, module);
        }

        return null; // Return null if there is an exception
    }
}