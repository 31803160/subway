package subway.model;

import java.util.ArrayList;
import java.util.List;

public class Beanstation {
	private String stationname;//站点名
	private List<String> stations=new ArrayList<String>();//某线路中的站点
	public String getStationname() {
		return stationname;
	}
	public void setStationname(String stationname) {
		this.stationname = stationname;
	}
	public List<String> getStations() {
		return stations;
	}
	public void setStations(List<String> stations) {
		this.stations = stations;
	}


	
}
