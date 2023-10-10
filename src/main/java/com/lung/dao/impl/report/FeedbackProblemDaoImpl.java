package com.lung.dao.impl.report;

import org.springframework.stereotype.Repository;

import com.lung.dao.report.IFeedbackProblemDao;
import com.lung.model.problem.FeedbackProblem;
import com.lung.mongo.dao.GeneralReportDaoImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class FeedbackProblemDaoImpl extends GeneralReportDaoImpl<FeedbackProblem> implements IFeedbackProblemDao {
	
	@Override
	protected Class<FeedbackProblem> getEntityClass() {
		return FeedbackProblem.class;
	}
	
	@Override
	public FeedbackProblem add(FeedbackProblem feedbackProblem) {
		try {
			this.getMongoTemplate().insert(feedbackProblem);
			return feedbackProblem;
		} catch (Exception e) {
			log.error("出错啦，错误信息为:{}", e.getMessage());
			return null;
		}
	}

}
