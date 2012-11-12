package com.reignite.messaging.server;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import com.reignite.messaging.proxy.TypeEditor;

public class SpringInitializedDestination implements Destination, InitializingBean {

	private String name;
	private Object target;
	private Map<String, List<Method>> methodMap = new HashMap<String, List<Method>>();
	private TypeEditor typeEditor;
	private boolean initialized = false;

	@Override
	public String getName() {
		return name;
	}

	public static void main(String[] args) throws Exception {
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Service getService(String operation, Object[] params) {
		List<Method> methods = methodMap.get(operation);
		int paramLength = params == null ? 0 : params.length;
		Service service = null;
		if (methods != null) {
			for (Method method : methods) {
				if (method.getParameterTypes().length == paramLength) {
					Class<?>[] methodTypes = method.getParameterTypes();
					int matches = 0;
					Object[] newParams = new Object[paramLength];
					for (int i = 0; i < paramLength; i++) {
						Object param = params[i];
						Class<?> requiredClass = methodTypes[i];
						if (param == null || requiredClass.isAssignableFrom(param.getClass())) {
							matches++;
							newParams[i] = param;
						} else {
							param = convertParam(param, requiredClass);
							if (param == null || requiredClass.isAssignableFrom(param.getClass())) {
								matches++;
								newParams[i] = param;
							}
						}
					}
					if (matches == paramLength) {
						service = new RAMFService(target, method, newParams);
						break;
					}
				}
			}
		}
		return service;
	}

	private Object convertParam(Object param, Class<?> requiredType) {
		if (typeEditor != null) {
			param = typeEditor.convert(param, requiredType);
		}
		return param;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (target != null) {
			// introspect and cache methods
			BeanInfo info = Introspector.getBeanInfo(target.getClass(), Object.class);
			MethodDescriptor[] meths = info.getMethodDescriptors();
			for (MethodDescriptor meth : meths) {
				Method method = meth.getMethod();
				List<Method> methods = methodMap.get(method.getName());
				if (methods == null) {
					methods = new ArrayList<Method>();
					methodMap.put(method.getName(), methods);
				}
				methods.add(method);
			}
		}
		initialized = true;
	}

	/**
	 * @return the target
	 */
	public Object getTarget() {
		return target;
	}

	/**
	 * @param target
	 *            the target to set
	 * @throws Exception
	 */
	public void setTarget(Object target) throws Exception {
		this.target = target;
		if (initialized) { // target being set outside of spring context.
			initialized = false;
			afterPropertiesSet();
		}
	}

	/**
	 * @return the typeEditor
	 */
	public TypeEditor getTypeEditor() {
		return typeEditor;
	}

	/**
	 * @param typeEditor
	 *            the typeEditor to set
	 */
	public void setTypeEditor(TypeEditor typeEditor) {
		this.typeEditor = typeEditor;
	}

}
