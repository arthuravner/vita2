package bean;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.extensions.event.ClipboardErrorEvent;
import org.primefaces.extensions.event.ClipboardSuccessEvent;

import jsf.JSFUtil;
import model.LinkAvaliacao;
import model.Turma;
import model.Usuario;
import model.filtro.FiltroLink;
import model.filtro.FiltroTurma;
import service.LinkAvaliacaoService;
import service.TurmaService;

@Named(value = "turmaBean")
@ViewScoped
public class TurmaBean extends AbstractBean {
	private static final long serialVersionUID = 1L;

	private Turma turma = new Turma();

	private final FiltroTurma filtro = new FiltroTurma();

	private FiltroLink filtroLink = new FiltroLink();

	private List<Turma> turmas;

	private List<LinkAvaliacao> links;

	private LinkAvaliacao linkSelecionado;

	@EJB
	private TurmaService service;
	
	@EJB
	private LinkAvaliacaoService linkService;

	@PostConstruct
	public void postConstruct() {
		
	}

	public void aplicarFiltro() {
		try {
			filtro.setProfessorId(this.getAuth().getUsuario().getId());
			turmas = this.service.getTurmas(this.filtro);
		} catch (Exception e) {
			JSFUtil.mensagemErro(null, e.getMessage(), null);
		}
	}

	public void onSelectTurma(Turma turma) {
		this.turma = turma;
		filtroLink = new FiltroLink();
		carregaLinks();
	}

	public void carregaLinks() {
		try {
			this.filtroLink.setTurmaId(turma.getId());
			this.links = this.linkService.getLinks(this.filtroLink);
		} catch (Exception e) {
			JSFUtil.mensagemErro("msgTurma", e.getMessage(), null);
		}

	}

	public void editaTurma(Turma turma) {
		if (turma == null) {
			this.turma = new Turma();
		} else {
			this.turma = turma;
		}
	}

	public void salvar() {
		try {
			if (this.validaTurma()) {
				Usuario usuario = this.getAuth().getUsuario();
				this.service.save(turma, usuario);
				this.aplicarFiltro();
				JSFUtil.executeClientScript("PF('dlgTurma').hide()");
			}
		} catch (Exception e) {
			JSFUtil.mensagemErro("msgTurma", e.getMessage(), null);
		}
	}

	public void excluir() {
		try {
			this.service.delete(this.turma);
			this.aplicarFiltro();
		} catch (Exception e) {
			JSFUtil.mensagemErro("messages", e.getMessage(), null);
		}
	}

	private boolean validaTurma() {

		if (StringUtils.isEmpty(this.turma.getNome())) {
			JSFUtil.mensagemErro("msgTurma", "Nome da turma deve ser preenchido", null);
			return false;
		}
		if (this.turma.getAno() == null) {
			JSFUtil.mensagemErro("msgTurma", "O ano deve ser informado", null);
			return false;
		}
		if(this.turma.getAno() < 2021) {
			JSFUtil.mensagemErro("msgTurma", "Ano inválido", null);
			return false;			
		}
		
		if (this.turma.getPeriodo() == null) {
			JSFUtil.mensagemErro("msgTurma", "O período deve ser informado", null);
			return false;
		}

		return true;
	}

	public void novoLink() {
		this.linkSelecionado = new LinkAvaliacao();
		this.linkSelecionado.setTurma(this.turma);
		this.linkSelecionado.setDataAula(new Date());
	}

	public void salvarLink() {
		try {
			if (validaLink()) {
				this.linkSelecionado.geraHashlink();
				this.service.saveLink(linkSelecionado);
				if (this.linkSelecionado.getId() == null) {
					this.filtroLink = new FiltroLink();
				}
				this.carregaLinks();
				JSFUtil.executeClientScript("PF('dlgLink').hide()");
				JSFUtil.executeClientUpdate("formLinks:tbLinks");
			}
		} catch (Exception e) {
			JSFUtil.mensagemErro("msgLink", e.getMessage(), null);
		}
	}

	private boolean validaLink() {
		if (this.linkSelecionado.getDataAula() == null || this.linkSelecionado.getDataValidadeLink() == null) {
			JSFUtil.mensagemErro("msgLink", "Os campos obrigatórios devem ser preenchidos", getAddressServer());
			return false;
		}
		return true;
	}

	public void successListener(final ClipboardSuccessEvent successEvent) {
		JSFUtil.mensagemInfo("growl", "URL copiada", null);
	}

	public void errorListener(final ClipboardErrorEvent errorEvent) {
		JSFUtil.mensagemErro("growl", "Não foi possível copiar a URL", null);
	}
	
	public String getUrl(LinkAvaliacao link) {
		try {
			return link.getUrl(this.getUrlServer());
		} catch (Exception e) {
			JSFUtil.mensagemErro("msgLink", e.getMessage(), null);
			return null;
		}
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public FiltroTurma getFiltro() {
		return filtro;
	}

	public List<Turma> getTurmas() {
		return turmas;
	}

	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas;
	}

	public List<LinkAvaliacao> getLinks() {
		return links;
	}

	public void setLinks(List<LinkAvaliacao> links) {
		this.links = links;
	}

	public LinkAvaliacao getLinkSelecionado() {
		return linkSelecionado;
	}

	public void setLinkSelecionado(LinkAvaliacao linkSelecionado) {
		this.linkSelecionado = linkSelecionado;
	}

	public FiltroLink getFiltroLink() {
		return filtroLink;
	}

}
