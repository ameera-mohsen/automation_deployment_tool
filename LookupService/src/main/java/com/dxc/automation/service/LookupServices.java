package com.dxc.automation.service;

import java.util.List;
import java.util.Map;

import com.dxc.automation.entity.Enviroment;
import com.dxc.automation.entity.Layer;
import com.dxc.automation.entity.Service;
import com.dxc.automation.entity.Status;

public interface LookupServices {

	public List<Service> listServices();

	public List<Layer> listLayers();

	public List<Enviroment> listEnviroments();

	public List<Status> listStatus();

	public Map<String, String> listLookup(String name);

}
