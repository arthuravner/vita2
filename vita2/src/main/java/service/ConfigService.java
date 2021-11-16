package service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import dao.ConfigDAO;
import model.Config;

@Stateless
public class ConfigService {

	@EJB
	private ConfigDAO dao;
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Config getConfig (String chave) throws Exception{
		return this.dao.getConfig(chave);
	}
}
