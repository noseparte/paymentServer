package com.xmbl.dao.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.xmbl.dao.IGameServersDao;
import com.xmbl.model.GameServers;
import com.xmbl.mongo.dao.GeneralDaoImpl;

@Repository
public class IGameServersDaoImpl extends GeneralDaoImpl<GameServers> implements IGameServersDao {

	@Override
	protected Class<GameServers> getEntityClass() {
		return GameServers.class;
	}

	@Override
	public List<GameServers> getServerListByTypeAndOperatorStatus(String type, Integer operatorStatus) {
		try {
			Query query = new Query();
			Criteria criteria = new Criteria();
			criteria = criteria.and("type").is(type);
			criteria = criteria.and("operatorStatus").is(operatorStatus);
			query.addCriteria(criteria);
			List<GameServers> gameServerLst = getMongoTemplate().find(query, GameServers.class, "gameservers");
			return gameServerLst;
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@Override
	public GameServers getServerByServerIdAndTypeAndOperatorStatus(Long serverId, String type, Integer operatorStatus) {
		Query query = new Query();
		Criteria criteria = new Criteria();
		criteria = criteria.and("serverId").is(serverId);
		criteria = criteria.and("type").is(type);
		criteria = criteria.and("operatorStatus").is(operatorStatus);
		query.addCriteria(criteria);
		GameServers gameServers = getMongoTemplate().findOne(query, GameServers.class, "gameservers");
		return gameServers;
	}

}
