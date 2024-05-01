package com.exam.forms;

import org.hibernate.validator.constraints.NotEmpty;

import com.exam.forms.checks.ExamMasterCheck;
import com.exam.forms.checks.ExamTopicMappingCheck;
import com.exam.forms.checks.QuestionMasterCheck;
import com.exam.forms.checks.TopicMasterCheck;

public class HibernateValidationMaster {


	@NotEmpty(message = "TopicName should not be empty", groups = { TopicMasterCheck.class })
	private String topicName;

	@NotEmpty(message = "ExamName should not be empty", groups = { ExamMasterCheck.class })
	private String examName;

	@NotEmpty(message = "Description should not be empty", groups = { ExamMasterCheck.class })
	private String description;

	@NotEmpty(message = "CreationDate should not be empty", groups = { ExamMasterCheck.class })
	private String creationDate;

	@NotEmpty(message = "ExpirationDate should not be empty", groups = { ExamMasterCheck.class })
	private String expirationDate;

	@NotEmpty(message = "NoOfQuestions should not be empty", groups = { ExamMasterCheck.class })
	private String noOfQuestions;

	@NotEmpty(message = "DurationMinutes should not be empty", groups = { ExamMasterCheck.class })
	private String durationMinutes;

	@NotEmpty(message = "PassPercentage should not be empty", groups = { ExamMasterCheck.class })
	private String passPercentage;

	@NotEmpty(message = "QuestionsRandomized should not be empty", groups = { ExamMasterCheck.class })
	private String questionsRandomized;

	@NotEmpty(message = "AnswersMust should not be empty", groups = { ExamMasterCheck.class })
	private String answersMust;

	@NotEmpty(message = "EnableNegativeMark should not be empty", groups = { ExamMasterCheck.class })
	private String enableNegativeMark;

	@NotEmpty(message = "NegativeMarkValue should not be empty", groups = { ExamMasterCheck.class,
			QuestionMasterCheck.class })
	private String negativeMarkValue;
	
	@NotEmpty(message = "questionDetail should not be empty", groups = { QuestionMasterCheck.class })
	private String questionDetail;

	@NotEmpty(message = "answer should not be empty", groups = { QuestionMasterCheck.class })
	private String answer;

	@NotEmpty(message = "numAnswers should not be empty", groups = { QuestionMasterCheck.class })
	private String numAnswers;

	@NotEmpty(message = "questionType should not be empty", groups = { QuestionMasterCheck.class })
	private String questionType;

	@NotEmpty(message = "difficultyLevel should not be empty", groups = { QuestionMasterCheck.class })
	private String difficultyLevel;

	@NotEmpty(message = "answerValue should not be empty", groups = { QuestionMasterCheck.class })
	private String answerValue;

	@NotEmpty(message = "TopicId should not be empty", groups = { QuestionMasterCheck.class,
			ExamTopicMappingCheck.class })
	private String topicId;

	@NotEmpty(message = "ExamID should not be empty", groups = { ExamTopicMappingCheck.class })
	private String examId;

	@NotEmpty(message = "Percentage should not be empty", groups = { ExamTopicMappingCheck.class })
	private String percentage;

	@NotEmpty(message = "QuestionsPerExam should not be empty", groups = { ExamTopicMappingCheck.class })
	private String questionsPerExam;

	public String getQuestionDetail() {
		return questionDetail;
	}

	public void setQuestionDetail(String questionDetail) {
		this.questionDetail = questionDetail;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getNumAnswers() {
		return numAnswers;
	}

	public void setNumAnswers(String numAnswers) {
		this.numAnswers = numAnswers;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public String getDifficultyLevel() {
		return difficultyLevel;
	}

	public void setDifficultyLevel(String difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}

	public String getAnswerValue() {
		return answerValue;
	}

	public void setAnswerValue(String answerValue) {
		this.answerValue = answerValue;
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	
	public String getQuestionsPerExam() {
		return questionsPerExam;
	}

	public void setQuestionsPerExam(String questionsPerExam) {
		this.questionsPerExam = questionsPerExam;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	public String getTopicPassPercentage() {
		return topicPassPercentage;
	}

	public void setTopicPassPercentage(String topicPassPercentage) {
		this.topicPassPercentage = topicPassPercentage;
	}

	@NotEmpty(message = "TopicPassPercentage should not be empty", groups = { ExamTopicMappingCheck.class })
	private String topicPassPercentage;

	public String getExamId() {
		return examId;
	}

	public void setExamId(String examId) {
		this.examId = examId;
	}

	public String getExamName() {
		return examName;
	}

	public void setExamName(String examName) {
		this.examName = examName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getNoOfQuestions() {
		return noOfQuestions;
	}

	public void setNoOfQuestions(String noOfQuestions) {
		this.noOfQuestions = noOfQuestions;
	}

	public String getDurationMinutes() {
		return durationMinutes;
	}

	public void setDurationMinutes(String durationMinutes) {
		this.durationMinutes = durationMinutes;
	}

	public String getPassPercentage() {
		return passPercentage;
	}

	public void setPassPercentage(String passPercentage) {
		this.passPercentage = passPercentage;
	}

	public String getQuestionsRandomized() {
		return questionsRandomized;
	}

	public void setQuestionsRandomized(String questionsRandomized) {
		this.questionsRandomized = questionsRandomized;
	}

	public String getAnswersMust() {
		return answersMust;
	}

	public void setAnswersMust(String answersMust) {
		this.answersMust = answersMust;
	}

	public String getEnableNegativeMark() {
		return enableNegativeMark;
	}

	public void setEnableNegativeMark(String enableNegativeMark) {
		this.enableNegativeMark = enableNegativeMark;
	}

	public String getNegativeMarkValue() {
		return negativeMarkValue;
	}

	public void setNegativeMarkValue(String negativeMarkValue) {
		this.negativeMarkValue = negativeMarkValue;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
}
