/*
 * 香港摩比科技有限公司 版权所有
 *
 * www.mobi-inf.com
 */
package org.herod.framework.db;

/**
 * @author Xiong Zhijun
 * @create 2011-11-17
 */
public class SqlTokenizer {
	private String _sql;
	private int _lastCharIdx;
	private int _lastDelimiterPos = -1;
	private int _nextDelimiterPos = -1;
	private boolean _finished;

	public SqlTokenizer(String sql) {
		_sql = sql;
		_lastCharIdx = sql.length() - 1;
	}

	public boolean hasMoreStatements() {
		if (_finished) {
			return false;
		} else {
			if (_nextDelimiterPos <= _lastDelimiterPos) {
				_nextDelimiterPos = _sql.indexOf(';', _lastDelimiterPos + 1);
			}
			return (_nextDelimiterPos >= 0)
					|| (_lastDelimiterPos < _lastCharIdx);
		}
	}

	public String getNextStatement() {
		String result = null;

		if (hasMoreStatements()) {
			if (_nextDelimiterPos >= 0) {
				result = _sql.substring(_lastDelimiterPos + 1,
						_nextDelimiterPos);
				_lastDelimiterPos = _nextDelimiterPos;
			} else {
				result = _sql.substring(_lastDelimiterPos + 1);
				_finished = true;
			}
		}
		return result;
	}
}
