/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.kernel.util;

/**
 * @author Michael C. Han
 */
public class PrimitiveIntList {

	public PrimitiveIntList() {
		_elements = new int[10];
	}

	public PrimitiveIntList(int capacity) {
		_elements = new int[capacity];
	}

	public void add(int value) {
		_checkCapacity(_elementsSize + 1);

		_elements[_elementsSize++] = value;

		_toString = null;
	}

	public void addAll(int[] values) {
		_checkCapacity(_elementsSize + values.length);

		System.arraycopy(values, 0, _elements, _elementsSize, values.length);

		_elementsSize += values.length;

		_toString = null;
	}

	public int[] getArray() {
		_trim();

		return _elements;
	}

	public int size() {
		return _elementsSize;
	}

	@Override
	public String toString() {
		if (_elementsSize == 0) {
			_toString = "[]";
		}

		if (_toString == null) {
			StringBundler sb = new StringBundler(_elementsSize * 3);

			sb.append(StringPool.OPEN_BRACKET);

			for (int i = 0; i < _elementsSize; i++) {
				long element = _elements[i];

				sb.append(element);

				if ((i + 1) < _elementsSize) {
					sb.append(StringPool.COMMA);
					sb.append(StringPool.SPACE);
				}
			}

			sb.append(StringPool.CLOSE_BRACKET);

			_toString = sb.toString();
		}

		return _toString;
	}

	private void _checkCapacity(int minSize) {
		int oldSize = _elements.length;

		if (minSize > oldSize) {
			int[] previousElements = _elements;

			int newCapacity = (oldSize * 3) / 2 + 1;

			if (newCapacity < minSize) {
				newCapacity = minSize;
			}

			_elements = new int[newCapacity];

			System.arraycopy(previousElements, 0, _elements, 0, _elementsSize);
		}
	}

	private void _trim() {
		int oldSize = _elements.length;

		if (_elementsSize < oldSize) {
			int[] previousElements = _elements;

			_elements = new int[_elementsSize];

			System.arraycopy(previousElements, 0, _elements, 0, _elementsSize);
		}
	}

	private int[] _elements;
	private int _elementsSize;
	private String _toString;

}