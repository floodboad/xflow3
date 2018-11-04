package org.xsnake.cloud.xflow3.core.context;

public interface IContext {

	public final static String RECORD_ID = "RECORD_ID";
	
	public final static String FROM_TRANSITION = "FROM_TRANSITION";
	
	public final static String RECORD_SN = "RECORD_SN";
	
	Object getAttribute(final String id);

    void setAttribute(final String id, final Object obj);

    Object removeAttribute(final String id);

    void clear();
	
}
