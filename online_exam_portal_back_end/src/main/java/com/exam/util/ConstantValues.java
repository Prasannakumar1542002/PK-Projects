package com.exam.util;

public class ConstantValues {
//	Enumeration Entity
	public static final String ENUM_ID = "enumId";
	public static final String ENUM_SEQUENCE_ID = "sequenceId";
	public static final String ENUM_TYPE_ID = "enumTypeId";
	public static final String ENUM_DESCRIPTION = "description";

//	TopicMaster Entity
	public static final String TOPIC_ID = "topicId";
	public static final String TOPIC_NAME = "topicName";

//	ExamMaster Entity
	public static final String EXAM_ID = "examId";
	public static final String EXAM_NAME = "examName";
	public static final String EXAM_DESCRIPTION = "description";
	public static final String EXAM_CREATION_DATE = "creationDate";
	public static final String EXAM_EXPIRATION_DATE = "expirationDate";
	public static final String EXAM_TOTAL_QUES = "noOfQuestions";
	public static final String EXAM_DURATION = "durationMinutes";
	public static final String EXAM_PASS_PERCENTAGE = "passPercentage";
	public static final String EXAM_QUES_RANDOMIZED = "questionsRandomized";
	public static final String EXAM_ANS_MUST = "answersMust";
	public static final String EXAM_ENABLE_NEG_MARK = "enableNegativeMark";
	public static final String EXAM_NEG_MARK = "negativeMarkValue";

// QuestionMaster Entity
	public static final String QUES_ID = "questionId";
	public static final String QUES_DETAIL = "questionDetail";
	public static final String QUES_OPTION_A = "optionA";
	public static final String QUES_OPTION_B = "optionB";
	public static final String QUES_OPTION_C = "optionC";
	public static final String QUES_OPTION_D = "optionD";
	public static final String QUES_OPTION_E = "optionE";
	public static final String QUES_ANSWER = "answer";
	public static final String QUES_NUM_ANS = "numAnswers";
	public static final String QUES_TYPE = "questionType";
	public static final String QUES_DIFFICULTY_LEVEL = "difficultyLevel";
	public static final String QUES_ANS_VALUE = "answerValue";
	public static final String QUES_TOPIC_ID = "topicId";
	public static final String QUES_NEG_MARK = "negativeMarkValue";

// ExamTopicMapping Entity
	public static final String EXAMTOPIC_EXAM_ID = "examId";
	public static final String EXAMTOPIC_TOPIC_ID = "topicId";
	public static final String EXAMTOPIC_PERCENTAGE = "percentage";
	public static final String EXAMTOPIC_TOPIC_PASS_PERCENTAGE = "topicPassPercentage";
	public static final String EXAMTOPIC_QUES_PER_EXAM = "questionsPerExam";

// UserAttemptMaster Entity
	public static final String USER_ATTEMPT_PERFORMANCE_ID = "performanceId";
	public static final String USER_ATTEMPT_NUMBER = "attemptNumber";
	public static final String USER_ATTEMPT_PARTY_ID = "partyId";
	public static final String USER_ATTEMPT_EXAM_ID = "examId";
	public static final String USER_ATTEMPT_SCORE = "score";
	public static final String USER_ATTEMPT_COMPLETED_DATE = "completedDate";
	public static final String USER_ATTEMPT_TOTAL_QUES = "noOfQuestions";
	public static final String USER_ATTEMPT_TOTAL_CORRECT = "totalCorrect";
	public static final String USER_ATTEMPT_TOTAL_WRONG = "totalWrong";
	public static final String USER_ATTEMPT_PASSED = "userPassed";

// UserAttemptTopicMaster Entity
	public static final String USER_TOPIC_PERFORMANCE_ID = "performanceId";
	public static final String USER_TOPIC_TOPIC_ID = "topicId";
	public static final String USER_TOPIC_PASS_PERCENTAGE = "topicPassPercentage";
	public static final String USER_TOPIC_TOTAL_QUES = "totalQuestionsInThisTopic";
	public static final String USER_TOPIC_CRCT_QUES = "correctQuestionsInThisTopic";
	public static final String USER_TOPIC_PERCENTAGE = "userTopicPercentage";
	public static final String USER_TOPIC_PASSED = "userPassedThisTopic";

// UserAttemptAnswerMaster Entity
	public static final String USER_ANSWER_PERFORMANCE_ID = "performanceId";
	public static final String USER_ANSWER_QUESTION_ID = "questionId";
	public static final String USER_ANSWER_SEQUENCE_ID = "sequenceNum";
	public static final String USER_ANSWER_SUBMITTED = "submittedAnswer";
	public static final String USER_ANSWER_FLAGGED = "isFlagged";
	
// UserExamMapping Entity
	public static final String USEREXAM_PARTY_ID = "partyId";
	public static final String USEREXAM_EXAM_ID = "examId";
	public static final String USEREXAM_ALLOWED_ATTEMPTS = "allowedAttempts";
	public static final String USEREXAM_NO_OF_ATTEMPTS = "noOfAttempts";
	public static final String USEREXAM_LAST_DATE = "lastPerformanceDate";
	public static final String USEREXAM_TIMEOUT_DAYS = "timeoutDays";
	public static final String USEREXAM_PASSWORD_CHANGE = "passwordChangesAuto";
	public static final String USEREXAM_SPLIT = "canSplitExams";
	public static final String USEREXAM_SEE_RESULT = "canSeeDetailedResults";
	public static final String USEREXAM_MAX_SPLIT = "maxSplitAttempts";
}
