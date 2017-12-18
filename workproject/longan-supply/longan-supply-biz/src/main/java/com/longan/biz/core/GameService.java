package com.longan.biz.core;

import java.util.List;

import com.longan.biz.dataobject.Game;
import com.longan.biz.dataobject.GameQuery;
import com.longan.biz.domain.Result;

public interface GameService {
	public Result<List<Game>> queryGameList(GameQuery gameQuery);

	public Result<Boolean> createGame(Game Game);

	public Result<Game> getGame(Long id);

	public Result<Boolean> updateGame(Game game);

	public Result<List<Game>> queryAllGame(GameQuery gameQuery);
}
