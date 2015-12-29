/**
 * Initial software, Pierre-Guillaume Raverdy,
 * 
 * Copyright, Ambientic 2014
 */

package happiness.mimove.inria.fr.rescomexample.crowdsourcing;

import android.location.Address;

import com.ambientic.crowdsource.data.DoExpose;
import com.ambientic.crowdsource.data.GoFlowLocation;

/**
 * Station
 * 
 * @author khoo
 * 
 */
public class Station extends GoFlowLocation {
	@DoExpose
	public static final Integer CLASS_VERSION = new Integer(4);

	protected double latitude;
	protected double longitude;
	protected String name = null;

    //
    protected String[] address = null;
    protected String ctryCode = null;
    protected String ctry = null;
    protected String zip = null;
    protected String city = null;

    public Station() {
		super();
	}

	/**
	 * Station constructor
	 * 
	 * @param id
	 *          Station id
	 * @param name
	 *          Station name
	 * @param latitude
	 *          Station latitude
	 * @param longitude
	 *          Station longitude
	 */
	public Station(String id, double latitude, double longitude) {
		super();
		super.setId(id);

		this.latitude = latitude;
		this.longitude = longitude;
	}

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(Address address) {
        try {
            if (address != null) {
                this.address = new String[address.getMaxAddressLineIndex() + 1];
                for (int x = 0; x <= address.getMaxAddressLineIndex(); x++) {
                    this.address[x] = new String(address.getAddressLine(x));
                }
                this.ctry = address.getCountryName();
                this.ctryCode = address.getCountryCode();
                this.city = address.getLocality();
                this.zip = address.getPostalCode();

                this.latitude = address.getLatitude();
                this.longitude = address.getLongitude();
            } else {
                this.address = null;
                this.ctry = null;
                this.ctryCode = null;
                this.city = null;
                this.zip = null;
                this.latitude = 0;
                this.longitude = 0;
            }
        } catch (Exception e) {
        }
    }


    /**
	 * Get station name
	 * 
	 * @return station name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get station latitude
	 * 
	 * @return station latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Get station longitude
	 * 
	 * @return
	 */
	public double getLongitude() {
		return longitude;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Id: " + id + "\n");
		sb.append("Name: " + name + "\n");
		sb.append("Latitude: " + String.valueOf(latitude) + "\n");
		sb.append("Longitude: " + String.valueOf(longitude) + "\n");
		return sb.toString();
	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof Station))
			return false;
		Station station = (Station) obj;

		if (id == null) {
			if (station.id != null)
				return false;
		} else if (!id.equals(station.id))
			return false;

		if (name == null) {
			if (station.name != null)
				return false;
		} else if (!name.equals(station.name))
			return false;

		if (latitude != station.latitude)
			return false;

		if (longitude != station.longitude)
			return false;

		return true;
	}
}
