/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package org.springframework.web.servlet.http;

import java.util.Enumeration;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * {@link HttpSession} implementation wrapping and proxying for an existing {@link HttpSession} instance.
 *
 * @author John Blum
 * @see jakarta.servlet.ServletContext
 * @see jakarta.servlet.http.HttpSession
 * @see org.springframework.web.servlet.http.AbstractHttpSession
 * @since 1.4.0
 */
public class HttpSessionProxy extends AbstractHttpSession {

	public static HttpSessionProxy from(HttpSession session) {
		return new HttpSessionProxy(session);
	}

	private final HttpSession session;

	private HttpSessionProxy(@NonNull HttpSession session) {

		Assert.notNull(session, "HttpSession must not be null");

		this.session = session;
	}

	protected @NonNull HttpSession getSession() {
		return this.session;
	}

	@Override
	public String getId() {
		return getSession().getId();
	}

	@Override
	public long getCreationTime() {
		return getSession().getCreationTime();
	}

	@Override
	public long getLastAccessedTime() {
		return getSession().getLastAccessedTime();
	}

	@Override
	public void setMaxInactiveInterval(int interval) {
		getSession().setMaxInactiveInterval(interval);
	}

	@Override
	public int getMaxInactiveInterval() {
		return getSession().getMaxInactiveInterval();
	}

	@Override
	public @NonNull ServletContext getServletContext() {
		return getSession().getServletContext();
	}

	@Override
	public void setAttribute(String name, Object value) {
		getSession().setAttribute(name, value);
	}

	@Override
	public @Nullable Object getAttribute(String name) {
		return getSession().getAttribute(name);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return getSession().getAttributeNames();
	}

	@Override
	public void removeAttribute(String name) {
		getSession().removeAttribute(name);
	}

	@Override
	public void invalidate() {
		getSession().invalidate();
	}

	@Override
	public boolean isNew() {
		return getSession().isNew();
	}
}
