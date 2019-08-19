package net.rationalminds.Aspects;

public abstract aspect DeadlockResolver {
	
	abstract pointcut pointCut();
	
	before(): pointCut(){
		try {
			System.out.println("AspectJ Thread is "+Thread.currentThread().getName());
			Thread.currentThread().sleep(2*60*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
