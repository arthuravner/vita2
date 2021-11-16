package bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import jsf.JSFUtil;
import model.Usuario;
import model.Usuario.NivelAcesso;
import model.filtro.FiltroUsuario;
import secutiry.SecurityUtil;
import service.UsuarioService;

@Named(value = "gestaoUsuariosBean")
@ViewScoped
public class GestaoUsuariosBean extends AbstractBean {
	private static final long serialVersionUID = 1L;

	private FiltroUsuario filtro;

	private Usuario usuarioSelecionado;

	private NivelAcesso nivelAcessoSelecionado;

	private List<Usuario> usuarios;

	@EJB
	private UsuarioService service;

	@PostConstruct
	public void postContruct() {
		filtro = new FiltroUsuario();
		usuarios = new ArrayList<Usuario>();
		usuarioSelecionado = null;
	}

	public void aplicarFiltro() {
		try {
			this.usuarios = this.service.getUsuarios(filtro);
		} catch (Exception e) {
			JSFUtil.mensagemErro(null, e.getMessage(), null);
		}
	}

	public void novo() {
		this.usuarioSelecionado = new Usuario();
	}
	
	public void edita(Usuario usuario) {
		this.usuarioSelecionado = usuario;
	}

	public void salvar() {
		try {
			if (this.validaUsuario()) {
				this.usuarioSelecionado.setNivelDeAcesso(this.nivelAcessoSelecionado);
				this.service.save(usuarioSelecionado);
				aplicarFiltro();
				JSFUtil.executeClientScript("PF('dlgEdicaoUsuario').hide()");
				JSFUtil.executeClientUpdate("formAdmUsuarios:tbUsuarios");
				JSFUtil.executeClientUpdate("messages");
			}
		} catch (Exception e) {
			JSFUtil.mensagemErro(null, e.getMessage(), null);
		}
	}

	public void excluir() {
		try {
			this.service.remove(usuarioSelecionado);
			aplicarFiltro();
		} catch (Exception e) {
			JSFUtil.mensagemErro(null, e.getMessage(), null);
		}

	}

	public boolean validaUsuario() {

		if (StringUtils.isAnyEmpty(this.usuarioSelecionado.getNome(), this.usuarioSelecionado.getLogin(),
				this.usuarioSelecionado.getSenha(), this.usuarioSelecionado.getEmail())) {
			JSFUtil.mensagemErro("msgUsuario", "Todos os campos devem ser preenchidos", null);
			return false;
		}

		return true;
	}
	
	public void trocaSenha() {
		try {
			
			if(!this.getAuth().isAdmin() && !SecurityUtil.convertStringToMd5(this.usuarioSelecionado.getSenhaAtual()).equals(usuarioSelecionado.getSenha())) {
				JSFUtil.mensagemErro("msgTrocaSenha", "Senha atual não confere", null);
				return;
			}
			
			if(!this.usuarioSelecionado.getNovaSenha().equals(usuarioSelecionado.getConfirmaNovaSenha())) {
				JSFUtil.mensagemErro("msgTrocaSenha", "Nova senha e confirma nova senha não conferem", null);
				return;				
			}
			
			this.usuarioSelecionado.setEdicaoSenha(usuarioSelecionado.getNovaSenha());
			
			this.service.save(usuarioSelecionado);
			
			JSFUtil.mensagemInfo(null, "Senha alterada com sucesso", null);
			JSFUtil.executeClientScript("PF('dlgTrocaSenha').hide()");
			
		} catch (Exception e) {
			JSFUtil.mensagemErro("msgTrocaSenha", e.getMessage(), null);
		}
	}

	public List<NivelAcesso> getNiveisAcesso() {
		return Arrays.asList(NivelAcesso.values());
	}

	public Usuario getUsuarioSelecionado() {
		return usuarioSelecionado;
	}

	public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public FiltroUsuario getFiltro() {
		return filtro;
	}

	public NivelAcesso getNivelAcessoSelecionado() {
		return nivelAcessoSelecionado;
	}

	public void setNivelAcessoSelecionado(NivelAcesso nivelAcessoSelecionado) {
		this.nivelAcessoSelecionado = nivelAcessoSelecionado;
	}

}
