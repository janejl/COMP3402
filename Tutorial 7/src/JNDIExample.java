import javax.naming.* ;

import java.io.Serializable;
import java.util.Hashtable ; 

public class JNDIExample {

	public static void main(String[] args) {
		
		 Hashtable<String, String> env = new Hashtable<String, String>() ;
		 env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory") ;
		 env.put(Context.PROVIDER_URL, "ldap://localhost:10389/dc=example,dc=com") ;
		 env.put(Context.SECURITY_AUTHENTICATION, "simple") ;
		 env.put(Context.SECURITY_PRINCIPAL, "uid=admin, ou=system") ;
		 env.put(Context.SECURITY_CREDENTIALS, "secret") ;
		 
		 try {
			 Context ctx = new InitialContext(env) ;
			 String message = "Testing";
			 //ctx.rebind( "cn=test", message) ;
			 String user = "Jane";
			 String subject = "Notice";
			 Message msn = new Message(user, subject, message);//
			 ctx.rebind("cn=test", msn);//
			 
			 
			 //String msg = (String) ctx.lookup( "cn=test") ;
			 Message msg = (Message) ctx.lookup("cn=test");//
			 
			 System.out.println("User: " + msg.user) ;
			 System.out.println("Subject: " + msg.subject);
			 System.out.println("Content: " + msg.content);
		 } catch (NamingException e) {
			 e.printStackTrace() ; 
		 }	
	}
	
	// Exercise 1:
	public static class Message implements Serializable{
		public String user, subject, content;
		
		public Message(String u, String s, String c){
			user = u;
			subject = s;
			content = c;
		}
	}
}

