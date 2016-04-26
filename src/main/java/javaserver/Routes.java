package javaserver;

import java.util.HashMap;

import javaserver.responders.*;

public class Routes {

	private static HashMap<String, Responder> routeResponders = new HashMap<String, Responder>();
	static {
		routeResponders.put("/", new RedirectResponder(new String[] {"GET"}));
		routeResponders.put("/gameboard", new FormResponder(new String[] {"GET", "POST", "PUT", "DELETE"}, new Form(new String[] {})));
	}

	public static Responder getResponder(String route) {
		Responder responder = routeResponders.get(route);
		if (responder == null) {
			return new ErrorResponder();
		} else {
			return responder;
		}
	}
}
