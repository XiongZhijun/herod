package org.herod.communication.common;

public interface Visitable {

	void accept(ByteCacheVisitor visitor);
}
