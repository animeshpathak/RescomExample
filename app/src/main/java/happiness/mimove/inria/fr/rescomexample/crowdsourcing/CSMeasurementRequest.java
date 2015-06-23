package happiness.mimove.inria.fr.rescomexample.crowdsourcing;

import com.ambientic.crowdsource.data.GoFlowRequest;


import happiness.mimove.inria.fr.rescomexample.models.Mood;

public class CSMeasurementRequest extends GoFlowRequest<Station, Mood> {

	private Station subject;

	public CSMeasurementRequest(Station station) {
        super(station, Mood.class);
	}

	public Station getStation() {
		return subject;
	}

	public void setStation(Station station) {
		this.subject = station;
	}

	//
	// CrowdSourcedRequest
	//

	@Override
	public String getLocationId() {
		return subject.getId();
	}

}
