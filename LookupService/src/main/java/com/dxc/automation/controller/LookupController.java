package com.dxc.automation.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dxc.automation.entity.Enviroment;
import com.dxc.automation.entity.Layer;
import com.dxc.automation.entity.Service;
import com.dxc.automation.entity.Status;
import com.dxc.automation.service.LookupServices;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class LookupController {

	@Autowired
	private LookupServices lookupService;

	
	/*
	 * This Operation used to response with all lookup based on parameter, Services, Layers, Enviroments and Status
	 * REST URL: http://localhost:8080/api/common/{name}
	 * URL Params : Required: name=[String]
	 * @return is the lookup Id and Name
	 * 
	 */
	@GetMapping("/common/{name}")
	private Map<String, String> listNames(@PathVariable String name) {
		return lookupService.listLookup(name);
	}

	
	/*
	 * All the below operations used to list all lookup entity in the system
	 * REST URL: http://localhost:8089/api/Services
	 * Method: GET
	 * Response Codes: Success (200 OK), Internal Server Error (500)
	 */
	@GetMapping("/Services")
	private List<Service> findAllService() {
		return lookupService.listServices();
	}

	/*
	 * All the below operations used to list all lookup entity in the system
	 * REST URL: http://localhost:8089/api/Layers
	 * Method: GET
	 * Response Codes: Success (200 OK), Internal Server Error (500)
	 */
	@GetMapping("/Layers")
	private List<Layer> findAllLayers() {
		return lookupService.listLayers();
	}
	
	/*
	 * All the below operations used to list all lookup entity in the system
	 * REST URL: http://localhost:8089/api/Enviroments
	 * Method: GET
	 * Response Codes: Success (200 OK), Internal Server Error (500)
	 */
	@GetMapping("/Enviroments")
	private List<Enviroment> findAllEnviroments() {
		return lookupService.listEnviroments();
	}
	
	/*
	 * All the below operations used to list all lookup entity in the system
	 * REST URL: http://localhost:8089/api/Status
	 * Method: GET
	 * Response Codes: Success (200 OK), Internal Server Error (500)
	 */
	@GetMapping("/Status")
	private List<Status> findAllStatus() {
		return lookupService.listStatus();
	}

}
