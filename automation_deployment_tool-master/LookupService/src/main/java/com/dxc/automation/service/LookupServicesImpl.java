package com.dxc.automation.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.dxc.automation.entity.Enviroment;
import com.dxc.automation.entity.Layer;
import com.dxc.automation.entity.Service;
import com.dxc.automation.entity.Status;

@org.springframework.stereotype.Service
public class LookupServicesImpl implements LookupServices {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<Service> listServices() {
		return mongoTemplate.findAll(Service.class);
	}

	@Override
	public List<Layer> listLayers() {
		return mongoTemplate.findAll(Layer.class);
	}

	@Override
	public List<Status> listStatus() {
		return mongoTemplate.findAll(Status.class);
	}

	@Override
	public List<Enviroment> listEnviroments() {
		List<Enviroment> list = mongoTemplate.findAll(Enviroment.class);
		list.forEach(person -> person.setEnvDetails(""));
		list.stream().filter(p -> p.getEnvName().equals("s")).map(envi -> listEnviroments().add(envi));
		return mongoTemplate.findAll(Enviroment.class);
	}

	public Map<String, String> getIdName(){
		Map<String, String> map = new HashMap<>();
		listServices().forEach(serv -> map.put(serv.getId(), serv.getServiceName()));
		map.put("", "");
		return null;
	}
	
	@Override
	public Map<String, String> listLookup(String name) {
		Map<String, String> lookuplist = new HashMap<>();
		switch (name) {
		case "Enviroment":
			listEnviroments().forEach(env -> lookuplist.put(env.getId(), env.getEnvName()));
			break;

		case "Services":
			listServices().forEach(serv -> lookuplist.put(serv.getId(), serv.getServiceName()));
			break;

		case "Layers":
			listLayers().forEach(layer -> lookuplist.put(layer.getId(), layer.getLayerName()));
			break;

		case "Status":
			listStatus().forEach(status -> lookuplist.put(status.getId(), status.getStatusName()));
			break;
		}

		return lookuplist;
	}
}
