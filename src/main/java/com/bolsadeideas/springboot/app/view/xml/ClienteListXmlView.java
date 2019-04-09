package com.bolsadeideas.springboot.app.view.xml;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.xml.MarshallingView;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
/**
 * el view resolver sabe como resolver esta vista xml
 * @author carlosmarin
 *
 */
@Component("listar.xml")
public class ClienteListXmlView extends MarshallingView {

	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Page<Cliente> clientes = (Page<Cliente>) model.get("clientes");
		
		model.remove("titulo");
		model.remove("page");
		model.remove("clientes");
		
		model.put("clientList", new ClienteList(clientes.getContent()));
		
		super.renderMergedOutputModel(model, request, response);
	}
	/**
	 * el Marshaller es la interfaz y nosotros pasamos la implementacion
	 * @param marshaller
	 */
	@Autowired
	public ClienteListXmlView(Jaxb2Marshaller marshaller) {
		super(marshaller);
	}

	
}
