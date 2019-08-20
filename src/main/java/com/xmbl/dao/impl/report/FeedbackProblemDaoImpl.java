package com.xmbl.dao.impl.report;

import org.springframework.stereotype.Repository;

import com.xmbl.dao.report.IFeedbackProblemDao;
import com.xmbl.model.problem.FeedbackProblem;
import com.xmbl.mongo.dao.GeneralReportDaoImpl;

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
