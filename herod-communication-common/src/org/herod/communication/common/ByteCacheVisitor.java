package org.herod.communication.common;

public interface ByteCacheVisitor {

	ByteFrame visit(ByteCache cache);

	ByteCacheVisitor NULL_VISITOR = new ByteCacheVisitor() {
		@Override
		public ByteFrame visit(ByteCache cache) {
			return null;
		}
	};
}
