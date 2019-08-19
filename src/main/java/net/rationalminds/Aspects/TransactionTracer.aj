package net.rationalminds.Aspects;

import java.lang.reflect.Method;

public abstract aspect TransactionTracer {

	public abstract pointcut runTrace();

	before(): runTrace(){
		System.out.print(thisJoinPoint + " *AJT*" + "\n");
		Object[] obj = thisJoinPoint.getArgs();
		if (obj != null) {
			for (int i = 0; i < obj.length; i++) {
				Method[] m = null;
				if (obj[i] != null) {
					m = obj[i].getClass().getMethods();
					System.out.print("\t" + obj[i].getClass().getName()
							+ " *ARGUMENT*" + "\n");
					if (m != null) {
						for (int j = 0; j < m.length && m[j] != null; j++) {
							System.out.print("\t\t" + m[j].getName()
									+ " *METHOD TYPE* "
									+ m[j].getGenericReturnType() + "\n");
						}
					}
				}
			}
		}
	}

}
