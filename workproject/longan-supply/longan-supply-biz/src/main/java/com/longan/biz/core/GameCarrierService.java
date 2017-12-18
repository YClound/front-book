package com.longan.biz.core;

import java.util.List;

import com.longan.biz.dataobject.GameCarrier;
import com.longan.biz.dataobject.GameCarrierQuery;
import com.longan.biz.domain.Result;

public interface GameCarrierService {
	public Result<List<GameCarrier>> queryGameCarrierList(GameCarrierQuery gameCarrierQuery);

	public Result<Boolean> createGameCarrier(GameCarrier gameCarrier);

	public Result<GameCarrier> getGameCarrier(Long id);

	public Result<Boolean> updateGameCarrier(GameCarrier gameCarrier);
	
	public Result<List<GameCarrier>> queryAllGameCarrier(GameCarrierQuery gameCarrierQuery);
}
