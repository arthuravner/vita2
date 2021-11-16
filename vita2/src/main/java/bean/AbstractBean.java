package bean;

import java.io.Serializable;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jsf.JSFUtil;
import model.Auth;
import model.Usuario;

public abstract class AbstractBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public Auth getAuth() {
		return JSFUtil.getAuth();
	}
	
	public Usuario getUsuario() {
		final HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
				.getSession(false);
		return (Usuario) session.getAttribute("usuario");
	}

	public void setUsuario(Usuario usuario) {
		final HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
				.getSession(false);
		session.setAttribute("usuario", usuario);
	}

	public String getHost() {
		final FacesContext context = FacesContext.getCurrentInstance();
		final ExternalContext externalContext = context.getExternalContext();
		final HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		return request.getRemoteHost();
	}

	public String getUserAgent() {
		final FacesContext context = FacesContext.getCurrentInstance();
		final ExternalContext externalContext = context.getExternalContext();
		final HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		return request.getHeader("User-Agent");
	}

	public boolean isInMobile() {
		return this.getUserAgent().indexOf("Mobile") > -1;
	}

	public String getPageRequest() {
		final FacesContext context = FacesContext.getCurrentInstance();
		final HttpServletRequest httpRequest = (HttpServletRequest) context.getExternalContext().getRequest();
		return httpRequest.getRequestURL().toString();
	}

	public String getAddressServer() {
		final FacesContext context = FacesContext.getCurrentInstance();
		final ExternalContext externalContext = context.getExternalContext();
		final HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		String name = "";
		String address = "";
		try {
			name = request.getLocalName();
		} catch (final Exception e) {
		}
		try {
			address = request.getServerName();
		} catch (final Exception e) {
		}
		return address + " - " + name;

	}
	
	public String getUrlServer() {
	    final FacesContext context = FacesContext.getCurrentInstance();
	    final ExternalContext externalContext = context.getExternalContext();
	    final HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
	    if (request.getServerPort() == 443) {
	      return "https://" + request.getServerName();
	    }
	    if (request.getServerPort() == 80) {
	      return "http://" + request.getServerName();
	    }
	    return "http://" + request.getServerName() + ":" + request.getServerPort();
	  }	

}
