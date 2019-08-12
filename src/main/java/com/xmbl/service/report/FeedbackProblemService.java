package com.xmbl.service.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xmbl.dao.report.IFeedbackProblemDao;
import com.xmbl.model.problem.FeedbackProblem;

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
