package ninjabrainbot.model.datastate;

import ninjabrainbot.model.datastate.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.model.datastate.blind.BlindResult;
import ninjabrainbot.model.datastate.calculator.ICalculatorResult;
import ninjabrainbot.model.datastate.common.IPlayerPosition;
import ninjabrainbot.model.datastate.common.ResultType;
import ninjabrainbot.model.datastate.divine.DivineResult;
import ninjabrainbot.model.datastate.divine.IDivineContext;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;
import ninjabrainbot.model.datastate.highprecision.IBoatDataState;
import ninjabrainbot.model.datastate.homeportal.HomePortalResult;
import ninjabrainbot.model.datastate.homeportal.IHomePortalContext;
import ninjabrainbot.model.datastate.stronghold.ChunkPrediction;
import ninjabrainbot.model.domainmodel.IDataComponent;
import ninjabrainbot.model.domainmodel.IDomainModelComponent;
import ninjabrainbot.model.domainmodel.IListComponent;

public interface IDataState {

	IAllAdvancementsDataState allAdvancementsDataState();

	IBoatDataState boatDataState();

	IDivineContext getDivineContext();
	
	IHomePortalContext getHomePortalContext();

	IListComponent<IEnderEyeThrow> getThrowList();

	IDataComponent<IPlayerPosition> playerPosition();

	IDataComponent<Boolean> locked();

	IDomainModelComponent<ICalculatorResult> calculatorResult();

	IDomainModelComponent<ChunkPrediction> topPrediction();

	IDomainModelComponent<BlindResult> blindResult();

	IDomainModelComponent<DivineResult> divineResult();
	
	IDomainModelComponent<HomePortalResult> homePortalResult();

	IDomainModelComponent<ResultType> resultType();

	default double getBestCertainty() {
		ICalculatorResult calculatorResult = calculatorResult().get();
		return calculatorResult != null && calculatorResult.success() ? calculatorResult.getBestPrediction().chunk.weight : 0;
	}

}
