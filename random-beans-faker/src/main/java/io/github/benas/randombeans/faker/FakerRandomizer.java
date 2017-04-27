/**
 * The MIT License
 *
 *   Copyright (c) 2017, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */
package io.github.benas.randombeans.faker;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.github.javafaker.Faker;

import io.github.benas.randombeans.api.Randomizer;

public class FakerRandomizer implements Randomizer<Object> {

	private final Field field;
	private final Faker faker;

	public FakerRandomizer(Field field) {
		this(field, new Faker());
	}

	public FakerRandomizer(Field field, Faker faker) {
		this.field = field;
		this.faker = faker;
	}

	@Override
	public Object getRandomValue() {
		io.github.benas.randombeans.faker.FakerValue fakerAnnotation = field
				.getAnnotation(io.github.benas.randombeans.faker.FakerValue.class);

		Method[] methods = Faker.class.getDeclaredMethods();
		for (Method method : methods) {
			Class<?> returnType = method.getReturnType();
			if (fakerAnnotation.category().equals(returnType)) {
				try {
					Object result = method.invoke(faker);
					Method finalMethod = returnType.getMethod(fakerAnnotation.item());
					return finalMethod.invoke(result);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return null;
	}

}
