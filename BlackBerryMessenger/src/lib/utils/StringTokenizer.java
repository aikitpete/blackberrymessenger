package lib.utils;

public final class StringTokenizer {
	private int _currentPosition;
	private int _newPosition;
	private int _maxPosition;
	private String _str;
	private String _delimiter;
	private boolean _delimsChanged;

	/**
	 * Constructor
	 * 
	 * @param str
	 *            The string to be parsed
	 * @param delim
	 *            The delimiters to split the string on
	 * @param returnDelims
	 *            Flag indicating whether to return the delimiters as tokens
	 */
	public StringTokenizer(String str) {
		// Initialize members
		_currentPosition = 0;
		_newPosition = -1;
		_str = str;
		_maxPosition = _str.length();
		_delimiter = " ";
	}

	/**
	 * Tests if there are more tokens available from this tokenizer's string
	 * 
	 * @return True if there is at least one token in the string after the
	 *         current position, otherwise false
	 */
	public boolean hasMoreTokens() {
		return (_currentPosition < _maxPosition);
	}

	/**
	 * Returns the next token from this string tokenizer
	 * 
	 * @return The next token from this string tokenizer
	 */
	public String nextToken() {
		_currentPosition = (_newPosition >= 0 && !_delimsChanged) ? _newPosition : _currentPosition;
		_delimsChanged = false;
		_newPosition = -1;

		if (_currentPosition >= _maxPosition)
			return null;
		int start = _currentPosition;
		_currentPosition = scanToken(_currentPosition);
		return _str.substring(start, _currentPosition);
	}

	/**
	 * Returns the end position of the next token
	 * 
	 * @param startPos
	 *            Start position of the token
	 * @return The end position of the next token
	 */
	private int scanToken(int startPos) {
		int position = startPos;
		while (position < _maxPosition) {
			char c = _str.charAt(position);

			if (_delimiter.indexOf(c) >= 0) {
				break;
			}
			++position;
		}
		if (startPos == position) {
			char c = _str.charAt(position);

			if (_delimiter.indexOf(c) >= 0) {
				++position;
			}
		}
		return position;
	}
}