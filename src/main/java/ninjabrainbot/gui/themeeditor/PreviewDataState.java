package ninjabrainbot.gui.themeeditor;

import java.util.List;

import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.model.datastate.blind.BlindResult;
import ninjabrainbot.model.datastate.calculator.ICalculatorResult;
import ninjabrainbot.model.datastate.common.IPlayerPosition;
import ninjabrainbot.model.datastate.common.ResultType;
import ninjabrainbot.model.datastate.divine.DivineContext;
import ninjabrainbot.model.datastate.divine.DivineResult;
import ninjabrainbot.model.datastate.divine.Fossil;
import ninjabrainbot.model.datastate.divine.IDivineContext;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;
import ninjabrainbot.model.datastate.highprecision.BoatDataState;
import ninjabrainbot.model.datastate.highprecision.IBoatDataState;
import ninjabrainbot.model.datastate.homeportal.HomePortalContext;
import ninjabrainbot.model.datastate.homeportal.HomePortalResult;
import ninjabrainbot.model.datastate.homeportal.IHomePortalContext;
import ninjabrainbot.model.datastate.stronghold.ChunkPrediction;
import ninjabrainbot.model.domainmodel.DataComponent;
import ninjabrainbot.model.domainmodel.IDataComponent;
import ninjabrainbot.model.domainmodel.IDomainModelComponent;
import ninjabrainbot.model.domainmodel.IListComponent;
import ninjabrainbot.model.domainmodel.ListComponent;

public class PreviewDataState implements IDataState {

	private final IBoatDataState boatDataState;
	private final IAllAdvancementsDataState allAdvancementsDataState;

	private final DivineContext divineContext;
	private final ListComponent<IEnderEyeThrow> throwSet;
	private final DataComponent<Boolean> locked;
	private final DataComponent<IPlayerPosition> playerPosition;

	private final DataComponent<ResultType> resultType;
	private final DataComponent<ICalculatorResult> calculatorResult;
	private final DataComponent<ChunkPrediction> topPrediction;
	private final DataComponent<BlindResult> blindResult;
	private final DataComponent<DivineResult> divineResult;
	private final DataComponent<HomePortalResult> homePortalResult;
	private final IHomePortalContext homePortalContext;

	public PreviewDataState(ICalculatorResult result, List<IEnderEyeThrow> eyeThrows, Fossil f) {
		this();
		calculatorResult.set(result);
		topPrediction.set(result.getBestPrediction());
		for (IEnderEyeThrow t : eyeThrows) {
			throwSet.add(t);
		}
		divineContext.fossil.set(f);
	}

	public PreviewDataState() {
		divineContext = new DivineContext(null);
		throwSet = new ListComponent<>(null, 10);
		playerPosition = new DataComponent<>(null);
		locked = new DataComponent<>(null, false);
		resultType = new DataComponent<>(null, ResultType.NONE);
		calculatorResult = new DataComponent<>(null);
		topPrediction = new DataComponent<>(null);
		blindResult = new DataComponent<>(null);
		divineResult = new DataComponent<>(null);
		homePortalResult = new DataComponent<>(null);
		
		homePortalContext = new HomePortalContext(null, false);
		boatDataState = new BoatDataState(null);
		allAdvancementsDataState = new PreviewAllAdvancementsDataState();
	}

	@Override
	public IDivineContext getDivineContext() {
		return divineContext;
	}

	@Override
	public IListComponent<IEnderEyeThrow> getThrowList() {
		return throwSet;
	}

	@Override
	public IDataComponent<IPlayerPosition> playerPosition() {
		return playerPosition;
	}

	@Override
	public IDataComponent<Boolean> locked() {
		return locked;
	}

	@Override
	public IDomainModelComponent<ICalculatorResult> calculatorResult() {
		return calculatorResult;
	}

	@Override
	public IDomainModelComponent<ChunkPrediction> topPrediction() {
		return topPrediction;
	}

	@Override
	public IDomainModelComponent<BlindResult> blindResult() {
		return blindResult;
	}

	@Override
	public IDomainModelComponent<DivineResult> divineResult() {
		return divineResult;
	}
	
	@Override
	public IDomainModelComponent<HomePortalResult> homePortalResult() {
		return homePortalResult;
	}

	@Override
	public IDomainModelComponent<ResultType> resultType() {
		return resultType;
	}

	@Override
	public IAllAdvancementsDataState allAdvancementsDataState() {
		return allAdvancementsDataState;
	}

	@Override
	public IBoatDataState boatDataState() {
		return boatDataState;
	}

	@Override
	public IHomePortalContext getHomePortalContext() {
		return homePortalContext;
	}

}
