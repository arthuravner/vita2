package bean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import jsf.JSFUtil;
import model.Avaliacao;
import model.LinkAvaliacao;
import model.Turma;
import model.filtro.FiltroAvaliacao;
import model.filtro.FiltroLink;
import model.filtro.FiltroTurma;
import service.AvaliacaoService;
import service.LinkAvaliacaoService;
import service.TurmaService;

@Named(value = "satisfacaoBean")
@ViewScoped
public class SatisfacaoBean extends AbstractBean {
	private static final long serialVersionUID = 1L;

	private List<Turma> turmas;

	private List<LinkAvaliacao> links;

	private FiltroAvaliacao filtro;

	private List<Avaliacao> avaliacoes;

	private Avaliacao avaliacaoSelecionada;

	@EJB
	private AvaliacaoService service;

	@EJB
	private TurmaService turmaService;

	@EJB
	private LinkAvaliacaoService linkService;

	@PostConstruct
	public void postConstruct() {
		try {
			this.filtro = new FiltroAvaliacao();
			avaliacoes = new ArrayList<Avaliacao>();

			FiltroTurma filtroTurma = new FiltroTurma();
			filtroTurma.setProfessorId(this.getAuth().getUsuario().getId());
			this.turmas = this.turmaService.getTurmas(filtroTurma);
		} catch (Exception e) {
			JSFUtil.mensagemErro(null, e.getMessage(), null);
		}
	}

	public void aplicarFiltro() {
		try {
			if (filtro.getLinkId() == null) {
				JSFUtil.mensagemErro(null, "Favor selecionar um link de pesquisa de avaliação", null);
				return;
			}
			this.filtro.setProfessorId(this.getAuth().getUsuario().getId());
			this.avaliacoes = this.service.getAvaliacaoes(this.getAuth(), filtro);
		} catch (Exception e) {
			JSFUtil.mensagemErro("messages", e.getMessage(), null);
		}
	}

	public void viewAction() {
		try {
			final String stringId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
					.get("x");
			if (StringUtils.isNotEmpty(stringId)) {
				try {
					final Integer id = Integer.parseInt(stringId);
					this.filtro = new FiltroAvaliacao();
					filtro.setLinkId(id);
				} catch (Exception e) {
					throw new Exception("Parâmetro inválido");
				}
				filtro.setProfessorId(this.getAuth().getUsuario().getId());
				this.avaliacoes = this.service.getAvaliacaoes(this.getAuth(), filtro);
			}
		} catch (Exception e) {
			JSFUtil.mensagemErro(null, e.getMessage(), null);
		}
	}

	public void onSelectTurma() {
		try {
			FiltroLink filtroLink = new FiltroLink();
			filtroLink.setTurmaId(this.filtro.getTurmaId());
			this.links = linkService.getLinks(filtroLink);
		} catch (Exception e) {
			JSFUtil.mensagemErro(null, e.getMessage(), null);
		}
	}

	public FiltroAvaliacao getFiltro() {
		return filtro;
	}

	public List<Avaliacao> getAvaliacoes() {
		return avaliacoes;
	}

	public Avaliacao getAvaliacaoSelecionada() {
		return avaliacaoSelecionada;
	}

	public void setAvaliacaoSelecionada(Avaliacao avaliacaoSelecionada) {
		this.avaliacaoSelecionada = avaliacaoSelecionada;
	}

	public List<Turma> getTurmas() {
		return turmas;
	}

	public List<LinkAvaliacao> getLinks() {
		return links;
	}
}
