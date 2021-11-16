package secutiry;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;

import bean.MainBean;
import jsf.JSFUtil;

@Named
@SessionScoped
public class LoginBacking extends MainBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "Por favor, entre com uma senha")
	private String password;

	@NotEmpty(message = "Por favor, entre com um usuário")
	private String login;

	@Inject
	private SecurityContext securityContext;

	@Inject
	private ExternalContext externalContext;

	@Inject
	private FacesContext facesContext;

	public void submit() throws IOException {

		switch (continueAuthentication()) {
		case SEND_FAILURE:
			JSFUtil.mensagemErro("msg", "Usuário ou senha incorretos. Por favor, tente novamente.", null);
			break;
		case SEND_CONTINUE:
			facesContext.responseComplete();
			break;
		case SUCCESS:
			JSFUtil.mensagemInfo(null, "Login bem sucedido", null);
			externalContext.redirect(externalContext.getRequestContextPath() + "/app/views/home.xhtml");
			break;
		case NOT_DONE:
			System.out.println("NOT_NONE login");
		}
	}

	private AuthenticationStatus continueAuthentication() {
		return securityContext.authenticate((HttpServletRequest) externalContext.getRequest(),
				(HttpServletResponse) externalContext.getResponse(),
				AuthenticationParameters.withParams().credential(new UsernamePasswordCredential(login, password)));
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

}
