package com.exam.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;

import com.exam.util.ConstantValues;

public class QuestionInfoService {

    // Main service method to get question information
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
                Debug.log("examId is null");
                return null; // If examId is empty, return null
            }

            // Retrieve a list of topics for the given examId
            List<GenericValue> examTopicList = EntityQuery.use(delegator).from("ExamTopicMapping")
                    .where(ConstantValues.EXAM_ID, examId).queryList();

            if (UtilValidate.isEmpty(examTopicList)) {
                Debug.log("ExamTopicList is null or empty..."); // Log if no topics found for the exam
                return null; // If no topics found, return null
            }

            // Loop through each topic and select random questions
            for (GenericValue getTopic : examTopicList) {
                String topicId = getTopic.getString(ConstantValues.TOPIC_ID);

                // Validate topicId
                if (UtilValidate.isEmpty(topicId)) {
                    Debug.log("topicId is null");
                }

                // Get the number of questions to be selected for the current topic
                Integer questionsPerExam = Integer.parseInt(getTopic.getString(ConstantValues.EXAMTOPIC_QUES_PER_EXAM));

                // Validate questionsPerExam
                if (UtilValidate.isEmpty(questionsPerExam)) {
                    Debug.log("questionsPerExam is null");
                }

                // Process the topic only if both topicId and questionsPerExam are not null
                if (topicId != null && questionsPerExam != null) {
                    // Retrieve all questions for the current topic
                    List<GenericValue> topicQuestions = EntityQuery.use(delegator).from("QuestionMaster")
                            .where(ConstantValues.TOPIC_ID, topicId).queryList();

                    // Log if no questions available for the topic
                    if (UtilValidate.isEmpty(topicQuestions)) {
                        Debug.log("No questions available for topic");
                    }

                    Random rd = new Random();
                    List<GenericValue> selectedQuestions = new ArrayList<>();

                    // Select random questions based on questionsPerExam
                    for (int i = 0; i < questionsPerExam; i++) {
                        int rand = rd.nextInt(topicQuestions.size());
                        selectedQuestions.add(topicQuestions.get(rand));
                        topicQuestions.remove(rand);
                    }

                    // Add the selected questions to the map if not empty
                    if (UtilValidate.isNotEmpty(selectedQuestions)) {
                        question.put(topicId, selectedQuestions);
                    }
                }
            }

            // Prepare a list of lists containing selected questions
            List<List<GenericValue>> topicQuestionsList = new ArrayList<>();
            for (Entry<String, Object> entry : question.entrySet()) {
                topicQuestionsList.add((List<GenericValue>) entry.getValue());
            }

            // Add the list of selected questions to the result map and session attribute
            if (UtilValidate.isNotEmpty(topicQuestionsList)) {
                result.put("examquestion", topicQuestionsList);
                request.getSession().setAttribute("selectedQuestions", topicQuestionsList);
            }

            return result; // Return the result map
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage()); // Log the exception message
        }

        return null; // Return null in case of an exception
    }
}