package bean;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import jsf.JSFUtil;
import model.Avaliacao;
import model.LinkAvaliacao;
import model.filtro.FiltroLink;
import service.LinkAvaliacaoService;

@Named(value = "avaliacaoBean")
@ViewScoped
public class AvaliacaoBean extends AbstractBean {
	private static final long serialVersionUID = 1L;

	private Integer rating;

	private LinkAvaliacao linkAvaliacao;

	String hash = null;

	private Avaliacao avaliacao;

	private boolean enviada;

	@EJB
	private LinkAvaliacaoService service;

	@Inject
	private ExternalContext externalContext;
	
	@PostConstruct
	public void postContruct() {
		this.enviada = false;
		avaliacao = new Avaliacao();
	}

	public void viewAction() {

		try {			
		
			this.hash = URLDecoder.decode(externalContext.getRequestParameterMap().get("h"), StandardCharsets.UTF_8.toString());			

			if (StringUtils.isNotEmpty(hash)) {
				
				FiltroLink filtro = new FiltroLink();
				filtro.setHash(hash);
				LocalDate now = LocalDate.now(ZoneId.of("America/Sao_Paulo"));
				Date date = Date.from(now.atStartOfDay(ZoneId.systemDefault()).toInstant());				
				filtro.setDataValidade(date);
				List<LinkAvaliacao> links = this.service.getLinks(filtro);
				this.linkAvaliacao = links.stream().findFirst().orElse(null);
			} 
		} catch (Exception e) {
			String msgErro = "Ops! Algo deu errado. Contate o adminstrador do sistema.";
			if (StringUtils.isEmpty(e.getMessage())) {
				msgErro = e.getMessage();
			}
			JSFUtil.mensagemErro("msgAvaliacao", msgErro, null);
		}
	}
	
	public void enviar() {
		
		try {
			if(!validaAvaliacao()) {
				return;
			}
			
			avaliacao.setLink(linkAvaliacao);
			
			this.service.save(avaliacao);
					
			this.enviada = true;	
		} catch (Exception e) {
			JSFUtil.mensagemErro("msgAvaliacao", e.getMessage(), null);
		}
		
	}
	
	private boolean validaAvaliacao() {
		
		if(this.avaliacao.getNota() == null ) {
			JSFUtil.mensagemErro("msgAvaliacao", "Dê uma nota de 1 a 5", null);
			return false;
		}
		if(this.linkAvaliacao.isComIdentificacao() && StringUtils.isEmpty(this.avaliacao.getAluno())) {
			JSFUtil.mensagemErro("msgAvaliacao", "Identifique-se", null);
			return false;			
		}		
		if(StringUtils.isEmpty(this.avaliacao.getTexto())) {
			JSFUtil.mensagemErro("msgAvaliacao", "Faça um breve comentário sobre a aula", null);
			return false;
		}
		if(this.avaliacao.getTexto().length() < 10) {
			JSFUtil.mensagemErro("msgAvaliacao", "Escreva mais um pouco sobre a aula", null);
			return false;
		}
		
		//contar palavras?
		
		return true;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Avaliacao getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(Avaliacao avaliacao) {
		this.avaliacao = avaliacao;
	}

	public LinkAvaliacao getLinkAvaliacao() {
		return linkAvaliacao;
	}

	public boolean isEnviada() {
		return enviada;
	}

	public void setEnviada(boolean enviada) {
		this.enviada = enviada;
	}

}
