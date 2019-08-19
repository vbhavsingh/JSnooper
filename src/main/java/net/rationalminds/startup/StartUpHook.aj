package net.rationalminds.startup;

import net.rationalminds.util.VersionInformationPrinter;

public abstract aspect StartUpHook {
		
	public pointcut start():execution(* *.main(..)) &&  if(VersionInformationPrinter.getStatus());
	
	before():start() && if(VersionInformationPrinter.getStatus()){
		VersionInformationPrinter.print();
	}

}
