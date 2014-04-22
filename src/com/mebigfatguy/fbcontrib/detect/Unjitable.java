/*
 * fb-contrib - Auxiliary detectors for Java programs
 * Copyright (C) 2005-2014 Dave Brosius
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.mebigfatguy.fbcontrib.detect;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.visitclass.PreorderVisitor;

public class Unjitable extends PreorderVisitor implements Detector {

	private static final int UNJITABLE_CODE_LENGTH = 8000;
	
	private BugReporter bugReporter;
	
	public Unjitable(BugReporter bugReporter) {
		this.bugReporter = bugReporter;
	}

	
	@Override
	public void visitClassContext(ClassContext classContext) {
		JavaClass cls = classContext.getJavaClass();
        cls.accept(this);
	}


	@Override
	public void visitCode(Code obj) {
		
		Method m = getMethod();
		if (((m.getAccessFlags() & Constants.ACC_STATIC) == 0) || !"<clinit>".equals(m.getName())) {
			byte[] code = obj.getCode();
			if (code.length >= UNJITABLE_CODE_LENGTH) {
				bugReporter.reportBug(new BugInstance(this, "UJM_UNJITABLE_METHOD", NORMAL_PRIORITY)
								.addClass(this)
								.addMethod(this));
			}
		}
	}
	
	@Override
	public void report() {
	}
}
