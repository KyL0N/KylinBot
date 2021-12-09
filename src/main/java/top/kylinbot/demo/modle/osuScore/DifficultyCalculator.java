package top.kylinbot.demo.modle.osuScore;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.tillerino.osuApiModel.Mods;


public interface DifficultyCalculator {
	DifficultyProperties calculate(InputStream byis, Collection<Mods> mods)
			throws UserException, IOException;

}