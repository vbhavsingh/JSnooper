/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rationalminds.startup;

import java.lang.instrument.Instrumentation;
import org.aspectj.weaver.loadtime.Agent;

import net.rationalminds.util.VersionInformationPrinter;

/**
 *
 * @author Vaibhav Singh
 */
public class Monitor extends Agent {

    public Monitor() {
        super();
    }

    public static void premain(String options, Instrumentation instrumentation) {
        VersionInformationPrinter.print();
        Agent.premain(options, instrumentation);
    }
}
