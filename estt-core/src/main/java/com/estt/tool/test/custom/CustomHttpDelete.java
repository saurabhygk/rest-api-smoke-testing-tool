package com.estt.tool.test.custom;

import java.net.URI;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

/**
 * Created by saurabh.yagnik on 2016/11/04.
  * The HttpEntityEnclosingRequestBase class to support response body to consume end point with HTTP DELETE method type
 */
public class CustomHttpDelete extends HttpEntityEnclosingRequestBase {

    public CustomHttpDelete() {
        super();
    }

    public CustomHttpDelete(final URI uri) {
        super();
        setURI(uri);
    }
    /**
     * @throws IllegalArgumentException if the uri is invalid.
     */
    public CustomHttpDelete(final String uri) {
        super();
        setURI(URI.create(uri));
    }
    @Override
    public String getMethod() {
        return "DELETE";
    }
}
