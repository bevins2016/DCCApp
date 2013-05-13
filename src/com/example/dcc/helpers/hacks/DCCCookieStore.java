package com.example.dcc.helpers.hacks;

import java.io.Serializable;
import org.apache.http.impl.client.BasicCookieStore;

/**
 * This is a class used to extend the BasicCookieStore into a Serializable object. This
 * enables us to pass the cookie store through intents.
 * @author Harmon
 *
 */
@SuppressWarnings("serial")
public class DCCCookieStore extends BasicCookieStore implements Serializable{
}
