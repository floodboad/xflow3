package org.xsnake.cloud.xflow3.core.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Context implements IContext{

	protected Map<String,Object> map = new ConcurrentHashMap<String, Object>();
	
    public Object getAttribute(final String id) {
        Object obj = this.map.get(id);
        return obj;
    }

    public void setAttribute(final String id, final Object obj) {
        if (obj != null) {
            this.map.put(id, obj);
        } else {
            this.map.remove(id);
        }
    }
    
    public Object removeAttribute(final String id) {
        return this.map.remove(id);
    }

    public void clear() {
        this.map.clear();
    }

    @Override
    public String toString() {
        return this.map.toString();
    }
    
}
