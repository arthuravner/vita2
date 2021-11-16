package service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import dao.AvaliacaoDAO;
import model.Auth;
import model.Avaliacao;
import model.filtro.FiltroAvaliacao;

@Stateless
public class AvaliacaoService {

	@EJB
	private AvaliacaoDAO dao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Avaliacao> getAvaliacaoes(Auth auth, FiltroAvaliacao filtro) throws Exception {
		return dao.getAvaliacoes(auth, filtro);
	}
}
