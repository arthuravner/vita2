package secutiry;

import java.util.Arrays;
import java.util.HashSet;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;

import jsf.JSFUtil;
import model.Auth;
import model.Usuario;
import service.UsuarioService;

@ApplicationScoped
public class CustomInMemoryIdentityStore implements IdentityStore {

	@EJB
	private UsuarioService service;

	@Override
	public CredentialValidationResult validate(Credential credential) {

		UsernamePasswordCredential loginCredential = (UsernamePasswordCredential) credential;

		String login = loginCredential.getCaller();
		String senha = loginCredential.getPasswordAsString();
		senha = SecurityUtil.convertStringToMd5(senha);
		
		Usuario usuario = null;

		try {
			usuario = service.login(login, senha);
		} catch (Exception e) {
			e.printStackTrace();
		}		

		if (usuario != null) {
			
			Auth auth = new Auth(usuario);
			JSFUtil.setAuth(new Auth(usuario));
			
			if(auth.isAdmin()) {
				return new CredentialValidationResult("admin", new HashSet<>(Arrays.asList("ADMIN")));
			}else {
				return new CredentialValidationResult("user", new HashSet<>(Arrays.asList("USER")));
			}					
		} else {
			return CredentialValidationResult.NOT_VALIDATED_RESULT;
		}
	}
}