package service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import dao.LinkAvaliacaoDAO;
import model.Avaliacao;
import model.LinkAvaliacao;
import model.azure.RetornoAzure;
import model.filtro.FiltroLink;
import service.external.APIAnaliseDeTexto;

@Stateless
public class LinkAvaliacaoService {

	@EJB
	private LinkAvaliacaoDAO dao;

	@EJB
	private APIAnaliseDeTexto apiAzure;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<LinkAvaliacao> getLinks(FiltroLink filtro) throws Exception {
		List<LinkAvaliacao> links = dao.getLinks(filtro);
		return links;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void save(Avaliacao avaliacao) throws Exception {

		Avaliacao avaliacaoEntity = this.dao.save(avaliacao);

		if (avaliacaoEntity != null) {
			RetornoAzure retornoAzure = this.apiAzure.analiseDeSentimento(avaliacao.getTexto());
			if (retornoAzure != null) {
				retornoAzure.calculaScoreNormalizado();
				retornoAzure.setAvaliacao(avaliacaoEntity);
				this.dao.save(retornoAzure);
			}
		}
	}

}
