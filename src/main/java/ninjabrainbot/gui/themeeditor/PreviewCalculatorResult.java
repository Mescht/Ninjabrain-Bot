package ninjabrainbot.gui.themeeditor;

import java.util.ArrayList;
import java.util.List;

import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.gui.themeeditor.panels.PreviewPlayerPosition;
import ninjabrainbot.io.preferences.enums.McVersion;
import ninjabrainbot.model.datastate.calculator.ICalculatorResult;
import ninjabrainbot.model.datastate.common.IPlayerPosition;
import ninjabrainbot.model.datastate.stronghold.Chunk;
import ninjabrainbot.model.datastate.stronghold.ChunkPrediction;

public class PreviewCalculatorResult implements ICalculatorResult {

	final List<ChunkPrediction> predictions = new ArrayList<>();
	final List<Chunk> chunks = new ArrayList<>();

	public PreviewCalculatorResult(McVersion version) {
		IObservable<IPlayerPosition> playerPosition = new ObservableField<>(new PreviewPlayerPosition(0, 60, 1950));
		predictions.add(createPrediction(-2, 109, 1, playerPosition, version));
		predictions.add(createPrediction(-59, 92, 0.75, playerPosition, version));
		predictions.add(createPrediction(-69, 89, 0.5, playerPosition, version));
		predictions.add(createPrediction(-49, 95, 0.25, playerPosition, version));
		predictions.add(createPrediction(-79, 86, 0, playerPosition, version));
	}

	private ChunkPrediction createPrediction(int x, int z, double certainty, IObservable<IPlayerPosition> playerPosition, McVersion version) {
		ChunkPrediction c = new ChunkPrediction(new Chunk(x, z), playerPosition, version);
		c.chunk.weight = certainty;
		return c;
	}

	@Override
	public void dispose() {
	}

	@Override
	public ChunkPrediction getBestPrediction() {
		return predictions.get(0);
	}

	@Override
	public List<ChunkPrediction> getTopPredictions() {
		return predictions;
	}

	@Override
	public boolean success() {
		return true;
	}

	@Override
	public List<Chunk> getTopChunks() {
		return chunks;
	}

}
