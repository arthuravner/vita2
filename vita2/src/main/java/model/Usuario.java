package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import secutiry.SecurityUtil;

@Entity(name = "usuario")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	public enum NivelAcesso {
		ADMIN("Administrador"), USER("Usuário");

		private final String value;

		private NivelAcesso(final String newValue) {
			value = newValue;
		}

		public String getValue() {
			return value;
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "nome")
	private String nome;

	@Column(name = "email")
	private String email;

	@Column(nullable = false)
	private String login;

	@Column(nullable = false)
	private String senha;

	@Column(columnDefinition = "boolean default true")
	private boolean ativo;

	@Column(name = "nivel_acesso")
	@Enumerated(EnumType.STRING)
	private NivelAcesso nivelDeAcesso;

	@Temporal(TemporalType.DATE)
	private Date dataCadastro;

	@OneToMany(mappedBy = "professor")
	private List<Turma> turmas = new ArrayList<Turma>();

	@Transient
	private String senhaAtual;

	@Transient
	private String novaSenha;

	@Transient
	private String confirmaNovaSenha;

	public Usuario() {
		this.dataCadastro = Calendar.getInstance().getTime();
		this.ativo = true;
	}

	public void copy(Usuario other) {
		this.nome = other.nome;
		this.email = other.email;
		this.login = other.login;
		this.ativo = other.ativo;
		this.nivelDeAcesso = other.nivelDeAcesso;
		this.dataCadastro = other.dataCadastro;
		this.turmas = other.turmas;
		this.senha = other.senha;
	}

	public String getEdicaoSenha() {
		return null;
	}

	public void setEdicaoSenha(String senha) {
		this.senha = SecurityUtil.convertStringToMd5(senha);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public List<Turma> getTurmas() {
		return turmas;
	}

	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public NivelAcesso getNivelDeAcesso() {
		return nivelDeAcesso;
	}

	public void setNivelDeAcesso(NivelAcesso nivelDeAcesso) {
		this.nivelDeAcesso = nivelDeAcesso;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public String getSenhaAtual() {
		return senhaAtual;
	}

	public void setSenhaAtual(String senhaAtual) {
		this.senhaAtual = senhaAtual;
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	public String getConfirmaNovaSenha() {
		return confirmaNovaSenha;
	}

	public void setConfirmaNovaSenha(String confirmaNovaSenha) {
		this.confirmaNovaSenha = confirmaNovaSenha;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		return (obj instanceof Usuario)
				? (this.getId() == null ? this == obj : this.getId().equals(((Usuario) obj).getId()))
				: false;
	}
}
