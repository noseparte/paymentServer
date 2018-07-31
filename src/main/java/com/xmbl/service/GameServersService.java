package com.xmbl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xmbl.dao.IGameServersDao;
import com.xmbl.model.GameServers;

@Repository
public class GameServersService {

	@Autowired
	private IGameServersDao gameServerDao;
	
	public List<GameServers> getServerListByTypeAndOperatorStatus(String type, Integer operatorStatus) {
		List<GameServers> gameServerLst = gameServerDao.getServerListByTypeAndOperatorStatus(type,operatorStatus);
		return gameServerLst;
	}
	
}