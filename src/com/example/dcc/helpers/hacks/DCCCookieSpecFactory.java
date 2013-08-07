package com.example.dcc.helpers.hacks;

import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.params.HttpParams;

/**
 * Class that relaxes the standards of the standard cookie factory
 */
public class DCCCookieSpecFactory implements CookieSpecFactory{

	@Override
	public CookieSpec newInstance(HttpParams params) {
		return new BrowserCompatSpec() {
			@Override
			public void validate(Cookie cookie, CookieOrigin origin)
					throws MalformedCookieException {
			}
		};
	}
}

