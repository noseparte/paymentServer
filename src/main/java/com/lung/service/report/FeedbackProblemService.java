package com.lung.service.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lung.dao.report.IFeedbackProblemDao;
import com.lung.model.problem.FeedbackProblem;

@Service
@Transactional
public class FeedbackProblemService{

	@Autowired
	private IFeedbackProblemDao iFeedbackProblemDao;
	
	public FeedbackProblem add(FeedbackProblem feedbackProblem) {
		feedbackProblem = iFeedbackProblemDao.add(feedbackProblem);
		return feedbackProblem;
	}

}
