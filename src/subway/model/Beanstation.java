package subway.model;

import java.util.ArrayList;
import java.util.List;

public class Beanstation {
	private String stationname;//վ����
	private List<String> stations=new ArrayList<String>();//ĳ��·�е�վ��
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
