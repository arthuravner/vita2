package dao;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import model.LinkAvaliacao;
import model.filtro.FiltroLink;

@Stateless
@LocalBean
public class LinkAvaliacaoDAO extends AbstractVitaDAO {

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<LinkAvaliacao> getLinks(FiltroLink filtro) throws Exception {

		CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<LinkAvaliacao> query = builder.createQuery(LinkAvaliacao.class);
		Root<LinkAvaliacao> root = query.from(LinkAvaliacao.class);
		root.fetch("turma");	
		root.fetch("avaliacoes", JoinType.LEFT);
		
		Predicate where = null;
		
		if(filtro.getTurmaId() == null && StringUtils.isEmpty(filtro.getHash())) {
			return null;
		}

		if (filtro.getTurmaId() != null) {
			where = builder.equal(root.get("turma"), filtro.getTurmaId());
		} else {
			where = builder.equal(root.get("hashlink"), filtro.getHash());
		}

		if (filtro.getDataValidade() != null) {
			where = builder.and(where,
					builder.greaterThanOrEqualTo(root.get("dataValidadeLink"), filtro.getDataValidade()));
		}

		if (StringUtils.isNotEmpty(filtro.getDescricao())) {
			where = builder.and(where, builder.like(root.get("descricao"), "%" + filtro.getDescricao() + "%"));
		}

		if (where != null) {
			query.where(where);
			query.orderBy(builder.desc(root.get("id")));
			query.select(root);
			return this.getEntityManager().createQuery(query).setMaxResults(100).getResultList();
		} else {
			return null;
		}

	}

}
