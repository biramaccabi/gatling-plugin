/**
 * Copyright 2011-2012 eBusiness Information, Groupe Excilys (www.excilys.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gatling.jenkins.chart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class Serie<X extends Number, Y extends Number> implements JsonSerializable {
	private final List<Point<X, Y>> points = new ArrayList<Point<X, Y>>();

	public void addPoint(X x, Y y) {
		points.add(new Point<X, Y>(x, y));
	}

	public void serialize(JsonGenerator jgen, SerializerProvider provider) throws IOException {
		jgen.writeObject(points);
	}

	public void serializeWithType(JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) {
		throw new UnsupportedOperationException();
	}
}
