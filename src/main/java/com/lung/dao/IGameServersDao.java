package com.lung.dao;

import java.util.List;

import com.lung.model.GameServers;
import com.lung.mongo.dao.GeneralDao;

public interface IGameServersDao extends GeneralDao<GameServers>{

	List<GameServers> getServerListByTypeAndOperatorStatus(String type, Integer operatorStatus);
	
	GameServers getServerByServerIdAndTypeAndOperatorStatus(Long serverId, String type, Integer operatorStatus);
	
}
