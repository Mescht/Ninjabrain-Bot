package ninjabrainbot.model.datastate.calculator;

import ninjabrainbot.model.datastate.blind.BlindPosition;
import ninjabrainbot.model.datastate.blind.BlindResult;
import ninjabrainbot.model.datastate.common.IPlayerPosition;
import ninjabrainbot.model.datastate.divine.DivineResult;
import ninjabrainbot.model.datastate.divine.IDivineContext;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;
import ninjabrainbot.model.datastate.homeportal.HomePortalResult;
import ninjabrainbot.model.datastate.homeportal.IHomePortalContext;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.IReadOnlyList;

public interface ICalculator {

	ICalculatorResult triangulate(IReadOnlyList<IEnderEyeThrow> eyeThrows, IObservable<IPlayerPosition> playerPos, IDivineContext divineContext);

	BlindResult blind(BlindPosition b, IDivineContext divineContext);

	DivineResult divine(IDivineContext divineContext);
	
	//HomePortalResult homePortal(HomePortalPosition h);
	HomePortalResult homePortal(IPlayerPosition pos, IHomePortalContext homePortalContext);
	
}
