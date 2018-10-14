package io.prodity.commons.threads;

import org.jvnet.hk2.annotations.Contract;

@Contract
public interface Threads {

	void main(Runnable runnable);

	void async(Runnable runnable);
	
}
