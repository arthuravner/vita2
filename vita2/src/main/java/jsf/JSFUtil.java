package jsf;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;

import model.Auth;

public class JSFUtil {

	public static final String AUTH_ATT = "auth";

	public static void executeClientScript(final String command) {
		PrimeFaces.current().executeScript(command);
	}

	public static void executeClientUpdate(final String... component) {
		PrimeFaces.current().ajax().update(component);
	}

	public static void mensagemInfo(final String id, final String mensagem, final String detalhe) {
		FacesContext.getCurrentInstance().addMessage(id,
				new FacesMessage(FacesMessage.SEVERITY_INFO, mensagem, detalhe));
	}

	public static void mensagemErro(final String id, String mensagem, final String detalhe) {
		if(StringUtils.isEmpty(mensagem)) {
			mensagem = "messages";
		}
		FacesContext.getCurrentInstance().addMessage(id,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, detalhe));
	}

	public static void mensagemAviso(final String id, final String mensagem, final String detalhe) {
		FacesContext.getCurrentInstance().addMessage(id,
				new FacesMessage(FacesMessage.SEVERITY_WARN, mensagem, detalhe));

	}

	public static void mensagemFatal(final String id, final String mensagem, final String detalhe) {
		FacesContext.getCurrentInstance().addMessage(id,
				new FacesMessage(FacesMessage.SEVERITY_FATAL, mensagem, detalhe));
	}

	public static ExternalContext getContext() {
		return FacesContext.getCurrentInstance().getExternalContext();
	}

	public static String getRequestParam(final String paramName) {
		return getContext().getRequestParameterMap().get(paramName);
	}
	
	public static void setAuth(final Auth auth) {
	    if (auth != null) {
	      final HttpSession session = (HttpSession) getContext().getSession(false);
	      session.setAttribute(AUTH_ATT, auth);
	    } else {
	      final FacesContext context = FacesContext.getCurrentInstance();
	      context.setViewRoot(context.getApplication().getViewHandler().createView(context, "/login.xhtml"));
	      context.getPartialViewContext().setRenderAll(true);
	      context.renderResponse();
	    }
	  }

	public static Auth getAuth() {
		return getAuth(getContext());
	}

	public static Auth getAuth(final ExternalContext context) {
		final HttpSession session = (HttpSession) context.getSession(false);
		Auth auth = null;
		if (session != null) {
			auth = (Auth) session.getAttribute(AUTH_ATT);
		}
		return auth;
	}

	public static Auth logoff() {
		final Auth auth = getAuth();
		final HttpSession session = (HttpSession) getContext().getSession(false);
		session.removeAttribute(AUTH_ATT);
		return auth;
	}

	public static void redirectToAcessoNegado() {
		final FacesContext context = FacesContext.getCurrentInstance();
		context.setViewRoot(context.getApplication().getViewHandler().createView(context, "/acessonegado.xhtml"));
		context.getPartialViewContext().setRenderAll(true);
		context.renderResponse();
	}

	public static void redirectToLogin() {
		final FacesContext context = FacesContext.getCurrentInstance();
		context.setViewRoot(context.getApplication().getViewHandler().createView(context, "/login.xhtml"));
		context.getPartialViewContext().setRenderAll(true);
		context.renderResponse();
	}

}
