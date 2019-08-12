package com.xmbl.dao;

import java.util.List;

import com.xmbl.model.GameServers;
import com.xmbl.mongo.dao.GeneralDao;

public interface IGameServersDao extends GeneralDao<GameServers>{

	List<GameServers> getServerListByTypeAndOperatorStatus(String type, Integer operatorStatus);
	
	GameServers getServerByServerIdAndTypeAndOperatorStatus(Long serverId, String type, Integer operatorStatus);
	
}
