package subway.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import subway.model.Beanstation;

public class readtxt {
	public List<Beanstation> txt(String data) throws IOException{
	List<Beanstation> lines=new ArrayList<Beanstation>();//存取所有线路
	String fg=" ";//空格是分隔符
	File file=new File(data);
	InputStreamReader read = new InputStreamReader(new FileInputStream(file));
	BufferedReader bufferedReader = new BufferedReader(read);
	String Line1 = null;
	//读文件
	while((Line1 = bufferedReader.readLine()) != null){       
	// 字符串分隔
	Beanstation station=new Beanstation();
	String route="";	
	String tmp[] = Line1.split(fg);
	route=tmp[0];
	station.setStationname(route);
	List<String> stations=new ArrayList<>();//站点集合
	for(String s:tmp) stations.add(s);
	stations.remove(0);
	station.setStations(stations);
	lines.add(station);//在该线路中加站点
	}
	return lines;
	}
}
