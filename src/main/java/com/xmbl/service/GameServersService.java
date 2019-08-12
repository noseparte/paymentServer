package com.xmbl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmbl.dao.IGameServersDao;
import com.xmbl.model.GameServers;

@Service
public class GameServersService{
	
	@Autowired 
	private IGameServersDao gameServerDao;
	
	public List<GameServers> getServerListByTypeAndOperatorStatus(String type, Integer operatorStatus) {
		List<GameServers> gameServerLst = gameServerDao.getServerListByTypeAndOperatorStatus(type,operatorStatus);
		return gameServerLst;
	}
	
	public GameServers getServerByServerIdAndTypeAndOperatorStatus(Long serverId, String type, Integer operatorStatus) {
		GameServers gameServers = gameServerDao.getServerByServerIdAndTypeAndOperatorStatus(serverId,type,operatorStatus);
		return gameServers;
	}

}
